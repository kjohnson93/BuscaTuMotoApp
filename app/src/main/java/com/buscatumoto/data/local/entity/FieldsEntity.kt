package com.buscatumoto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "fields")
data class FieldsEntity(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    val id: Int?,
    @field:SerializedName("respuesta")
    val respuesta: String?,
    @field:SerializedName("modelsList")
    val models: List<String>?,
    @field:SerializedName("brandList")
    val brandList: List<String>? ,
    @field:SerializedName("bikeTypesList")
    val bikeTypesList: List<String>?,
    @field:SerializedName("priceMinList")
    val priceMinList: List<Int>?,
    @field:SerializedName("priceMaxList")
    val priceMaxList: List<Int>?,
    @field:SerializedName("powerMinList")
    val powerMinList: List<Float>?,
    @field:SerializedName("powerMaxList")
    val powerMaxList: List<Float>?,
    @field:SerializedName("cilMinList")
    val cilMinList: List<Float>?,
    @field:SerializedName("cilMaxList")
    val cilMaxList: List<Float>?,
    @field:SerializedName("weightMinList")
    val weightMinList: List<Float>?,
    @field:SerializedName("weightMaxList")
    val weightMaxList: List<Float>?,
    @field:SerializedName("yearList")
    val yearList: List<Int>?,
    @field:SerializedName("licenses")
    val licenses: List<String>?
) {

}

