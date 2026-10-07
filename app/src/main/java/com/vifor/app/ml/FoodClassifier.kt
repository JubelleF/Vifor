package com.vifor.app.ml

import android.content.Context
import android.util.Log
import com.vifor.app.data.ViforDatabase
import kotlinx.coroutines.flow.first

data class Recognition(val foodName: String, val confidence: Double)

interface FoodClassifier {
    suspend fun classify(imagePath: String): Recognition?
}

// TEMPORARY: picks a random food so the scan flow can be tested end to end.
// Replace with the real model-based classifier later.
class PlaceholderClassifier(private val context: Context) : FoodClassifier {
    override suspend fun classify(imagePath: String): Recognition? {
        val foods = ViforDatabase.getInstance(context).foodDao().getAllFoods().first()
        val pick = foods.randomOrNull() ?: return null
        return Recognition(foodName = pick.foodName, confidence = 0.0)
    }
}

// Uses the real model if its files are in assets/model, otherwise the temporary classifier
fun createClassifier(context: Context): FoodClassifier =
    try {
        OnnxClassifier(context)
    } catch (e: Exception) {
        Log.w("ViFoR", "Model not loaded, using placeholder classifier: ${e.message}")
        PlaceholderClassifier(context)
    }