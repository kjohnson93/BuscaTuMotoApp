package com.buscatumoto.ui.viewmodels

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.dto.response.MotoResponse
import com.buscatumoto.data.remote.dto.response.PagedListMotoEntity
import com.buscatumoto.domain.features.catalogue.LoadCatalogueUseCase
import com.buscatumoto.ui.activities.CatalogueActivity
import com.buscatumoto.ui.adapters.CatalogueListAdapter
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.utils.data.TotalElementsObject
import com.buscatumoto.utils.global.MOTO_ID_KEY
import com.buscatumoto.utils.ui.CatalogueItemClickListener
import com.buscatumoto.utils.ui.PaginationListener
import com.buscatumoto.utils.ui.PaginationListener.Companion.PAGE_START
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class CatalogueViewModel @Inject constructor(private val loadCatalogueUseCase: LoadCatalogueUseCase): ViewModel(),
    CatalogueItemClickListener {

    //Visibilities
    val noResultVisibility = MutableLiveData<Int>()

    //Mutables
    private val _loadMoreItemsEvent = MutableLiveData<Boolean> ()
    var loadMoreItemsEvent: LiveData<Boolean> = _loadMoreItemsEvent
    private val _isLastPageMutable = MutableLiveData<Boolean>()
    var isLastPageLiveData: LiveData<Boolean> = _isLastPageMutable
    private val _currentPageMutable = MutableLiveData<Int>()
    val currentPageLiveData = _currentPageMutable
    private val _pageLoadingMutable = MutableLiveData<Boolean>()
    val pageLoadingLiveData = _pageLoadingMutable
    val catalogueData = MutableLiveData<Result<MotoResponse>>()
    var refreshingMutable = MutableLiveData<Boolean>()

    //Error management
    private var errorMessage = MutableLiveData<String>()
    fun getErrorMessage() = errorMessage
    var retryClickListener = View.OnClickListener {
//        loadCatalogue(lastPageRequested)
    }

    //Utils
    var currentPage: Int = PAGE_START

    //To Remove
    lateinit var screenNavigator: ScreenNavigator
    private val appContext: Context = BuscaTuMotoApplication.getInstance().applicationContext

    init {
        loadCatalogue(PAGE_START)
    }

    /**
     * Loads next page including next 20 items
     */
    fun loadMoreItems() {
            currentPage++
            _currentPageMutable.value = currentPage
        catalogueData.value = Result.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
                val motoResponse = loadCatalogueUseCase.requestCatalogueDatePage(currentPage)
                withContext(Dispatchers.Main) {
                    motoResponse.data?.totalPages?.let {
                        if (motoResponse.data.number >= it - 1) {
                            _isLastPageMutable.value = true
                        }
                    }
                    catalogueData.value = motoResponse
                }
            }
    }

    override fun onItemClick(id: String) {
        val extras = Bundle()
        extras.putString(MOTO_ID_KEY, id)
        screenNavigator.navigateToNext(CatalogueActivity.NAVIGATE_TO_DETAIL, extras)
    }

    private fun loadCatalogue(pageIndex: Int) {
        catalogueData.value = Result.loading(null)
        Timber.d("pageIndex: $pageIndex")
        viewModelScope.launch(Dispatchers.IO) {
            val motoResponse = loadCatalogueUseCase.loadCatalogue(pageIndex)
            withContext(Dispatchers.Main) {
                catalogueData.value = motoResponse
            }
        }
    }
}