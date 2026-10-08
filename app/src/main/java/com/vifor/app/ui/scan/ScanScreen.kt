package com.vifor.app.ui.scan

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.vifor.app.data.History
import com.vifor.app.data.ScanRecord
import com.vifor.app.data.ViforDatabase
import com.vifor.app.ml.createClassifier
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Copies [file] into the phone's gallery (Pictures/ViFoR). Returns true on success. */
private fun saveCopyToGallery(context: Context, file: File): Boolean = runCatching {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "ViFoR_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/ViFoR")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }
    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
    resolver.openOutputStream(uri)!!.use { out ->
        file.inputStream().use { input -> input.copyTo(out) }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    }
    true
}.getOrDefault(false)

@Composable
fun ScanScreen(
    onResult: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val db = remember { ViforDatabase.getInstance(context) }
    val classifier = remember { createClassifier(context) }
    val scope = rememberCoroutineScope()

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { hasPermission = it }

    LaunchedEffect(Unit) {
        if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    // "Save photo to gallery" switch. Android 9 and older also need a storage permission.
    var saveCopy by remember { mutableStateOf(false) }
    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        saveCopy = granted
        if (!granted) message = "Storage permission is needed to save photos on this Android version."
    }
    fun onSaveCopyToggled(on: Boolean) {
        val needsPermission = on &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        if (needsPermission) {
            storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            saveCopy = on
        }
    }

    // Shared by the camera and the gallery upload: classify, save to history, open the result
    suspend fun processImage(file: File, saveToGallery: Boolean) {
        val recognition = classifier.classify(file.absolutePath)
        val food = recognition?.let { db.foodDao().getFoodByName(it.foodName) }
        if (recognition == null || food == null) {
            file.delete()
            message = "Could not recognize this food. Try again."
            busy = false
            return
        }
        val now = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val scanId = db.scanDao().insert(
            ScanRecord(
                imagePath = file.absolutePath,
                scanDate = now,
                foodName = food.foodName,
                confidenceScore = recognition.confidence
            )
        ).toInt()
        db.historyDao().insert(
            History(
                scanId = scanId,
                dateAccessed = now,
                foodName = food.foodName,
                imagePath = file.absolutePath
            )
        )
        if (saveToGallery) {
            val saved = withContext(Dispatchers.IO) { saveCopyToGallery(context, file) }
            Toast.makeText(
                context,
                if (saved) "Photo saved to gallery" else "Could not save photo to gallery",
                Toast.LENGTH_SHORT
            ).show()
        }
        busy = false
        onResult(food.foodId)
    }

    // Photo picker: needs no storage permission
    val pickLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null || busy) return@rememberLauncherForActivityResult
        busy = true
        message = null
        scope.launch {
            // Copy the picked photo into the app's own storage, same place camera photos go
            val file = withContext(Dispatchers.IO) {
                runCatching {
                    val dest = File(context.filesDir, "scan_${System.currentTimeMillis()}.jpg")
                    context.contentResolver.openInputStream(uri)!!.use { input ->
                        dest.outputStream().use { output -> input.copyTo(output) }
                    }
                    dest
                }.getOrNull()
            }
            if (file == null) {
                message = "Could not open that photo. Try another one."
                busy = false
            } else {
                // Already in the gallery, so never save a second copy
                processImage(file, saveToGallery = false)
            }
        }
    }

    fun capture() {
        if (busy) return
        busy = true
        message = null
        val file = File(context.filesDir, "scan_${System.currentTimeMillis()}.jpg")
        val options = ImageCapture.OutputFileOptions.Builder(file).build()
        val saveThisOne = saveCopy

        imageCapture.takePicture(
            options,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    scope.launch { processImage(file, saveThisOne) }
                }

                override fun onError(exception: ImageCaptureException) {
                    message = "Could not take the photo. Try again."
                    busy = false
                }
            }
        )
    }

    Column(modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) { Text("‹ Back") }

        if (!hasPermission) {
            Text("Camera permission is needed to scan food with the camera.")
            Spacer(Modifier.height(12.dp))
            Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text("Allow camera")
            }
            Spacer(Modifier.height(12.dp))
            message?.let { Text(it) }
        } else {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val providerFuture = ProcessCameraProvider.getInstance(ctx)
                    providerFuture.addListener({
                        val provider = providerFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageCapture
                        )
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
            Spacer(Modifier.height(12.dp))
            message?.let { Text(it) }

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Save photo to gallery", Modifier.weight(1f))
                Switch(
                    checked = saveCopy,
                    onCheckedChange = { onSaveCopyToggled(it) },
                    enabled = !busy
                )
            }

            Button(
                onClick = { capture() },
                enabled = !busy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (busy) "Recognizing…" else "Scan")
            }
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = {
                pickLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            enabled = !busy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Upload photo from gallery")
        }
    }
}