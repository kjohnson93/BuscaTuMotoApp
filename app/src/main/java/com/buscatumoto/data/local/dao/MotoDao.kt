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
    suspend fun insert(motos: List<MotoEntity>)

    @Query("DELETE FROM moto")
//    @Delete
    suspend fun delete()

    @Query("SELECT * FROM moto")
    fun getMotoLiveData(): LiveData<List<MotoEntity>>

    @Query("SELECT * FROM moto")
    fun getMotos(): List<MotoEntity>

    //Tryng to get motyEntity
    //on get objects from DB suspend fun is not required!
    @Query("SELECT * FROM moto WHERE id=:id ")
    fun getMotoById(id: String): LiveData<MotoEntity>

    @Query("SELECT * FROM moto WHERE id=:id ")
    fun getMotoByIdNoLiveData(id: String): MotoEntity
}