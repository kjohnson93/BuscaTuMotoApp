package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.buscatumoto.domain.features.search.FilterUseCase
import com.buscatumoto.utils.ui.RetryErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class FrontPageViewModel @Inject constructor(private val filterUseCase: FilterUseCase): BaseViewModel() {

    //Mutables
    val searchTextMutable = MutableLiveData<String> ()
    val navigateMutable = MutableLiveData<Boolean>()

    //Visibilities
    val loadingVisibility = MutableLiveData<Int>().apply {
        this.value = View.GONE
    }

    //Utils
    private lateinit var lastBrandSelected: String

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

    /**
     * Gets motorcycles based on filter values and obtains them from Dao.
     * Dao source means that response is always ruled by DAO Single Source Of Truth (SSOT)
     * Api network call just updates a DAO but response is only retrieved from DAO.
     */
    fun navigateByFilter(brand: String) {
        viewModelScope.launch(Dispatchers.IO) {
            filterUseCase.navigateByBrandFilter(brand)
            navigateMutable.postValue(true)
        }
    }

    /**
     * Handles click events on brand list
     * and stores it value in case of having to retry event.
     */
    fun onBrandItemClick(brand: String) {
        Timber.d("brand: $brand")
        lastBrandSelected = brand
        navigateByFilter(brand)
    }
}