package com.buscatumoto.ui.viewmodels

import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.ui.adapters.FilterRecyclerAdapter
import com.buscatumoto.ui.adapters.FilterRecyclerItem
import javax.inject.Inject

class FilterViewModel @Inject constructor() : BaseViewModel(),
    FilterRecyclerAdapter.FilterItemClickListener {

    val brandExpanded = MutableLiveData<Boolean> ()
    val bikeTypeExpanded = MutableLiveData<Boolean> ()

    val itemClick = MutableLiveData<Boolean> ()
    var filterRecyclerAdapter = FilterRecyclerAdapter(this)

    init {
        brandExpanded.value = false
        bikeTypeExpanded.value = false

        itemClick.value = false

        //test
        loadRecyclerValues()
    }

    private fun loadRecyclerValues() {
        val list = ArrayList<FilterRecyclerItem>()
        val res1 = ContextCompat.getDrawable(BuscaTuMotoApplication.getInstance().applicationContext,
        R.drawable.montesa150x150)
        val res2 = ContextCompat.getDrawable(BuscaTuMotoApplication.getInstance().applicationContext,
            R.drawable.honda150x150)
        val res3 = ContextCompat.getDrawable(BuscaTuMotoApplication.getInstance().applicationContext,
            R.drawable.yamaha150x150)
        val res4 = ContextCompat.getDrawable(BuscaTuMotoApplication.getInstance().applicationContext,
            R.drawable.daelim150x150)
        list.add(FilterRecyclerItem("Montesa", res1))
        list.add(FilterRecyclerItem("Honda", res2))
        list.add(FilterRecyclerItem("Yamaha", res3))
        list.add(FilterRecyclerItem("Daelim", res4))

        filterRecyclerAdapter.updateFilterItemsList(list)
    }

    fun onBrandLayoutClick() {
        brandExpanded.value = brandExpanded.value?.not()
    }

    fun onBikeTypeLayoutClick() {
        bikeTypeExpanded.value = bikeTypeExpanded.value?.not()
    }

    override fun onClick(filterItem: FilterRecyclerItem, position: Int) {
        val isItemSelected = filterItem.isSelected

        if (isItemSelected) {
            filterItem.isSelected = false
        } else {
            filterRecyclerAdapter.filterItemsList.forEach {
                it.isSelected = false
            }
            filterRecyclerAdapter.filterItemsList[position].isSelected = true
        }

        filterRecyclerAdapter.notifyDataSetChanged()
    }


}