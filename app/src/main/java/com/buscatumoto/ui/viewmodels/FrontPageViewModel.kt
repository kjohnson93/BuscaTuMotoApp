package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.ui.adapters.SearchBrandsRecyclerAdapter
import com.buscatumoto.ui.fragments.SearchFragment
import com.buscatumoto.ui.models.BrandRecyclerUiModel
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.utils.ui.RetryErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class FrontPageViewModel @Inject constructor(val buscaTuMotoRepository: BuscaTuMotoRepository): BaseViewModel(), SearchBrandsRecyclerAdapter.BrandItemClickListener {

    companion object {
        const val PAGE_START_INDEX = 0
    }

    lateinit var lifeCycleOwner: SearchFragment

    lateinit var screenNavigator: ScreenNavigator

    private var errorModel = MutableLiveData<RetryErrorModel>()
    fun getError() = errorModel

    private lateinit var lastBrandSelected: String
    private lateinit var lastSearch: String

    private lateinit var retryErrorModel: RetryErrorModel

    val searchBrandsAdapter = SearchBrandsRecyclerAdapter(this)

    val loadingVisibility = MutableLiveData<Int>().apply {
        this.value = View.GONE
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun clear() = onCleared()

    private val retryErrorClickListener = View.OnClickListener {

        when (retryErrorModel.requestType) {
            RetryErrorModel.FILTER_ERROR -> {
                navigateByFilter(lastBrandSelected)
            }
            RetryErrorModel.SEARCH_ERROR -> {
                navigateBySearch(lastSearch)
            }
        }
    }

    fun getErrorClickListener() : View.OnClickListener = retryErrorClickListener


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
                null, null, null, null, PAGE_START_INDEX)


            withContext(Dispatchers.Main) {
                //filter observer
                liveData.observe(lifeCycleOwner,
                    Observer { result ->

                        when (result.status) {
                            Result.Status.SUCCESS -> {
                                Timber.d("Filter Success")
                                loadingVisibility.value = View.GONE
                                screenNavigator.navigateToNext(SearchFragment.NAVIGATE_TO_CATALOGUE, null)
                                liveData.removeObservers(lifeCycleOwner)
                            }
                            Result.Status.LOADING -> {
                                Timber.d("Filter Loading")
                                loadingVisibility.value = View.VISIBLE
                            }
                            Result.Status.ERROR -> {
                                Timber.d("Filter error")
                                loadingVisibility.value = View.GONE
                                retryErrorModel = RetryErrorModel(R.string.load_filter_error, RetryErrorModel.FILTER_ERROR)
                                errorModel.value = retryErrorModel
                                liveData.removeObservers(lifeCycleOwner)
                            }
                        }
                        //Maybe const val is enough
                        //View Models it's ok to know UI constants
                    })
            }
        }
    }

    fun navigateBySearch(search: String) {
        viewModelScope.launch(Dispatchers.IO) {

            //search response
            val liveData = buscaTuMotoRepository.search(search, PAGE_START_INDEX)

            withContext(Dispatchers.Main) {
                liveData.observe(lifeCycleOwner, Observer {
                    result ->
                    when (result.status) {
                        Result.Status.SUCCESS -> {
                            loadingVisibility.value = View.GONE
                            screenNavigator.navigateToNext(SearchFragment.NAVIGATE_TO_CATALOGUE, null)
                            liveData.removeObservers(lifeCycleOwner)
                        }
                        Result.Status.LOADING -> {
                            Timber.d("Search V LOADING")
                            loadingVisibility.value = View.VISIBLE

                        }
                        Result.Status.ERROR -> {
                            Timber.d("Search VM Error")
                            loadingVisibility.value = View.GONE
                            retryErrorModel = RetryErrorModel(R.string.load_search_error, RetryErrorModel.SEARCH_ERROR)
                            errorModel.value = retryErrorModel
                            liveData.removeObservers(lifeCycleOwner)
                        }
                    }
                })
            }
        }
    }

    override fun onBrandItemClick(brand: String) {
        Timber.d("brand: $brand")
        lastBrandSelected = brand
        navigateByFilter(brand)
    }

    fun onSearchRequested(search: String) {
        Timber.d("Search requested: $search")
        lastSearch = search
        navigateBySearch(search)
    }




}