package com.buscatumoto.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buscatumoto.data.local.entity.MotoEntity

@Dao
interface MotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(motos: MotoEntity)

    @Query("SELECT * FROM moto")
    fun getMotos(): List<MotoEntity>

    @Query("SELECT * FROM moto WHERE id=:id ")
    fun getMotoById(id: String): MotoEntity
}