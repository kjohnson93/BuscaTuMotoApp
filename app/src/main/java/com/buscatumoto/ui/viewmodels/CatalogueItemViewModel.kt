package com.buscatumoto.ui.viewmodels

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.domain.features.catalogue.GetModelImageUseCase
import com.buscatumoto.utils.ui.CatalogueItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatalogueItemViewModel @Inject constructor(private val getModelImageUseCase: GetModelImageUseCase): BaseViewModel() {

    lateinit var catalogueItemClickListener: CatalogueItemClickListener

    val modelTitleLiveData = MutableLiveData<String>()
    val modelImageLiveData = MutableLiveData<Drawable>()
    val modelHighlightsLiveData = MutableLiveData<String>()


    fun bind(motoEntity: MotoEntity) {
        modelTitleLiveData.value = motoEntity.model
        modelHighlightsLiveData.value = motoEntity.modelHighlights

        viewModelScope.launch(Dispatchers.IO) {

            val imageDrawable = getModelImageUseCase.execute(motoEntity.imgThumbUrl)

            withContext(Dispatchers.Main) {
                modelImageLiveData.value = imageDrawable
            }
        }
    }

    /**
     * This should be called from databinding
     */
    fun clickListenerTap() {
        catalogueItemClickListener.onItemClick()
    }


}