package com.buscatumoto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "moto")
data class MotoEntity (
                       @PrimaryKey
                       @field:SerializedName("id") val id : String,
                       @field:SerializedName("bikeType") val bikeType : String,
                       @field:SerializedName("brand") val brand : String,
                       @field:SerializedName("model") val model : String,
                       @field:SerializedName("price") val price : Int,
                       @field:SerializedName("power") val power : Int,
                       @field:SerializedName("displacement") val displacement : Double,
                       @field:SerializedName("weight") val weight : Double,
                       @field:SerializedName("year") val year : Int,
                       @field:SerializedName("imgThumbUrl") val imgThumbUrl : String,
                       @field:SerializedName("modelHighlights") val modelHighlights : String,
                       @field:SerializedName("imgBannerUrl") val imgBannerUrl : String,
                       @field:SerializedName("modelDetailHighlights") val modelDetailHighlights : List<String>,
                       @field:SerializedName("priceTitle") val priceTitle : String,
                       @field:SerializedName("priceDesc") val priceDesc : String,
                       @field:SerializedName("mainDesc") val mainDesc : String,
                       @field:SerializedName("licenses") val licenses : List<String>,
                       @field:SerializedName("licensesTitle") val licensesTitle : String,
                       @field:SerializedName("specsTitle") val specsTitle : String,
//                       @field:SerializedName("specsTable") val specsTable : List<List<String>>,
                       @field:SerializedName("relatedItems") val relatedItems : List<String>,
                       @field:SerializedName("relatedItemsUrl") val relatedItemsUrl : List<String>)