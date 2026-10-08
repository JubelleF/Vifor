package com.vifor.app.`data`

import androidx.collection.LongSparseArray
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndex
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.room.util.recursiveFetchLongSparseArray
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
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
public class FoodDao_Impl(
  __db: RoomDatabase,
) : FoodDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfRecognizedFood: EntityInsertAdapter<RecognizedFood>

  private val __insertAdapterOfHealthBenefit: EntityInsertAdapter<HealthBenefit>

  private val __insertAdapterOfNutritionFact: EntityInsertAdapter<NutritionFact>
  init {
    this.__db = __db
    this.__insertAdapterOfRecognizedFood = object : EntityInsertAdapter<RecognizedFood>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `tbl_recognized_food` (`food_id`,`food_name`,`scientific_name`,`category`,`image_resource`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: RecognizedFood) {
        statement.bindLong(1, entity.foodId.toLong())
        statement.bindText(2, entity.foodName)
        statement.bindText(3, entity.scientificName)
        statement.bindText(4, entity.category)
        statement.bindText(5, entity.imageResource)
      }
    }
    this.__insertAdapterOfHealthBenefit = object : EntityInsertAdapter<HealthBenefit>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `tbl_health_benefits` (`benefit_id`,`food_id`,`benefit_title`,`benefit_description`,`benefit_category`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: HealthBenefit) {
        statement.bindLong(1, entity.benefitId.toLong())
        statement.bindLong(2, entity.foodId.toLong())
        statement.bindText(3, entity.benefitTitle)
        statement.bindText(4, entity.benefitDescription)
        statement.bindText(5, entity.benefitCategory)
      }
    }
    this.__insertAdapterOfNutritionFact = object : EntityInsertAdapter<NutritionFact>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `tbl_nutrition_facts` (`nutrition_id`,`food_id`,`nutrient_name`,`amount`,`unit`,`daily_value_percent`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: NutritionFact) {
        statement.bindLong(1, entity.nutritionId.toLong())
        statement.bindLong(2, entity.foodId.toLong())
        statement.bindText(3, entity.nutrientName)
        statement.bindDouble(4, entity.amount)
        statement.bindText(5, entity.unit)
        val _tmpDailyValuePercent: Double? = entity.dailyValuePercent
        if (_tmpDailyValuePercent == null) {
          statement.bindNull(6)
        } else {
          statement.bindDouble(6, _tmpDailyValuePercent)
        }
      }
    }
  }

  public override suspend fun insertFoods(foods: List<RecognizedFood>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfRecognizedFood.insert(_connection, foods)
  }

  public override suspend fun insertBenefits(benefits: List<HealthBenefit>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfHealthBenefit.insert(_connection, benefits)
  }

  public override suspend fun insertNutritionFacts(facts: List<NutritionFact>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfNutritionFact.insert(_connection, facts)
  }

  public override suspend fun countFoods(): Int {
    val _sql: String = "SELECT COUNT(*) FROM tbl_recognized_food"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllFoods(): Flow<List<RecognizedFood>> {
    val _sql: String = "SELECT * FROM tbl_recognized_food ORDER BY food_name"
    return createFlow(__db, false, arrayOf("tbl_recognized_food")) { _connection ->
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

  public override suspend fun getFoodByName(name: String): RecognizedFood? {
    val _sql: String = "SELECT * FROM tbl_recognized_food WHERE food_name = ? COLLATE NOCASE LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, name)
        val _columnIndexOfFoodId: Int = getColumnIndexOrThrow(_stmt, "food_id")
        val _columnIndexOfFoodName: Int = getColumnIndexOrThrow(_stmt, "food_name")
        val _columnIndexOfScientificName: Int = getColumnIndexOrThrow(_stmt, "scientific_name")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfImageResource: Int = getColumnIndexOrThrow(_stmt, "image_resource")
        val _result: RecognizedFood?
        if (_stmt.step()) {
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
          _result = RecognizedFood(_tmpFoodId,_tmpFoodName,_tmpScientificName,_tmpCategory,_tmpImageResource)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFoodWithDetails(foodId: Int): FoodWithDetails? {
    val _sql: String = "SELECT * FROM tbl_recognized_food WHERE food_id = ?"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, foodId.toLong())
        val _columnIndexOfFoodId: Int = getColumnIndexOrThrow(_stmt, "food_id")
        val _columnIndexOfFoodName: Int = getColumnIndexOrThrow(_stmt, "food_name")
        val _columnIndexOfScientificName: Int = getColumnIndexOrThrow(_stmt, "scientific_name")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfImageResource: Int = getColumnIndexOrThrow(_stmt, "image_resource")
        val _collectionBenefits: LongSparseArray<MutableList<HealthBenefit>> = LongSparseArray<MutableList<HealthBenefit>>()
        val _collectionNutritionFacts: LongSparseArray<MutableList<NutritionFact>> = LongSparseArray<MutableList<NutritionFact>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfFoodId)
          if (!_collectionBenefits.containsKey(_tmpKey)) {
            _collectionBenefits.put(_tmpKey, mutableListOf())
          }
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfFoodId)
          if (!_collectionNutritionFacts.containsKey(_tmpKey_1)) {
            _collectionNutritionFacts.put(_tmpKey_1, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshiptblHealthBenefitsAscomViforAppDataHealthBenefit(_connection, _collectionBenefits)
        __fetchRelationshiptblNutritionFactsAscomViforAppDataNutritionFact(_connection, _collectionNutritionFacts)
        val _result: FoodWithDetails?
        if (_stmt.step()) {
          val _tmpFood: RecognizedFood
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
          _tmpFood = RecognizedFood(_tmpFoodId,_tmpFoodName,_tmpScientificName,_tmpCategory,_tmpImageResource)
          val _tmpBenefitsCollection: MutableList<HealthBenefit>
          val _tmpKey_2: Long
          _tmpKey_2 = _stmt.getLong(_columnIndexOfFoodId)
          _tmpBenefitsCollection = checkNotNull(_collectionBenefits.get(_tmpKey_2))
          val _tmpNutritionFactsCollection: MutableList<NutritionFact>
          val _tmpKey_3: Long
          _tmpKey_3 = _stmt.getLong(_columnIndexOfFoodId)
          _tmpNutritionFactsCollection = checkNotNull(_collectionNutritionFacts.get(_tmpKey_3))
          _result = FoodWithDetails(_tmpFood,_tmpBenefitsCollection,_tmpNutritionFactsCollection)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun search(query: String): Flow<List<RecognizedFood>> {
    val _sql: String = "SELECT * FROM tbl_recognized_food WHERE food_name LIKE '%' || ? || '%' OR scientific_name LIKE '%' || ? || '%' OR category LIKE '%' || ? || '%' ORDER BY food_name"
    return createFlow(__db, false, arrayOf("tbl_recognized_food")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
        _argIndex = 3
        _stmt.bindText(_argIndex, query)
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

  private fun __fetchRelationshiptblHealthBenefitsAscomViforAppDataHealthBenefit(_connection: SQLiteConnection, _map: LongSparseArray<MutableList<HealthBenefit>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshiptblHealthBenefitsAscomViforAppDataHealthBenefit(_connection, _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `benefit_id`,`food_id`,`benefit_title`,`benefit_description`,`benefit_category` FROM `tbl_health_benefits` WHERE `food_id` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      val _itemKeyIndex: Int = getColumnIndex(_stmt, "food_id")
      if (_itemKeyIndex == -1) {
        return
      }
      val _columnIndexOfBenefitId: Int = 0
      val _columnIndexOfFoodId: Int = 1
      val _columnIndexOfBenefitTitle: Int = 2
      val _columnIndexOfBenefitDescription: Int = 3
      val _columnIndexOfBenefitCategory: Int = 4
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<HealthBenefit>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: HealthBenefit
          val _tmpBenefitId: Int
          _tmpBenefitId = _stmt.getLong(_columnIndexOfBenefitId).toInt()
          val _tmpFoodId: Int
          _tmpFoodId = _stmt.getLong(_columnIndexOfFoodId).toInt()
          val _tmpBenefitTitle: String
          _tmpBenefitTitle = _stmt.getText(_columnIndexOfBenefitTitle)
          val _tmpBenefitDescription: String
          _tmpBenefitDescription = _stmt.getText(_columnIndexOfBenefitDescription)
          val _tmpBenefitCategory: String
          _tmpBenefitCategory = _stmt.getText(_columnIndexOfBenefitCategory)
          _item_1 = HealthBenefit(_tmpBenefitId,_tmpFoodId,_tmpBenefitTitle,_tmpBenefitDescription,_tmpBenefitCategory)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  private fun __fetchRelationshiptblNutritionFactsAscomViforAppDataNutritionFact(_connection: SQLiteConnection, _map: LongSparseArray<MutableList<NutritionFact>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshiptblNutritionFactsAscomViforAppDataNutritionFact(_connection, _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `nutrition_id`,`food_id`,`nutrient_name`,`amount`,`unit`,`daily_value_percent` FROM `tbl_nutrition_facts` WHERE `food_id` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      val _itemKeyIndex: Int = getColumnIndex(_stmt, "food_id")
      if (_itemKeyIndex == -1) {
        return
      }
      val _columnIndexOfNutritionId: Int = 0
      val _columnIndexOfFoodId: Int = 1
      val _columnIndexOfNutrientName: Int = 2
      val _columnIndexOfAmount: Int = 3
      val _columnIndexOfUnit: Int = 4
      val _columnIndexOfDailyValuePercent: Int = 5
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<NutritionFact>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: NutritionFact
          val _tmpNutritionId: Int
          _tmpNutritionId = _stmt.getLong(_columnIndexOfNutritionId).toInt()
          val _tmpFoodId: Int
          _tmpFoodId = _stmt.getLong(_columnIndexOfFoodId).toInt()
          val _tmpNutrientName: String
          _tmpNutrientName = _stmt.getText(_columnIndexOfNutrientName)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpUnit: String
          _tmpUnit = _stmt.getText(_columnIndexOfUnit)
          val _tmpDailyValuePercent: Double?
          if (_stmt.isNull(_columnIndexOfDailyValuePercent)) {
            _tmpDailyValuePercent = null
          } else {
            _tmpDailyValuePercent = _stmt.getDouble(_columnIndexOfDailyValuePercent)
          }
          _item_1 = NutritionFact(_tmpNutritionId,_tmpFoodId,_tmpNutrientName,_tmpAmount,_tmpUnit,_tmpDailyValuePercent)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
