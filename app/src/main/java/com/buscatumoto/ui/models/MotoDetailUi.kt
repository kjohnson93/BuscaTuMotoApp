package com.buscatumoto.ui.models

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable


data class MotoDetailUi(
     var bannerImg: Drawable?,
     var modelTitle: String?,
     var modelDetailHighlights: String?,
     var priceTitle: String?,
     var priceDesc: String?,
     var mainDesc: String?,
     var licensesTitle: String?,
     var licenses: String?
     //list of
    //                       @field:SerializedName("specsTable") val specsTable : List<List<String>>
//     val relatedItems : List<String>,
): Parcelable {
     constructor(parcel: Parcel) : this(
          TODO("bannerImg"),
          parcel.readString(),
          parcel.readString(),
          parcel.readString(),
          parcel.readString(),
          parcel.readString(),
          parcel.readString(),
          parcel.readString()
     ) {
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
          parcel.writeString(modelTitle)
          parcel.writeString(modelDetailHighlights)
          parcel.writeString(priceTitle)
          parcel.writeString(priceDesc)
          parcel.writeString(mainDesc)
          parcel.writeString(licensesTitle)
          parcel.writeString(licenses)
     }

     override fun describeContents(): Int {
          return 0
     }

     companion object CREATOR : Parcelable.Creator<MotoDetailUi> {
          override fun createFromParcel(parcel: Parcel): MotoDetailUi {
               return MotoDetailUi(parcel)
          }

          override fun newArray(size: Int): Array<MotoDetailUi?> {
               return arrayOfNulls(size)
          }
     }
}