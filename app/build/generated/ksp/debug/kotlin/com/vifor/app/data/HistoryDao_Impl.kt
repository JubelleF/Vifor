package com.vifor.app.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class HistoryDao_Impl(
  __db: RoomDatabase,
) : HistoryDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfHistory: EntityInsertAdapter<History>
  init {
    this.__db = __db
    this.__insertAdapterOfHistory = object : EntityInsertAdapter<History>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `tbl_history` (`history_id`,`scan_id`,`date_accessed`,`food_name`,`image_path`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: History) {
        statement.bindLong(1, entity.historyId.toLong())
        val _tmpScanId: Int? = entity.scanId
        if (_tmpScanId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpScanId.toLong())
        }
        statement.bindText(3, entity.dateAccessed)
        statement.bindText(4, entity.foodName)
        statement.bindText(5, entity.imagePath)
      }
    }
  }

  public override suspend fun insert(history: History): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfHistory.insert(_connection, history)
  }

  public override fun getAll(): Flow<List<History>> {
    val _sql: String = "SELECT * FROM tbl_history ORDER BY date_accessed DESC"
    return createFlow(__db, false, arrayOf("tbl_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfHistoryId: Int = getColumnIndexOrThrow(_stmt, "history_id")
        val _columnIndexOfScanId: Int = getColumnIndexOrThrow(_stmt, "scan_id")
        val _columnIndexOfDateAccessed: Int = getColumnIndexOrThrow(_stmt, "date_accessed")
        val _columnIndexOfFoodName: Int = getColumnIndexOrThrow(_stmt, "food_name")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "image_path")
        val _result: MutableList<History> = mutableListOf()
        while (_stmt.step()) {
          val _item: History
          val _tmpHistoryId: Int
          _tmpHistoryId = _stmt.getLong(_columnIndexOfHistoryId).toInt()
          val _tmpScanId: Int?
          if (_stmt.isNull(_columnIndexOfScanId)) {
            _tmpScanId = null
          } else {
            _tmpScanId = _stmt.getLong(_columnIndexOfScanId).toInt()
          }
          val _tmpDateAccessed: String
          _tmpDateAccessed = _stmt.getText(_columnIndexOfDateAccessed)
          val _tmpFoodName: String
          _tmpFoodName = _stmt.getText(_columnIndexOfFoodName)
          val _tmpImagePath: String
          _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          _item = History(_tmpHistoryId,_tmpScanId,_tmpDateAccessed,_tmpFoodName,_tmpImagePath)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteByIds(ids: List<Int>) {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("DELETE FROM tbl_history WHERE history_id IN (")
    val _inputSize: Int = ids.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        for (_item: Int in ids) {
          _stmt.bindLong(_argIndex, _item.toLong())
          _argIndex++
        }
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM tbl_history"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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
