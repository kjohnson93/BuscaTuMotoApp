package com.buscatumoto.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "search")
data class Search (
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String
)