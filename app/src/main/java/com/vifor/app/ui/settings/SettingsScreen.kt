package com.vifor.app.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vifor.app.data.AppSettings
import com.vifor.app.data.ViforDatabase
import kotlin.math.abs
import kotlinx.coroutines.launch

private val textSizeOptions = listOf(
    "Small" to 0.85,
    "Normal" to 1.0,
    "Large" to 1.2,
    "Extra large" to 1.4
)

private val languageOptions = listOf(
    "English" to "en",
    "Filipino" to "fil"
)

@Composable
fun SettingsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dao = remember { ViforDatabase.getInstance(context).settingsDao() }
    val saved by remember { dao.observe() }.collectAsState(initial = null)
    val settings = saved ?: AppSettings()
    val scope = rememberCoroutineScope()

    fun update(new: AppSettings) {
        scope.launch { dao.save(new) }
    }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onBack) { Text("‹ Back") }
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark mode", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            Switch(
                checked = settings.darkMode == 1,
                onCheckedChange = { update(settings.copy(darkMode = if (it) 1 else 0)) }
            )
        }

        Spacer(Modifier.height(20.dp))
        Text("Text size", style = MaterialTheme.typography.titleMedium)
        textSizeOptions.forEach { (label, value) ->
            OptionRow(
                label = label,
                selected = abs(settings.textSize - value) < 0.01,
                onSelect = { update(settings.copy(textSize = value)) }
            )
        }

        Spacer(Modifier.height(20.dp))
        Text("Language", style = MaterialTheme.typography.titleMedium)
        languageOptions.forEach { (label, code) ->
            OptionRow(
                label = label,
                selected = settings.language == code,
                onSelect = { update(settings.copy(language = code)) }
            )
        }
    }
}

@Composable
private fun OptionRow(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable { onSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(label)
    }
}