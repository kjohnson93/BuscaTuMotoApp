package com.buscatumoto.gateway.model.response

import com.google.gson.annotations.SerializedName

data class FieldsResponse (

    @SerializedName("respuesta") val respuesta : String,
    @SerializedName("brandList") val brandList : List<String>,
    @SerializedName("bikeTypesList") val bikeTypesList : List<String>,
    @SerializedName("yearList") val yearList : List<Int>,
    @SerializedName("priceMinList") val priceMinList : List<Int>,
    @SerializedName("priceMaxList") val priceMaxList : List<Int>,
    @SerializedName("powerMinList") val powerMinList : List<Int>,
    @SerializedName("powerMaxList") val powerMaxList : List<Int>,
    @SerializedName("cilMinList") val cilMinList : List<Int>,
    @SerializedName("cilMaxList") val cilMaxList : List<Int>,
    @SerializedName("weightMinList") val weightMinList : List<Int>,
    @SerializedName("weightMaxList") val weightMaxList : List<Int>,
    @SerializedName("licenses") val licenses : List<String>
)