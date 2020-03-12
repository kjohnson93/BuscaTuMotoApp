package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.buscatumoto.domain.features.detail.LoadMotoDetailUseCase
import com.buscatumoto.ui.fragments.DetailContentFragment
import com.buscatumoto.ui.models.MotoDetailUi

import javax.inject.Inject

class DetailContentViewModel @Inject constructor(private val loadMotoDetailUseCase: LoadMotoDetailUseCase) :
    ViewModel() {


    lateinit var lifeCycleOwner: DetailContentFragment

    var id: String = ""

    val modelTitleLiveData = MutableLiveData<String>()
    val highLightsLiveData = MutableLiveData<String>()
    val priceTitleLiveData = MutableLiveData<String>()
    val priceDescLiveData = MutableLiveData<String>()
    val mainDescLiveData = MutableLiveData<String>()
    val licensesTitleLiveData = MutableLiveData<String>()
    val licensesLiveData = MutableLiveData<String>()

    fun bind(motoDetailUi: MotoDetailUi) {
        modelTitleLiveData.value = motoDetailUi?.modelTitle
        highLightsLiveData.value = motoDetailUi?.modelDetailHighlights
        priceTitleLiveData.value = motoDetailUi?.priceTitle
        priceDescLiveData.value = motoDetailUi?.priceDesc
        mainDescLiveData.value = motoDetailUi?.mainDesc
        licensesTitleLiveData.value = motoDetailUi?.licensesTitle
        licensesLiveData.value = motoDetailUi?.licenses
    }
}
