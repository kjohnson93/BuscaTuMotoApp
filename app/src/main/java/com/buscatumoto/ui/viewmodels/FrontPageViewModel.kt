package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.remote.api.BuscaTuMotoService
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.ui.adapters.SearchBrandsRecyclerAdapter
import com.buscatumoto.ui.fragments.SearchFragment
import com.buscatumoto.ui.models.BrandRecyclerUiModel
import com.buscatumoto.ui.navigation.ScreenNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class FrontPageViewModel @Inject constructor(val buscaTuMotoRepository: BuscaTuMotoRepository): BaseViewModel(), SearchBrandsRecyclerAdapter.BrandItemClickListener {

    lateinit var lifeCycleOwner: SearchFragment

    lateinit var screenNavigator: ScreenNavigator

    private var brandSelected = MutableLiveData<String>()

    val searchBrandsAdapter = SearchBrandsRecyclerAdapter(this)

    init {
        loadBrands()
    }

    private fun loadBrands() {
        //Load brands locally (no remote request required) and change mutable to tell binded view to display it's data
        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)
        val brandNamesTypedArray = context.resources.obtainTypedArray(R.array.brand_names_array)

        val  brandRecyclerUiModelList = ArrayList<BrandRecyclerUiModel?>()

        var index = 0

        while (index < drawabletypedArray.length()) {
            val brandRecyclerUiModel = BrandRecyclerUiModel(brandNamesTypedArray.getString(index), drawabletypedArray.getDrawable(index))
            brandRecyclerUiModelList.add(brandRecyclerUiModel)
            index ++
        }

        val modifiedList: List<BrandRecyclerUiModel?> = listOf(brandRecyclerUiModelList.last()) + brandRecyclerUiModelList + listOf(brandRecyclerUiModelList.first())

        searchBrandsAdapter.updateBrandHighLights(modifiedList as List<BrandRecyclerUiModel>)
    }

    fun navigateByFilter(brand: String) {

        viewModelScope.launch(Dispatchers.IO) {

            //filter response
            val liveData = buscaTuMotoRepository.filter(brand,
                null, null, null,
                null, null, null,
                null, null,
                null, null, null, null)

            withContext(Dispatchers.Main) {
                //filter observer
                liveData.observe(lifeCycleOwner,
                    Observer { result ->

                        when (result.status) {
                            Result.Status.SUCCESS -> {
                                Timber.d("Filter Success")
                                screenNavigator.navigateToNext(SearchFragment.NAVIGATE_TO_CATALOGUE)
                            }
                            Result.Status.LOADING -> {
                                Timber.d("Filter Loading")
                            }
                            Result.Status.ERROR -> {
                                Timber.d("Filter error")
                            }
                        }
                        //Maybe const val is enough
                        //View Models it's ok to know UI constants
                    })

//        //search observer textview
//        this.getbrandSelected().observe(lifeCycleOwner,
//            Observer { search ->
//                screenNavigator.navigateToNext(SearchFragment.NAVIGATE_TO_CATALOGUE) })
            }
        }
    }

    fun navigateBySerch(search: String) {
        viewModelScope.launch(Dispatchers.IO) {

            //search response

            withContext(Dispatchers.Main) {
                //filter observer
                brandSelected.observe(lifeCycleOwner,
                    Observer { brand ->
                        //Maybe const val is enough
                        //View Models it's ok to know UI constants
                        screenNavigator.navigateToNext(SearchFragment.NAVIGATE_TO_CATALOGUE)
                    })

//        //search observer textview
//        this.getbrandSelected().observe(lifeCycleOwner,
//            Observer { search ->
//                screenNavigator.navigateToNext(SearchFragment.NAVIGATE_TO_CATALOGUE) })
            }
        }
    }

    override fun onBrandItemClick(brand: String) {
        Timber.d("brand: $brand")
        navigateByFilter(brand)
    }

    fun onSearchRequested(search: String) {
        Timber.d("Search requested: $search")
        navigateBySerch(search)
    }




}