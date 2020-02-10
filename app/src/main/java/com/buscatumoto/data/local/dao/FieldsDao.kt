package com.buscatumoto.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

}