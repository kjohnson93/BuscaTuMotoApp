package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buscatumoto.data.mapper.MotoEntityToMotoDetailUiMapper
import com.buscatumoto.domain.features.detail.LoadMotoDetailUseCase
import com.buscatumoto.ui.fragments.DetailContentFragment
import com.buscatumoto.ui.models.MotoDetailUi
import com.buscatumoto.utils.global.*
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

    val displacementMutable = MutableLiveData<String>()
    val powerMutable = MutableLiveData<Int> ()
    val priceMutable = MutableLiveData<Int>()
    val weightMutable = MutableLiveData<String>()

    init {
        loadMotoEntity()
    }

    private fun loadMotoEntity() {

        viewModelScope.launch(Dispatchers.IO) {
            val moto = loadMotoDetailUseCase.getMoto()
            val motoUi = MotoEntityToMotoDetailUiMapper.suspenMap(moto)

            val displacement = moto.displacement
            val power = moto.power
            val price = moto.price
            val weight = moto.weight

            if (displacement == DISPLACEMENT_UNKNOWN) {
                displacementMutable.postValue(NOT_DETERMINED_VALUE)
            } else {
                displacementMutable.postValue(moto.displacement.toString().plus(" $DISPLACEMENT_MAGNITUDE"))
            }

            //Power and price neeed to get its magnitude translated
            powerMutable.postValue(moto.power)
            priceMutable.postValue(moto.price)

            if (weight == WEIGHT_UNKNOWN) {
                weightMutable.postValue(NOT_DETERMINED_VALUE)
            } else {
                weightMutable.postValue(moto.weight.toString().plus(" $WEIGHT_MAGNITUDE"))
            }

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
