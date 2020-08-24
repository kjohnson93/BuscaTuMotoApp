package com.buscatumoto.ui.viewmodels

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.domain.features.catalogue.GetModelImageUseCase
import com.buscatumoto.utils.global.PRICE_UNKNOWN
import com.buscatumoto.utils.ui.CatalogueItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatalogueItemViewModel @Inject constructor(private val getModelImageUseCase: GetModelImageUseCase): BaseViewModel() {

    val modelTitleLiveData = MutableLiveData<String>()
    val modelImageLiveData = MutableLiveData<Drawable>()
    val modelDisplacementMutable = MutableLiveData<Double>()
    val modelWeightMutable = MutableLiveData<Double>()
    val modelPowerMutable = MutableLiveData<Int>()
    val modelPriceMutable = MutableLiveData<Int>()

    lateinit var modelId: String


    fun bind(motoEntity: MotoEntity) {
        modelTitleLiveData.value = motoEntity.model
        modelId = motoEntity.id
        modelDisplacementMutable.value = motoEntity.displacement
        modelWeightMutable.value = motoEntity.weight
        modelPowerMutable.value = motoEntity.power
        modelPriceMutable.value = motoEntity.price

        viewModelScope.launch(Dispatchers.IO) {

            val imageDrawable = getModelImageUseCase.execute(motoEntity.imgThumbUrl)

            withContext(Dispatchers.Main) {
                modelImageLiveData.value = imageDrawable
            }
        }
    }
}