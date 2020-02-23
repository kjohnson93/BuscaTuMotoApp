package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.utils.ui.CatalogueItemClickListener
import javax.inject.Inject

class CatalogueItemViewModel @Inject constructor(): BaseViewModel() {

    val modelTitleLiveData = MutableLiveData<String>()
    lateinit var catalogueItemClickListener: CatalogueItemClickListener

    fun bind(motoEntity: MotoEntity) {
        //Update observables here
        modelTitleLiveData.value = motoEntity.model
    }

    /**
     * This should be called from databinding
     */
    fun clickListenerTap() {
        catalogueItemClickListener.onItemClick()
    }


}