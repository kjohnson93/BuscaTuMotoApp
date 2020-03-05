package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.domain.features.detail.LoadMotoDetailUseCase
import com.buscatumoto.ui.activities.MotoDetailActivity
import com.buscatumoto.ui.models.MotoDetailUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MotoDetailViewModel @Inject constructor(private val loadMotoDetailUseCase: LoadMotoDetailUseCase) : ViewModel() {
    lateinit var lifeCycleOwner: MotoDetailActivity

    var id: String = ""

    val modelTitleLiveData = MutableLiveData<String>()
    val highLightsLiveData = MutableLiveData<String>()
    val priceTitleLiveData = MutableLiveData<String>()
    val priceDescLiveData = MutableLiveData<String>()
    val mainDescLiveData = MutableLiveData<String>()
    val licensesTitleLiveData = MutableLiveData<String>()
    val licensesLiveData = MutableLiveData<String>()
    val specsTableTitleLiveData = MutableLiveData<String>()


    fun loadMotoDetail(id: String) {
        Timber.d("Id on VM: $id")
        viewModelScope.launch(Dispatchers.IO) {
            val motoEntity = loadMotoDetailUseCase.execute(id)

            withContext(Dispatchers.Main) {
                motoEntity.observe(lifeCycleOwner, Observer {
                    result ->

                    viewModelScope.launch (Dispatchers.IO) {
                        val motoDetailUi: MotoDetailUi? = loadMotoDetailUseCase.parseMotoEntity(result)

                        withContext(Dispatchers.Main) {
                            modelTitleLiveData.value = motoDetailUi?.modelTitle
                            highLightsLiveData.value = motoDetailUi?.modelDetailHighlights
                        }
                    }



//                    modelTitleLiveData.value = motoDetailUi.model
//                    highLightsLiveData.value = motoDetailUi.modelDetailHighlights
//                    priceTitleLiveData.value = motoDetailUi.priceTitle
//                    priceDescLiveData.value =  motoDetailUi.priceDesc
//                    mainDescLiveData.value = motoDetailUi.mainDesc
//                    licensesTitleLiveData.value = motoDetailUi.licensesTitle
//                    licensesLiveData.value = motoDetailUi.licenses
//                    specsTableTitleLiveData.value = motoDetailUi.specsTitle
                })
            }
        }
    }
}