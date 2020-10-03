package com.buscatumoto.data.remote.dto.response

import com.buscatumoto.data.local.entity.MotoEntity
import com.google.gson.annotations.SerializedName

data class MotoResponse (@SerializedName("content") val motos : List<MotoEntity>,
                         @SerializedName("pageable") val pageable : Pageable,
                         @SerializedName("last") val last : Boolean,
                         @SerializedName("totalPages") val totalPages : Int,
                         @SerializedName("totalElements") val totalElements : Int,
                         @SerializedName("size") val size : Int,
                         @SerializedName("number") val number : Int,
                         @SerializedName("numberOfElements") val numberOfElements : Int,
                         @SerializedName("first") val first : Boolean,
                         @SerializedName("sort") val sort : Sort,
                         @SerializedName("empty") val empty : Boolean)