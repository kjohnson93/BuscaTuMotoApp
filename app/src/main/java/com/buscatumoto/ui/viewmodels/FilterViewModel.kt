package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class FilterViewModel @Inject constructor() : BaseViewModel() {

    val brandExpanded = MutableLiveData<Boolean> ()
    val bikeTypeExpanded = MutableLiveData<Boolean> ()

    val itemClick = MutableLiveData<Boolean> ()

    init {
        brandExpanded.value = false
        bikeTypeExpanded.value = false

        itemClick.value = false
    }

    fun onBrandLayoutClick() {
        brandExpanded.value = brandExpanded.value?.not()
    }

    fun onBikeTypeLayoutClick() {
        bikeTypeExpanded.value = bikeTypeExpanded.value?.not()
    }

    fun onItemClicked() {
        itemClick.value = itemClick.value?.not()
    }


}