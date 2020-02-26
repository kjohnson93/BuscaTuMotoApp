package com.buscatumoto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "search")
data class SearchEntity (
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("search")
    val search: String?,
    @field:SerializedName("brand")
    val brand: String?,
    @field:SerializedName("model")
    var model: String?,
    @field:SerializedName("bikeType")
    val bikeType: String?,
    @field:SerializedName("priceMin")
    val priceMin: String?,
    @field:SerializedName("priceMax")
    val priceMax: String?,
    @field:SerializedName("powerMin")
    val powerMin: String?,
    @field:SerializedName("powerMax")
    val powerMax: String?,
    @field:SerializedName("cilMin")
    val cilMin: String?,
    @field:SerializedName("cilMax")
    val cilMax: String?,
    @field:SerializedName("weightMin")
    val weightMin: String?,
    @field:SerializedName("weightMax")
    val weightMax: String?,
    @field:SerializedName("year")
    val year: String?,
    @field:SerializedName("license")
    val license: String?
//    @field:SerializedName("pageIndex")
//    val pageIndex: Int?

)