package com.vifor.app.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vifor.app.data.History
import com.vifor.app.data.ViforDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dao = remember { ViforDatabase.getInstance(context).historyDao() }
    val entries by remember { dao.getAll() }.collectAsState(initial = emptyList())
    val selected = remember { mutableStateListOf<Int>() }
    var showClearDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) { Text("‹ Back") }
        Text("History", style = MaterialTheme.typography.headlineMedium)

        Row {
            TextButton(
                enabled = selected.isNotEmpty(),
                onClick = {
                    val ids = selected.toList()
                    selected.clear()
                    scope.launch { dao.deleteByIds(ids) }
                }
            ) { Text("Delete selected") }
            TextButton(
                enabled = entries.isNotEmpty(),
                onClick = { showClearDialog = true }
            ) { Text("Clear all") }
        }

        // TEMPORARY: remove when the scan screen creates real history entries
        TextButton(onClick = {
            val now = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            scope.launch {
                dao.insert(
                    History(scanId = null, dateAccessed = now, foodName = "Apple", imagePath = "")
                )
            }
        }) { Text("Add test entry (temporary)") }

        Spacer(Modifier.height(8.dp))

        if (entries.isEmpty()) {
            Text("No history yet")
        } else {
            LazyColumn {
                items(entries, key = { it.historyId }) { entry ->
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = entry.historyId in selected,
                            onCheckedChange = { checked ->
                                if (checked) selected.add(entry.historyId)
                                else selected.remove(entry.historyId)
                            }
                        )
                        Column {
                            Text(entry.foodName, style = MaterialTheme.typography.titleMedium)
                            Text(entry.dateAccessed, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear all history?") },
            text = { Text("This will delete every history entry.") },
            confirmButton = {
                TextButton(onClick = {
                    showClearDialog = false
                    selected.clear()
                    scope.launch { dao.deleteAll() }
                }) { Text("Clear") }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
            }
        )
    }
}