package com.vifor.app.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SettingsDao_Impl(
  __db: RoomDatabase,
) : SettingsDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAppSettings: EntityInsertAdapter<AppSettings>
  init {
    this.__db = __db
    this.__insertAdapterOfAppSettings = object : EntityInsertAdapter<AppSettings>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `tbl_settings` (`setting_id`,`dark_mode`,`language`,`text_size`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AppSettings) {
        statement.bindLong(1, entity.settingId.toLong())
        statement.bindLong(2, entity.darkMode.toLong())
        statement.bindText(3, entity.language)
        statement.bindDouble(4, entity.textSize)
      }
    }
  }

  public override suspend fun save(settings: AppSettings): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfAppSettings.insert(_connection, settings)
  }

  public override fun observe(): Flow<AppSettings?> {
    val _sql: String = "SELECT * FROM tbl_settings WHERE setting_id = 1"
    return createFlow(__db, false, arrayOf("tbl_settings")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfSettingId: Int = getColumnIndexOrThrow(_stmt, "setting_id")
        val _columnIndexOfDarkMode: Int = getColumnIndexOrThrow(_stmt, "dark_mode")
        val _columnIndexOfLanguage: Int = getColumnIndexOrThrow(_stmt, "language")
        val _columnIndexOfTextSize: Int = getColumnIndexOrThrow(_stmt, "text_size")
        val _result: AppSettings?
        if (_stmt.step()) {
          val _tmpSettingId: Int
          _tmpSettingId = _stmt.getLong(_columnIndexOfSettingId).toInt()
          val _tmpDarkMode: Int
          _tmpDarkMode = _stmt.getLong(_columnIndexOfDarkMode).toInt()
          val _tmpLanguage: String
          _tmpLanguage = _stmt.getText(_columnIndexOfLanguage)
          val _tmpTextSize: Double
          _tmpTextSize = _stmt.getDouble(_columnIndexOfTextSize)
          _result = AppSettings(_tmpSettingId,_tmpDarkMode,_tmpLanguage,_tmpTextSize)
        } else {
          _result = null
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
