package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.domain.features.search.FilterUseCase
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


class FrontPageViewModel @Inject constructor(val buscaTuMotoRepository: BuscaTuMotoRepository,
    val filterUseCase: FilterUseCase): BaseViewModel(), SearchBrandsRecyclerAdapter.BrandItemClickListener {

    companion object {
        const val PAGE_START_INDEX = 0
    }

    //UI  TODO (Should be removed) and use observerForever also remove observers.
    lateinit var lifeCycleOwner: SearchFragment
    lateinit var screenNavigator: ScreenNavigator

    //Mutables
    val drawableLoadMutable = MutableLiveData<Boolean> ()
    val searchTextMutable = MutableLiveData<String> ()

    //Adapters
    val searchBrandsAdapter = SearchBrandsRecyclerAdapter(this)

    //Visibilities
    val loadingVisibility = MutableLiveData<Int>().apply {
        this.value = View.GONE
    }

    //Utils
    private lateinit var lastBrandSelected: String
    private lateinit var lastSearch: String

    //Error retry management
    public var errorModelMutable = MutableLiveData<RetryErrorModel>()
    private val retryErrorClickListener = View.OnClickListener {
        when (errorModelMutable.value?.requestType) {
            RetryErrorModel.FILTER_ERROR -> {
                navigateByFilter(lastBrandSelected)
            }
            RetryErrorModel.SEARCH_ERROR -> {
            }
        }
    }
    fun getErrorClickListener() : View.OnClickListener = retryErrorClickListener

    override fun onCleared() {
        super.onCleared()
    }

    fun clear() = onCleared()


    init {
        loadBrands()
        drawableLoadMutable.value = true
    }

    private fun loadBrands() {
        //Load brands locally (no remote request required) and change mutable to tell binded view to display it's data
        val context = BuscaTuMotoApplication.getInstance().baseContext
        val brandNamesTypedArray = context.resources.obtainTypedArray(R.array.filter_brand_names_array)
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.filter_brand_logos_array)

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

    /**
     * Gets motorcycles based on filter values and obtains them from Dao.
     * Dao source means that response is always ruled by DAO Single Source Of Truth (SSOT)
     * Api network call just updates a DAO but response is only retrieved from DAO.
     */
    fun navigateByFilter(brand: String) {

        viewModelScope.launch(Dispatchers.IO) {

            //filter response
//            val liveData = buscaTuMotoRepository.getMotosFilter(brand,
//                null, null, null,
//                null, null, null,
//                null, null,
//                null, null, null, null, PAGE_START_INDEX)
            val liveData = filterUseCase.getMotosFilterSource(brand,
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
                                errorModelMutable.value = RetryErrorModel(R.string.load_filter_error, RetryErrorModel.FILTER_ERROR)
                                liveData.removeObservers(lifeCycleOwner)
                            }
                        }
                        //Maybe const val is enough
                        //View Models it's ok to know UI constants
                    })
            }
        }
    }

    override fun onBrandItemClick(brand: String) {
        Timber.d("brand: $brand")
        lastBrandSelected = brand
        navigateByFilter(brand)
    }
}