package com.vifor.app.`data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ViforDatabase_Impl : ViforDatabase() {
  private val _foodDao: Lazy<FoodDao> = lazy {
    FoodDao_Impl(this)
  }

  private val _scanDao: Lazy<ScanDao> = lazy {
    ScanDao_Impl(this)
  }

  private val _historyDao: Lazy<HistoryDao> = lazy {
    HistoryDao_Impl(this)
  }

  private val _favoriteDao: Lazy<FavoriteDao> = lazy {
    FavoriteDao_Impl(this)
  }

  private val _settingsDao: Lazy<SettingsDao> = lazy {
    SettingsDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "2bbd7999176538c46ba45d3d7acd4681", "8c5a0a054ea2988b20965fc98dd6284c") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tbl_scan_record` (`scan_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `image_path` TEXT NOT NULL, `scan_date` TEXT NOT NULL, `food_name` TEXT NOT NULL, `confidence_score` REAL NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tbl_recognized_food` (`food_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `food_name` TEXT NOT NULL, `scientific_name` TEXT NOT NULL, `category` TEXT NOT NULL, `image_resource` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tbl_health_benefits` (`benefit_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `food_id` INTEGER NOT NULL, `benefit_title` TEXT NOT NULL, `benefit_description` TEXT NOT NULL, `benefit_category` TEXT NOT NULL, FOREIGN KEY(`food_id`) REFERENCES `tbl_recognized_food`(`food_id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_tbl_health_benefits_food_id` ON `tbl_health_benefits` (`food_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tbl_nutrition_facts` (`nutrition_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `food_id` INTEGER NOT NULL, `nutrient_name` TEXT NOT NULL, `amount` REAL NOT NULL, `unit` TEXT NOT NULL, `daily_value_percent` REAL, FOREIGN KEY(`food_id`) REFERENCES `tbl_recognized_food`(`food_id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_tbl_nutrition_facts_food_id` ON `tbl_nutrition_facts` (`food_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tbl_history` (`history_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `scan_id` INTEGER, `date_accessed` TEXT NOT NULL, `food_name` TEXT NOT NULL, `image_path` TEXT NOT NULL, FOREIGN KEY(`scan_id`) REFERENCES `tbl_scan_record`(`scan_id`) ON UPDATE NO ACTION ON DELETE SET NULL )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_tbl_history_scan_id` ON `tbl_history` (`scan_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tbl_favorites` (`favorite_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `food_id` INTEGER NOT NULL, `date_added` TEXT NOT NULL, FOREIGN KEY(`food_id`) REFERENCES `tbl_recognized_food`(`food_id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tbl_favorites_food_id` ON `tbl_favorites` (`food_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tbl_settings` (`setting_id` INTEGER NOT NULL, `dark_mode` INTEGER NOT NULL, `language` TEXT NOT NULL, `text_size` REAL NOT NULL, PRIMARY KEY(`setting_id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2bbd7999176538c46ba45d3d7acd4681')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `tbl_scan_record`")
        connection.execSQL("DROP TABLE IF EXISTS `tbl_recognized_food`")
        connection.execSQL("DROP TABLE IF EXISTS `tbl_health_benefits`")
        connection.execSQL("DROP TABLE IF EXISTS `tbl_nutrition_facts`")
        connection.execSQL("DROP TABLE IF EXISTS `tbl_history`")
        connection.execSQL("DROP TABLE IF EXISTS `tbl_favorites`")
        connection.execSQL("DROP TABLE IF EXISTS `tbl_settings`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsTblScanRecord: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTblScanRecord.put("scan_id", TableInfo.Column("scan_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblScanRecord.put("image_path", TableInfo.Column("image_path", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblScanRecord.put("scan_date", TableInfo.Column("scan_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblScanRecord.put("food_name", TableInfo.Column("food_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblScanRecord.put("confidence_score", TableInfo.Column("confidence_score", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTblScanRecord: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTblScanRecord: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTblScanRecord: TableInfo = TableInfo("tbl_scan_record", _columnsTblScanRecord, _foreignKeysTblScanRecord, _indicesTblScanRecord)
        val _existingTblScanRecord: TableInfo = read(connection, "tbl_scan_record")
        if (!_infoTblScanRecord.equals(_existingTblScanRecord)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tbl_scan_record(com.vifor.app.data.ScanRecord).
              | Expected:
              |""".trimMargin() + _infoTblScanRecord + """
              |
              | Found:
              |""".trimMargin() + _existingTblScanRecord)
        }
        val _columnsTblRecognizedFood: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTblRecognizedFood.put("food_id", TableInfo.Column("food_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblRecognizedFood.put("food_name", TableInfo.Column("food_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblRecognizedFood.put("scientific_name", TableInfo.Column("scientific_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblRecognizedFood.put("category", TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblRecognizedFood.put("image_resource", TableInfo.Column("image_resource", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTblRecognizedFood: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTblRecognizedFood: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTblRecognizedFood: TableInfo = TableInfo("tbl_recognized_food", _columnsTblRecognizedFood, _foreignKeysTblRecognizedFood, _indicesTblRecognizedFood)
        val _existingTblRecognizedFood: TableInfo = read(connection, "tbl_recognized_food")
        if (!_infoTblRecognizedFood.equals(_existingTblRecognizedFood)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tbl_recognized_food(com.vifor.app.data.RecognizedFood).
              | Expected:
              |""".trimMargin() + _infoTblRecognizedFood + """
              |
              | Found:
              |""".trimMargin() + _existingTblRecognizedFood)
        }
        val _columnsTblHealthBenefits: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTblHealthBenefits.put("benefit_id", TableInfo.Column("benefit_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblHealthBenefits.put("food_id", TableInfo.Column("food_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblHealthBenefits.put("benefit_title", TableInfo.Column("benefit_title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblHealthBenefits.put("benefit_description", TableInfo.Column("benefit_description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblHealthBenefits.put("benefit_category", TableInfo.Column("benefit_category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTblHealthBenefits: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysTblHealthBenefits.add(TableInfo.ForeignKey("tbl_recognized_food", "CASCADE", "NO ACTION", listOf("food_id"), listOf("food_id")))
        val _indicesTblHealthBenefits: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTblHealthBenefits.add(TableInfo.Index("index_tbl_health_benefits_food_id", false, listOf("food_id"), listOf("ASC")))
        val _infoTblHealthBenefits: TableInfo = TableInfo("tbl_health_benefits", _columnsTblHealthBenefits, _foreignKeysTblHealthBenefits, _indicesTblHealthBenefits)
        val _existingTblHealthBenefits: TableInfo = read(connection, "tbl_health_benefits")
        if (!_infoTblHealthBenefits.equals(_existingTblHealthBenefits)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tbl_health_benefits(com.vifor.app.data.HealthBenefit).
              | Expected:
              |""".trimMargin() + _infoTblHealthBenefits + """
              |
              | Found:
              |""".trimMargin() + _existingTblHealthBenefits)
        }
        val _columnsTblNutritionFacts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTblNutritionFacts.put("nutrition_id", TableInfo.Column("nutrition_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblNutritionFacts.put("food_id", TableInfo.Column("food_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblNutritionFacts.put("nutrient_name", TableInfo.Column("nutrient_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblNutritionFacts.put("amount", TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblNutritionFacts.put("unit", TableInfo.Column("unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblNutritionFacts.put("daily_value_percent", TableInfo.Column("daily_value_percent", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTblNutritionFacts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysTblNutritionFacts.add(TableInfo.ForeignKey("tbl_recognized_food", "CASCADE", "NO ACTION", listOf("food_id"), listOf("food_id")))
        val _indicesTblNutritionFacts: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTblNutritionFacts.add(TableInfo.Index("index_tbl_nutrition_facts_food_id", false, listOf("food_id"), listOf("ASC")))
        val _infoTblNutritionFacts: TableInfo = TableInfo("tbl_nutrition_facts", _columnsTblNutritionFacts, _foreignKeysTblNutritionFacts, _indicesTblNutritionFacts)
        val _existingTblNutritionFacts: TableInfo = read(connection, "tbl_nutrition_facts")
        if (!_infoTblNutritionFacts.equals(_existingTblNutritionFacts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tbl_nutrition_facts(com.vifor.app.data.NutritionFact).
              | Expected:
              |""".trimMargin() + _infoTblNutritionFacts + """
              |
              | Found:
              |""".trimMargin() + _existingTblNutritionFacts)
        }
        val _columnsTblHistory: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTblHistory.put("history_id", TableInfo.Column("history_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblHistory.put("scan_id", TableInfo.Column("scan_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblHistory.put("date_accessed", TableInfo.Column("date_accessed", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblHistory.put("food_name", TableInfo.Column("food_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblHistory.put("image_path", TableInfo.Column("image_path", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTblHistory: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysTblHistory.add(TableInfo.ForeignKey("tbl_scan_record", "SET NULL", "NO ACTION", listOf("scan_id"), listOf("scan_id")))
        val _indicesTblHistory: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTblHistory.add(TableInfo.Index("index_tbl_history_scan_id", false, listOf("scan_id"), listOf("ASC")))
        val _infoTblHistory: TableInfo = TableInfo("tbl_history", _columnsTblHistory, _foreignKeysTblHistory, _indicesTblHistory)
        val _existingTblHistory: TableInfo = read(connection, "tbl_history")
        if (!_infoTblHistory.equals(_existingTblHistory)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tbl_history(com.vifor.app.data.History).
              | Expected:
              |""".trimMargin() + _infoTblHistory + """
              |
              | Found:
              |""".trimMargin() + _existingTblHistory)
        }
        val _columnsTblFavorites: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTblFavorites.put("favorite_id", TableInfo.Column("favorite_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblFavorites.put("food_id", TableInfo.Column("food_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblFavorites.put("date_added", TableInfo.Column("date_added", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTblFavorites: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysTblFavorites.add(TableInfo.ForeignKey("tbl_recognized_food", "CASCADE", "NO ACTION", listOf("food_id"), listOf("food_id")))
        val _indicesTblFavorites: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTblFavorites.add(TableInfo.Index("index_tbl_favorites_food_id", true, listOf("food_id"), listOf("ASC")))
        val _infoTblFavorites: TableInfo = TableInfo("tbl_favorites", _columnsTblFavorites, _foreignKeysTblFavorites, _indicesTblFavorites)
        val _existingTblFavorites: TableInfo = read(connection, "tbl_favorites")
        if (!_infoTblFavorites.equals(_existingTblFavorites)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tbl_favorites(com.vifor.app.data.Favorite).
              | Expected:
              |""".trimMargin() + _infoTblFavorites + """
              |
              | Found:
              |""".trimMargin() + _existingTblFavorites)
        }
        val _columnsTblSettings: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTblSettings.put("setting_id", TableInfo.Column("setting_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblSettings.put("dark_mode", TableInfo.Column("dark_mode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblSettings.put("language", TableInfo.Column("language", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTblSettings.put("text_size", TableInfo.Column("text_size", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTblSettings: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTblSettings: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTblSettings: TableInfo = TableInfo("tbl_settings", _columnsTblSettings, _foreignKeysTblSettings, _indicesTblSettings)
        val _existingTblSettings: TableInfo = read(connection, "tbl_settings")
        if (!_infoTblSettings.equals(_existingTblSettings)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tbl_settings(com.vifor.app.data.AppSettings).
              | Expected:
              |""".trimMargin() + _infoTblSettings + """
              |
              | Found:
              |""".trimMargin() + _existingTblSettings)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "tbl_scan_record", "tbl_recognized_food", "tbl_health_benefits", "tbl_nutrition_facts", "tbl_history", "tbl_favorites", "tbl_settings")
  }

  public override fun clearAllTables() {
    super.performClear(true, "tbl_scan_record", "tbl_recognized_food", "tbl_health_benefits", "tbl_nutrition_facts", "tbl_history", "tbl_favorites", "tbl_settings")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(FoodDao::class, FoodDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ScanDao::class, ScanDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(HistoryDao::class, HistoryDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(FavoriteDao::class, FavoriteDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SettingsDao::class, SettingsDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun foodDao(): FoodDao = _foodDao.value

  public override fun scanDao(): ScanDao = _scanDao.value

  public override fun historyDao(): HistoryDao = _historyDao.value

  public override fun favoriteDao(): FavoriteDao = _favoriteDao.value

  public override fun settingsDao(): SettingsDao = _settingsDao.value
}
