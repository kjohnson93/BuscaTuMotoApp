package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.buscatumoto.ui.adapters.SearchBrandsRecyclerAdapter
import com.buscatumoto.ui.models.BrandRecyclerUiModel


class FrontPageBrandViewModel: BaseViewModel() {

    private val brandRecyclerObservable = MutableLiveData<BrandRecyclerUiModel>()

    lateinit var clickListener: SearchBrandsRecyclerAdapter.BrandItemClickListener

    fun getBrandObservable(): MutableLiveData<BrandRecyclerUiModel> = brandRecyclerObservable

    /**
     * Binds data and view using databinding
     */
    fun bind(brandRecyclerUiModel: BrandRecyclerUiModel) {
        brandRecyclerObservable.value = brandRecyclerUiModel
    }

    /**
     * Called from databinding
     */
    fun brandImageClick(brand: String) {
        clickListener.onBrandItemClick(brand)
    }


}