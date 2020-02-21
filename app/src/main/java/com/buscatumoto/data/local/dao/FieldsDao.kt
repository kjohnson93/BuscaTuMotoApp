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
    suspend fun getFields(): FieldsEntity

    @Update
    suspend fun updateModels(fieldsEntity: FieldsEntity)
}