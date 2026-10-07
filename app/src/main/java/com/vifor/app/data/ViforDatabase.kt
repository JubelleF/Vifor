package com.vifor.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ScanRecord::class,
        RecognizedFood::class,
        HealthBenefit::class,
        NutritionFact::class,
        History::class,
        Favorite::class,
        AppSettings::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ViforDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun scanDao(): ScanDao
    abstract fun historyDao(): HistoryDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile private var INSTANCE: ViforDatabase? = null

        fun getInstance(context: Context): ViforDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ViforDatabase::class.java,
                    "vifor.db"
                ).build().also { INSTANCE = it }
            }
    }
}