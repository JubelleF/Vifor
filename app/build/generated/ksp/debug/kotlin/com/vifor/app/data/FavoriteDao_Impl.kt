package com.vifor.app.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FavoriteDao_Impl(
  __db: RoomDatabase,
) : FavoriteDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFavorite: EntityInsertAdapter<Favorite>
  init {
    this.__db = __db
    this.__insertAdapterOfFavorite = object : EntityInsertAdapter<Favorite>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `tbl_favorites` (`favorite_id`,`food_id`,`date_added`) VALUES (nullif(?, 0),?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Favorite) {
        statement.bindLong(1, entity.favoriteId.toLong())
        statement.bindLong(2, entity.foodId.toLong())
        statement.bindText(3, entity.dateAdded)
      }
    }
  }

  public override suspend fun insert(favorite: Favorite): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfFavorite.insert(_connection, favorite)
  }

  public override fun isFavorite(foodId: Int): Flow<Boolean> {
    val _sql: String = "SELECT EXISTS(SELECT 1 FROM tbl_favorites WHERE food_id = ?)"
    return createFlow(__db, false, arrayOf("tbl_favorites")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, foodId.toLong())
        val _result: Boolean
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp != 0
        } else {
          _result = false
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getFavoriteFoods(): Flow<List<RecognizedFood>> {
    val _sql: String = "SELECT f.* FROM tbl_recognized_food f INNER JOIN tbl_favorites v ON f.food_id = v.food_id ORDER BY v.date_added DESC"
    return createFlow(__db, false, arrayOf("tbl_recognized_food", "tbl_favorites")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfFoodId: Int = getColumnIndexOrThrow(_stmt, "food_id")
        val _columnIndexOfFoodName: Int = getColumnIndexOrThrow(_stmt, "food_name")
        val _columnIndexOfScientificName: Int = getColumnIndexOrThrow(_stmt, "scientific_name")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfImageResource: Int = getColumnIndexOrThrow(_stmt, "image_resource")
        val _result: MutableList<RecognizedFood> = mutableListOf()
        while (_stmt.step()) {
          val _item: RecognizedFood
          val _tmpFoodId: Int
          _tmpFoodId = _stmt.getLong(_columnIndexOfFoodId).toInt()
          val _tmpFoodName: String
          _tmpFoodName = _stmt.getText(_columnIndexOfFoodName)
          val _tmpScientificName: String
          _tmpScientificName = _stmt.getText(_columnIndexOfScientificName)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpImageResource: String
          _tmpImageResource = _stmt.getText(_columnIndexOfImageResource)
          _item = RecognizedFood(_tmpFoodId,_tmpFoodName,_tmpScientificName,_tmpCategory,_tmpImageResource)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteByFoodId(foodId: Int) {
    val _sql: String = "DELETE FROM tbl_favorites WHERE food_id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, foodId.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
