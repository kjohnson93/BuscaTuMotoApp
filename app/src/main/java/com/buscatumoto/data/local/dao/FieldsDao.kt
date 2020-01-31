package com.buscatumoto.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buscatumoto.data.local.entity.Fields

/**
 * This interface represents a fields document
 */
@Dao
interface FieldsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fields: Fields)

    @Query("SELECT * FROM fields")
    fun getFields(): LiveData<Fields>

}