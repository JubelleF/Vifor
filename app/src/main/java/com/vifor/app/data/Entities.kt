package com.vifor.app.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "tbl_scan_record")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "scan_id") val scanId: Int = 0,
    @ColumnInfo(name = "image_path") val imagePath: String,
    @ColumnInfo(name = "scan_date") val scanDate: String,          // YYYY-MM-DD HH:MM:SS
    @ColumnInfo(name = "food_name") val foodName: String,
    @ColumnInfo(name = "confidence_score") val confidenceScore: Double
)

@Entity(tableName = "tbl_recognized_food")
data class RecognizedFood(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "food_id") val foodId: Int = 0,
    @ColumnInfo(name = "food_name") val foodName: String,
    @ColumnInfo(name = "scientific_name") val scientificName: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "image_resource") val imageResource: String
)

@Entity(
    tableName = "tbl_health_benefits",
    foreignKeys = [ForeignKey(
        entity = RecognizedFood::class,
        parentColumns = ["food_id"],
        childColumns = ["food_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("food_id")]
)
data class HealthBenefit(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "benefit_id") val benefitId: Int = 0,
    @ColumnInfo(name = "food_id") val foodId: Int,
    @ColumnInfo(name = "benefit_title") val benefitTitle: String,
    @ColumnInfo(name = "benefit_description") val benefitDescription: String,
    @ColumnInfo(name = "benefit_category") val benefitCategory: String
)

@Entity(
    tableName = "tbl_nutrition_facts",
    foreignKeys = [ForeignKey(
        entity = RecognizedFood::class,
        parentColumns = ["food_id"],
        childColumns = ["food_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("food_id")]
)
data class NutritionFact(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "nutrition_id") val nutritionId: Int = 0,
    @ColumnInfo(name = "food_id") val foodId: Int,
    @ColumnInfo(name = "nutrient_name") val nutrientName: String,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "daily_value_percent") val dailyValuePercent: Double?
)

// scan_id is nullable + SET_NULL so a history entry survives if its scan record is deleted,
// which is what the denormalization note in the SDD requires.
@Entity(
    tableName = "tbl_history",
    foreignKeys = [ForeignKey(
        entity = ScanRecord::class,
        parentColumns = ["scan_id"],
        childColumns = ["scan_id"],
        onDelete = ForeignKey.SET_NULL
    )],
    indices = [Index("scan_id")]
)
data class History(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "history_id") val historyId: Int = 0,
    @ColumnInfo(name = "scan_id") val scanId: Int?,
    @ColumnInfo(name = "date_accessed") val dateAccessed: String,
    @ColumnInfo(name = "food_name") val foodName: String,
    @ColumnInfo(name = "image_path") val imagePath: String
)

@Entity(
    tableName = "tbl_favorites",
    foreignKeys = [ForeignKey(
        entity = RecognizedFood::class,
        parentColumns = ["food_id"],
        childColumns = ["food_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["food_id"], unique = true)]   // a food can be favorited only once
)
data class Favorite(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "favorite_id") val favoriteId: Int = 0,
    @ColumnInfo(name = "food_id") val foodId: Int,
    @ColumnInfo(name = "date_added") val dateAdded: String
)

// Single-row table: always use setting_id = 1
@Entity(tableName = "tbl_settings")
data class AppSettings(
    @PrimaryKey @ColumnInfo(name = "setting_id") val settingId: Int = 1,
    @ColumnInfo(name = "dark_mode") val darkMode: Int = 0,         // 0 = off, 1 = on
    @ColumnInfo(name = "language") val language: String = "en",
    @ColumnInfo(name = "text_size") val textSize: Double = 1.0
)

// Used by the Food Result screen: one food plus its benefits and nutrition facts
data class FoodWithDetails(
    @Embedded val food: RecognizedFood,
    @Relation(parentColumn = "food_id", entityColumn = "food_id")
    val benefits: List<HealthBenefit>,
    @Relation(parentColumn = "food_id", entityColumn = "food_id")
    val nutritionFacts: List<NutritionFact>
)