package com.buscatumoto.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buscatumoto.data.local.entity.FieldsEntity

/**
 * This interface represents a fields document
 */
@Dao
interface FieldsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fieldsEntity: FieldsEntity)

    @Query("SELECT * FROM fields")
    fun getFieldsLiveData(): LiveData<FieldsEntity>

    @Query("SELECT * FROM fields")
    fun getFields(): List<FieldsEntity>

    @Query("SELECT models FROM fields")
    fun getModels(): LiveData<List<String>>


//    @Query("UPDATE tableName SET
//            field1 = :value1,
//        field2 = :value2,
//        ...
//    //some more fields to update
//    ...
//    field_N= :value_N
//    WHERE id = :id)

    @Query("UPDATE fields SET models= :models")
    suspend fun updateModels(models: List<String>): Int

}