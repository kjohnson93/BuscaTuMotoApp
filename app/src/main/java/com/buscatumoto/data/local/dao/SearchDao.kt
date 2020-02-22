package com.buscatumoto.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buscatumoto.data.local.entity.SearchEntity

/**
 * This DAO represents a interface for interacting with data persisted related with params
 * searched on the bike. Search or filter that is.
 */
@Dao
interface SearchDao {

    @Query("SELECT * FROM search ORDER BY id DESC")
    fun getSearchParams(): SearchEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchParams: SearchEntity)

    @Query("DELETE FROM search")
//    @Delete
    suspend fun delete()
}