package com.buscatumoto.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class MotoResponseItemModel (@SerializedName("id") val id : String,
                                  @SerializedName("bikeType") val bikeType : String,
                                  @SerializedName("brand") val brand : String,
                                  @SerializedName("model") val model : String,
                                  @SerializedName("price") val price : Int,
                                  @SerializedName("power") val power : Int,
                                  @SerializedName("displacement") val displacement : Float,
                                  @SerializedName("weight") val weight : Float,
                                  @SerializedName("year") val year : Int,
                                  @SerializedName("imgThumbUrl") val imgThumbUrl : String,
                                  @SerializedName("modelHighlights") val modelHighlights : String,
                                  @SerializedName("imgBannerUrl") val imgBannerUrl : String,
                                  @SerializedName("modelDetailHighlights") val modelDetailHighlights : List<String>,
                                  @SerializedName("priceTitle") val priceTitle : String,
                                  @SerializedName("priceDesc") val priceDesc : String,
                                  @SerializedName("mainDesc") val mainDesc : String,
                                  @SerializedName("licenses") val licenses : List<String>,
                                  @SerializedName("licensesTitle") val licensesTitle : String,
                                  @SerializedName("specsTitle") val specsTitle : String,
                                  @SerializedName("specsTable") val specsTable : List<List<String>>,
                                  @SerializedName("relatedItems") val relatedItems : List<String>,
                                  @SerializedName("relatedItemsUrl") val relatedItemsUrl : List<String>)