package com.vifor.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<RecognizedFood>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBenefits(benefits: List<HealthBenefit>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutritionFacts(facts: List<NutritionFact>)

    @Query("SELECT COUNT(*) FROM tbl_recognized_food")
    suspend fun countFoods(): Int

    @Query("SELECT * FROM tbl_recognized_food ORDER BY food_name")
    fun getAllFoods(): Flow<List<RecognizedFood>>

    @Query("SELECT * FROM tbl_recognized_food WHERE food_name = :name COLLATE NOCASE LIMIT 1")
    suspend fun getFoodByName(name: String): RecognizedFood?

    @Transaction
    @Query("SELECT * FROM tbl_recognized_food WHERE food_id = :foodId")
    suspend fun getFoodWithDetails(foodId: Int): FoodWithDetails?

    @Query(
        "SELECT * FROM tbl_recognized_food " +
                "WHERE food_name LIKE '%' || :query || '%' " +
                "OR scientific_name LIKE '%' || :query || '%' " +
                "OR category LIKE '%' || :query || '%' " +
                "ORDER BY food_name"
    )
    fun search(query: String): Flow<List<RecognizedFood>>
}

@Dao
interface ScanDao {
    @Insert
    suspend fun insert(scan: ScanRecord): Long   // returns the new scan_id
}

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history: History)

    @Query("SELECT * FROM tbl_history ORDER BY date_accessed DESC")
    fun getAll(): Flow<List<History>>

    @Query("DELETE FROM tbl_history WHERE history_id IN (:ids)")
    suspend fun deleteByIds(ids: List<Int>)

    @Query("DELETE FROM tbl_history")
    suspend fun deleteAll()
}

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM tbl_favorites WHERE food_id = :foodId")
    suspend fun deleteByFoodId(foodId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM tbl_favorites WHERE food_id = :foodId)")
    fun isFavorite(foodId: Int): Flow<Boolean>

    @Query(
        "SELECT f.* FROM tbl_recognized_food f " +
                "INNER JOIN tbl_favorites v ON f.food_id = v.food_id " +
                "ORDER BY v.date_added DESC"
    )
    fun getFavoriteFoods(): Flow<List<RecognizedFood>>
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM tbl_settings WHERE setting_id = 1")
    fun observe(): Flow<AppSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(settings: AppSettings)
}