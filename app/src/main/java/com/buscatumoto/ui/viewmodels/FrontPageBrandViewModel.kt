package com.buscatumoto.ui.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.graphics.drawable.Drawable
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
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