package com.buscatumoto.ui.models

import android.graphics.drawable.Drawable


data class MotoDetailUi (
     var bannerImg: Drawable?,
     var modelTitle : String,
     var modelDetailHighlights : String,
     var priceTitle : String,
     var priceDesc : String,
     var mainDesc : String,
     var licensesTitle : String,
     var licenses : String,
     var specsTitle : String
    //                       @field:SerializedName("specsTable") val specsTable : List<List<String>>
//     val relatedItems : List<String>,
)