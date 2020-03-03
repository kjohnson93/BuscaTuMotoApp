package com.buscatumoto.ui.viewmodels

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.domain.features.catalogue.GetModelImageUseCase
import com.buscatumoto.utils.ui.CatalogueItemClickListener
import javax.inject.Inject

class CatalogueItemViewModel @Inject constructor(): BaseViewModel() {

    lateinit var catalogueItemClickListener: CatalogueItemClickListener

    val modelTitleLiveData = MutableLiveData<String>()
    val modelImageLiveData = MutableLiveData<Drawable>()
    val modelHighlightsLiveData = MutableLiveData<String>()

    @Inject lateinit var getModelImageUseCase: GetModelImageUseCase

    fun bind(motoEntity: MotoEntity) {
        //Update observables here
        modelTitleLiveData.value = motoEntity.model

//        val imageDrawable = getModelImageUseCase.execute(motoEntity.imgThumbUrl)

//        modelImageLiveData.value = imageDrawable
        modelHighlightsLiveData.value = motoEntity.modelHighlights

    }

    /**
     * This should be called from databinding
     */
    fun clickListenerTap() {
        catalogueItemClickListener.onItemClick()
    }


}