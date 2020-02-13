package com.buscatumoto.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buscatumoto.data.local.entity.SearchEntity

@Dao
interface SearchDao {

    @Query("SELECT * FROM search ORDER BY id DESC")
    fun getLegoThemes(): LiveData<List<SearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<SearchEntity>)
}