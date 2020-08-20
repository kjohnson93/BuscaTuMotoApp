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
    CatalogueItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private val motosLiveData: MutableLiveData<PagedListMotoEntity> = MutableLiveData()

    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var screenNavigator: ScreenNavigator

    private val appContext: Context = BuscaTuMotoApplication.getInstance().applicationContext

    val noResultVisibility = MutableLiveData<Int>()

    //reference to adapter

    private var lastPageRequested: Int? = null

    //scroll listener
    private val _loadMoreItemsEvent = MutableLiveData<Boolean> ()
    var loadMoreItemsEvent: LiveData<Boolean> = _loadMoreItemsEvent
    fun loadMoreItemsEvent() {
        loadMoreItems()
    }

    private val _isLastPageMutable = MutableLiveData<Boolean>()
    var isLastPageLiveData: LiveData<Boolean> = _isLastPageMutable

    private val _currentPageMutable = MutableLiveData<Int>()
    val currentPageLiveData = _currentPageMutable

    private val _pageLoadingMutable = MutableLiveData<Boolean>()
    val pageLoadingLiveData = _pageLoadingMutable

    private var errorMessage = MutableLiveData<String>()
    fun getErrorMessage() = errorMessage

    var retryClickListener = View.OnClickListener {
//        loadCatalogue(lastPageRequested)
    }

    var currentPage: Int = PAGE_START
    private var isLastPage = false
    private var isLoading = false
    var itemCount = 0

    var refreshingMutable = MutableLiveData<Boolean>()

    var layoutManager: LinearLayoutManager? = LinearLayoutManager(
        appContext,
        RecyclerView.VERTICAL,
        false
    )

    init {
        //Always loads first page of moto search/filter
//        loadCatalogue(0)
    }

    override fun onItemClick(id: String) {
        val extras = Bundle()
        extras.putString(MOTO_ID_KEY, id)
        screenNavigator.navigateToNext(CatalogueActivity.NAVIGATE_TO_DETAIL, extras)
    }

    override fun onRefresh() {
        currentPage = PAGE_START;
        isLastPage = false;
//        catalogueListAdapter.clear()
//        loadCatalogue(currentPage)
//        loadCatalogueUseCase.observeCatalogueData(currentPage)
    }

    fun loadMoreItems() {
//        isLoading = true
//        if (currentPage != 0 && currentPage >= TotalElementsObject.totalElements) {
//            _isLastPageMutable.value = true
//        }
        if (currentPage >= TotalElementsObject.totalPages) {
            _isLastPageMutable.value = true
        } else {
            currentPage++
            _currentPageMutable.value = currentPage
            viewModelScope.launch(Dispatchers.IO) {
                pageLoadingLiveData.postValue(true)
                loadCatalogueUseCase.requestCatalogueDatePage(currentPage)
            }
        }

    }

    fun isLastPage(): Boolean {
        return isLastPage
    }

    fun isLoading(): Boolean {
        return isLoading
    }

    fun getLinearLayoutManager(): LinearLayoutManager? {

        //TODO check if only create when layout manager is null
            layoutManager = LinearLayoutManager(
                appContext,
                RecyclerView.VERTICAL,
                false
            )
            return layoutManager
    }

    fun getScrollableListener(): RecyclerView.OnScrollListener? {

        var scrollListener: RecyclerView.OnScrollListener? = null

        layoutManager?.let {
            scrollListener = object: PaginationListener(it) {
                override fun loadMoreItems() {
                    this@CatalogueViewModel.loadMoreItems()
                }

                override fun isLastPage(): Boolean {
                    return this@CatalogueViewModel.isLastPage()
                }

                override fun isLoading(): Boolean {
                    return this@CatalogueViewModel.isLoading()
                }
            }

        } ?: run {
            var linearLayoutM = getLinearLayoutManager()

            linearLayoutM?.let {
                scrollListener = object: PaginationListener(it) {
                    override fun loadMoreItems() {
                        this@CatalogueViewModel.loadMoreItems()
                    }
                    override fun isLastPage(): Boolean {
                        return this@CatalogueViewModel.isLastPage()
                    }
                    override fun isLoading(): Boolean {
                        return this@CatalogueViewModel.isLoading()
                    }
                }
            }
        }
        return scrollListener
    }

//    val catalogueData = loadCatalogueUseCase.catalogue

    val catalogueDataIndex by lazy { loadCatalogueUseCase.observeCatalogueData(currentPage) }
}