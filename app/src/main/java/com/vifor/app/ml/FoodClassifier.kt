package com.vifor.app.ml

import android.content.Context
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