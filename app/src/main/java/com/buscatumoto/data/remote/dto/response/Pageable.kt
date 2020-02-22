package com.buscatumoto.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class Pageable (

    @SerializedName("sort") val sort : Sort,
    @SerializedName("offset") val offset : Int,
    @SerializedName("pageNumber") val pageNumber : Int,
    @SerializedName("pageSize") val pageSize : Int,
    @SerializedName("unpaged") val unpaged : Boolean,
    @SerializedName("paged") val paged : Boolean
)