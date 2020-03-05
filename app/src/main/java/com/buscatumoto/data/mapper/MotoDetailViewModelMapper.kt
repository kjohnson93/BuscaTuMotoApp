package com.buscatumoto.data.mapper

import android.graphics.drawable.Drawable
import coil.Coil
import coil.api.get
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.ui.models.MotoDetailUi
import java.lang.StringBuilder

object MotoEntityToMotoDetailUiMapper: BaseSuspendMapper<MotoEntity, MotoDetailUi?> {

    override suspend fun suspenMap(type: MotoEntity?): MotoDetailUi? {

        var bannerDrawable: Drawable? = null

        var result: MotoDetailUi? = null

        type?.let {
            //Banner imagr
            it.imgBannerUrl?.let {
                bannerDrawable = Coil.get(it)
            }

            //Highlights
            val highlightsStrBuilder = StringBuilder()
            for (modelDetailHighlight in it.modelDetailHighlights) {
                highlightsStrBuilder.append(modelDetailHighlight)
            }
            val modelDetailHighLights = highlightsStrBuilder.toString()

            //licenses
            val licensesStrBuilder = StringBuilder()
            for (license in it.licenses) {
                licensesStrBuilder.append(license)
            }
            val licenses = licensesStrBuilder.toString()


            result = MotoDetailUi(bannerDrawable, it.model, modelDetailHighLights, it.priceTitle,
                it.priceDesc, it.mainDesc, it.licensesTitle, licenses, it.specsTitle)
        }

        return result

    }
//    override suspend fun suspenMap(type: MotoEntity?): MotoDetailUi {
//
//        val bannerDrawable = Coil.get(type?.imgBannerUrl)
//
//        val result = MotoDetailUi()
//
//    }

}