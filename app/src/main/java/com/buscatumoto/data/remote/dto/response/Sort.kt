package com.buscatumoto.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class Sort (

    @SerializedName("unsorted") val unsorted : Boolean,
    @SerializedName("sorted") val sorted : Boolean,
    @SerializedName("empty") val empty : Boolean
)