package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class FilterViewModel @Inject constructor() : BaseViewModel() {

    val brandExpanded = MutableLiveData<Boolean> ()
    val bikeTypeExpanded = MutableLiveData<Boolean> ()

    init {
        brandExpanded.value = false
        bikeTypeExpanded.value = false
    }

    fun onBrandLayoutClick() {
        brandExpanded.value = brandExpanded.value?.not()
    }

    fun onBikeTypeLayoutClick() {
        bikeTypeExpanded.value = bikeTypeExpanded.value?.not()
    }


}