package com.buscatumoto.ui.viewmodels

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.domain.features.catalogue.GetModelImageUseCase
import com.buscatumoto.utils.global.PRICE_UNKNOWN
import com.buscatumoto.utils.ui.CatalogueItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatalogueItemViewModel @Inject constructor(private val getModelImageUseCase: GetModelImageUseCase): BaseViewModel() {

    lateinit var catalogueItemClickListener: CatalogueItemClickListener

    val modelTitleLiveData = MutableLiveData<String>()
    val modelImageLiveData = MutableLiveData<Drawable>()
    val modelDisplacementMutable = MutableLiveData<String>()
    val modelWeightMutable = MutableLiveData<String>()
    val modelPowerMutable = MutableLiveData<String>()
    val modelPriceMutable = MutableLiveData<String>()

    lateinit var modelId: String


    fun bind(motoEntity: MotoEntity) {
        modelTitleLiveData.value = motoEntity.model
        modelId = motoEntity.id
        modelDisplacementMutable.value = parseDisplacement(motoEntity.displacement)
        modelWeightMutable.value = parseWeight(motoEntity.weight)
        modelPowerMutable.value = parsePower(motoEntity.power)
        modelPriceMutable.value = parsePrice(motoEntity.price)


        viewModelScope.launch(Dispatchers.IO) {

            val imageDrawable = getModelImageUseCase.execute(motoEntity.imgThumbUrl)

            withContext(Dispatchers.Main) {
                modelImageLiveData.value = imageDrawable
            }
        }
    }

    private fun parseDisplacement(value: Double): String? {
        val string = BuscaTuMotoApplication.getInstance().resources.getString(R.string.highlight_displacement)
        return string.format(value.toString())
    }

    private fun parseWeight(value: Double): String? {
        val string = BuscaTuMotoApplication.getInstance().resources.getString(R.string.highlight_weight)
        return string.format(value.toString())
    }

    private fun parsePower(value: Int): String? {
        val string = BuscaTuMotoApplication.getInstance().resources.getString(R.string.highlight_power)
        return string.format(value.toString())
    }

    private fun parsePrice(value: Int): String? {
        return if (value == PRICE_UNKNOWN) {
            BuscaTuMotoApplication.getInstance().resources.getString(R.string.price_unknown)
        } else {
            val string = BuscaTuMotoApplication.getInstance().resources.getString(R.string.highlight_price)
            string.format(value.toString())
        }
    }

    /**
     * This should be called from databinding
     */
    fun clickListenerTap(id: String) {
        catalogueItemClickListener.onItemClick(id)
    }


}