package com.vifor.app.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ScanDao_Impl(
  __db: RoomDatabase,
) : ScanDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfScanRecord: EntityInsertAdapter<ScanRecord>
  init {
    this.__db = __db
    this.__insertAdapterOfScanRecord = object : EntityInsertAdapter<ScanRecord>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `tbl_scan_record` (`scan_id`,`image_path`,`scan_date`,`food_name`,`confidence_score`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ScanRecord) {
        statement.bindLong(1, entity.scanId.toLong())
        statement.bindText(2, entity.imagePath)
        statement.bindText(3, entity.scanDate)
        statement.bindText(4, entity.foodName)
        statement.bindDouble(5, entity.confidenceScore)
      }
    }
  }

  public override suspend fun insert(scan: ScanRecord): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfScanRecord.insertAndReturnId(_connection, scan)
    _result
  }

  public override fun getRecentFoods(limit: Int): Flow<List<RecognizedFood>> {
    val _sql: String = "SELECT f.* FROM tbl_recognized_food f INNER JOIN tbl_scan_record s ON f.food_name = s.food_name COLLATE NOCASE GROUP BY f.food_id ORDER BY MAX(s.scan_date) DESC LIMIT ?"
    return createFlow(__db, false, arrayOf("tbl_recognized_food", "tbl_scan_record")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
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

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
