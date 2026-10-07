package com.vifor.app.ml

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.nio.FloatBuffer
import kotlin.math.exp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OnnxClassifier(context: Context) : FoodClassifier {

    private val env: OrtEnvironment = OrtEnvironment.getEnvironment()
    private val session: OrtSession
    private val labels: List<String>

    init {
        val modelBytes = context.assets.open("model/vifor_model.onnx").use { it.readBytes() }
        session = env.createSession(modelBytes, OrtSession.SessionOptions())
        labels = context.assets.open("model/labels.txt").bufferedReader()
            .readLines().map { it.trim() }.filter { it.isNotEmpty() }
    }

    override suspend fun classify(imagePath: String): Recognition? =
        withContext(Dispatchers.Default) {
            val bitmap = loadUpright(imagePath) ?: return@withContext null
            val scaled = Bitmap.createScaledBitmap(bitmap, SIZE, SIZE, true)

            // Same preprocessing as training: 224x224, RGB, normalized, channels-first
            val plane = SIZE * SIZE
            val input = FloatArray(3 * plane)
            val pixels = IntArray(plane)
            scaled.getPixels(pixels, 0, SIZE, 0, 0, SIZE, SIZE)
            for (i in pixels.indices) {
                val p = pixels[i]
                val r = ((p shr 16) and 0xFF) / 255f
                val g = ((p shr 8) and 0xFF) / 255f
                val b = (p and 0xFF) / 255f
                input[i] = (r - MEAN[0]) / STD[0]
                input[plane + i] = (g - MEAN[1]) / STD[1]
                input[2 * plane + i] = (b - MEAN[2]) / STD[2]
            }

            val shape = longArrayOf(1, 3, SIZE.toLong(), SIZE.toLong())
            val logits: FloatArray = OnnxTensor.createTensor(env, FloatBuffer.wrap(input), shape)
                .use { tensor ->
                    session.run(mapOf("input" to tensor)).use { result ->
                        @Suppress("UNCHECKED_CAST")
                        (result[0].value as Array<FloatArray>)[0]
                    }
                }

            // softmax
            val max = logits.max()
            val exps = FloatArray(logits.size) { exp(logits[it] - max) }
            val sum = exps.sum()
            var best = 0
            for (i in exps.indices) if (exps[i] > exps[best]) best = i
            val confidence = (exps[best] / sum).toDouble()

            if (best >= labels.size || confidence < MIN_CONFIDENCE) null
            else Recognition(labels[best], confidence)
        }

    private fun loadUpright(path: String): Bitmap? {
        val bmp = BitmapFactory.decodeFile(path) ?: return null
        val degrees = when (
            ExifInterface(path).getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
            )
        ) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        if (degrees == 0f) return bmp
        val m = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, m, true)
    }

    companion object {
        private const val SIZE = 224
        private val MEAN = floatArrayOf(0.485f, 0.456f, 0.406f)
        private val STD = floatArrayOf(0.229f, 0.224f, 0.225f)
        // Below this the app says "Could not recognize". Tune after testing.
        private const val MIN_CONFIDENCE = 0.5
    }
}