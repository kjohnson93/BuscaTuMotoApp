package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buscatumoto.data.mapper.MotoEntityToMotoDetailUiMapper
import com.buscatumoto.domain.features.detail.LoadMotoDetailUseCase
import com.buscatumoto.ui.fragments.DetailContentFragment
import com.buscatumoto.ui.models.MotoDetailUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import javax.inject.Inject

class DetailContentViewModel @Inject constructor(private val loadMotoDetailUseCase: LoadMotoDetailUseCase) :
    ViewModel() {

    var id: String = ""

    val modelTitleLiveData = MutableLiveData<String>()
    val highLightsLiveData = MutableLiveData<String>()
    val priceTitleLiveData = MutableLiveData<String>()
    val priceDescLiveData = MutableLiveData<String>()
    val mainDescLiveData = MutableLiveData<String>()
    val licensesTitleLiveData = MutableLiveData<String>()
    val licensesLiveData = MutableLiveData<String>()

    init {
        loadMotoEntity()
    }

    private fun loadMotoEntity() {

        viewModelScope.launch(Dispatchers.IO) {
            val moto = loadMotoDetailUseCase.getMoto()
            val motoUi = MotoEntityToMotoDetailUiMapper.suspenMap(moto)

            withContext(Dispatchers.Main) {
                motoUi?.let {
                    bind(motoUi)
                }
            }
        }
    }

    fun bind(motoDetailUi: MotoDetailUi) {
        modelTitleLiveData.value = motoDetailUi.modelTitle
        highLightsLiveData.value = motoDetailUi.modelDetailHighlights
        priceTitleLiveData.value = motoDetailUi.priceTitle
        priceDescLiveData.value = motoDetailUi.priceDesc
        mainDescLiveData.value = motoDetailUi.mainDesc
        licensesTitleLiveData.value = motoDetailUi.licensesTitle
        licensesLiveData.value = motoDetailUi.licenses
    }
}
