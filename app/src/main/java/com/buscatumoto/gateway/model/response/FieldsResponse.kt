package com.buscatumoto.gateway.model.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FieldsResponse(

    @SerializedName("respuesta") val respuesta : String,
    @SerializedName("brandList") val brandList : List<String>,
    @SerializedName("bikeTypesList") val bikeTypesList : List<String>,
    @SerializedName("yearList") val yearList : List<Int>,
    @SerializedName("priceMinList") val priceMinList : List<Int>,
    @SerializedName("priceMaxList") val priceMaxList : List<Int>,
    @SerializedName("powerMinList") val powerMinList : List<Float>,
    @SerializedName("powerMaxList") val powerMaxList : List<Float>,
    @SerializedName("cilMinList") val cilMinList : List<Float>,
    @SerializedName("cilMaxList") val cilMaxList : List<Float>,
    @SerializedName("weightMinList") val weightMinList : List<Float>,
    @SerializedName("weightMaxList") val weightMaxList : List<Float>,
    @SerializedName("licenses") val licenses : List<String>
)

//    : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.createStringArrayList(),
//        parcel.createStringArrayList(),
//        parcel.createIntArray().asList(),
//        parcel.createIntArray().asList(),
//        parcel.createIntArray().asList(),
//        parcel.createFloatArray().asList(),
//        parcel.createFloatArray().asList(),
//        parcel.createFloatArray().asList(),
//        parcel.createFloatArray().asList(),
//        parcel.createFloatArray().asList(),
//        parcel.createFloatArray().asList(),
//        parcel.createFloatArray().asList(),
//        parcel.createFloatArray().asList(),
//        parcel.createFloatArray().asList(),
//        parcel.createStringArrayList()
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(respuesta)
//        parcel.writeStringList(brandList)
//        parcel.writeStringList(bikeTypesList)
//        parcel.writeStringList(licenses)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<FieldsResponse> {
//        override fun createFromParcel(parcel: Parcel): FieldsResponse {
//            return FieldsResponse(parcel)
//        }
//
//        override fun newArray(size: Int): Array<FieldsResponse?> {
//            return arrayOfNulls(size)
//        }
//    }
//}