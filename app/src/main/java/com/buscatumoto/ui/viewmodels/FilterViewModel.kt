package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.ui.adapters.FilterRecyclerAdapter
import com.buscatumoto.ui.adapters.TestRecyclerItemData
import javax.inject.Inject

class FilterViewModel @Inject constructor() : BaseViewModel() {

    val brandExpanded = MutableLiveData<Boolean> ()
    val bikeTypeExpanded = MutableLiveData<Boolean> ()

    val itemClick = MutableLiveData<Boolean> ()
    val filterRecyclerAdapter = FilterRecyclerAdapter()

    init {
        brandExpanded.value = false
        bikeTypeExpanded.value = false

        itemClick.value = false

        //test
        loadRecyclerValues()
    }

    private fun loadRecyclerValues() {
        val list = ArrayList<TestRecyclerItemData>()
        val res1 = ContextCompat.getDrawable(BuscaTuMotoApplication.getInstance().applicationContext,
        R.drawable.montesa150x150)
        val res2 = ContextCompat.getDrawable(BuscaTuMotoApplication.getInstance().applicationContext,
            R.drawable.honda150x150)
        list.add(TestRecyclerItemData("Montesa", res1))
        list.add(TestRecyclerItemData("Montesa", res2))

        filterRecyclerAdapter.updateFilterItemsList(list)
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