package com.vifor.app.data

import android.content.Context
import androidx.room.withTransaction
import kotlinx.coroutines.flow.first
import org.json.JSONObject

object DatabaseSeeder {

    suspend fun seedIfEmpty(context: Context) {
        val db = ViforDatabase.getInstance(context)

        // Default settings row (SDD: tbl_settings holds exactly one record)
        if (db.settingsDao().observe().first() == null) {
            db.settingsDao().save(AppSettings())
        }

        if (db.foodDao().countFoods() > 0) return

        val text = context.assets.open("foods.json").bufferedReader().use { it.readText() }
        val array = JSONObject(text).getJSONArray("foods")

        val foods = mutableListOf<RecognizedFood>()
        val benefits = mutableListOf<HealthBenefit>()
        val facts = mutableListOf<NutritionFact>()

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            val id = o.getInt("food_id")
            foods += RecognizedFood(
                foodId = id,
                foodName = o.getString("food_name"),
                scientificName = o.getString("scientific_name"),
                category = o.getString("category"),
                imageResource = o.getString("image_resource")
            )
            o.optJSONArray("benefits")?.let { b ->
                for (j in 0 until b.length()) {
                    val x = b.getJSONObject(j)
                    benefits += HealthBenefit(
                        foodId = id,
                        benefitTitle = x.getString("title"),
                        benefitDescription = x.getString("description"),
                        benefitCategory = x.getString("category")
                    )
                }
            }
            o.optJSONArray("nutrition")?.let { n ->
                for (j in 0 until n.length()) {
                    val x = n.getJSONObject(j)
                    facts += NutritionFact(
                        foodId = id,
                        nutrientName = x.getString("name"),
                        amount = x.getDouble("amount"),
                        unit = x.getString("unit"),
                        dailyValuePercent =
                            if (x.isNull("daily_value_percent")) null
                            else x.getDouble("daily_value_percent")
                    )
                }
            }
        }

        db.withTransaction {
            db.foodDao().insertFoods(foods)
            db.foodDao().insertBenefits(benefits)
            db.foodDao().insertNutritionFacts(facts)
        }
    }
}