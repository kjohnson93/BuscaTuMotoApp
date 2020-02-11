package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.buscatumoto.data.remote.api.BuscaTuMotoService
import com.buscatumoto.ui.models.BrandRecyclerUiModel
import javax.inject.Inject

class FrontPageBrandViewModel: BaseViewModel() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

//    private val drawableObservable = MutableLiveData<Drawable>()
    private val brandRecyclerObservable = MutableLiveData<BrandRecyclerUiModel>()

    fun getBrandObservable(): MutableLiveData<BrandRecyclerUiModel> = brandRecyclerObservable

    fun bind(brandRecyclerUiModel: BrandRecyclerUiModel) {
        brandRecyclerObservable.value = brandRecyclerUiModel
    }
}