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

    val loadingVisibility = MutableLiveData<Int>()
    val noResultVisibility = MutableLiveData<Int>()

    //reference to adapter

    private var lastPageRequested: Int? = null

    //scroll listener
    private val _loadMoreItemsEvent = MutableLiveData<Boolean> ()
    var loadMoreItemsEvent: LiveData<Boolean> = _loadMoreItemsEvent
    fun loadMoreItemsEvent() {
        loadMoreItems()
    }

    val _isLastPageMutable = MutableLiveData<Boolean> ()
    var isLastPageLiveData: LiveData<Boolean> = _isLastPageMutable

    private var errorMessage = MutableLiveData<String>()
    fun getErrorMessage() = errorMessage

    var retryClickListener = View.OnClickListener {
//        loadCatalogue(lastPageRequested)
    }

    private var currentPage: Int = PAGE_START
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

    private fun loadCatalogue(pageIndex: Int?) {
        Timber.d("pageIndex: $pageIndex")
        viewModelScope.launch(Dispatchers.IO) {
            val motos = loadCatalogueUseCase.execute(pageIndex)

            //Initial no resu'lt visibility.
            noResultVisibility.postValue(View.GONE)

            withContext(Dispatchers.Main) {
                motos.observe(lifecycleOwner, Observer { result ->
                    when(result.status) {
                        Result.Status.SUCCESS -> {
                            noResultVisibility.value = View.GONE
                            Timber.d("Data: ${result.data}")
                            motosLiveData.value = result.data
                            result.data?.let {

                                noResultVisibility.value = View.GONE

//                                if (it.list.isEmpty() && catalogueListAdapter.itemCount == 0) {
//                                    noResultVisibility.value = View.VISIBLE
//                                }
//
//                                loadingVisibility.value = View.GONE
//
//                                if (pageIndex != PAGE_START) {
//                                    catalogueListAdapter.removeLoading()
//                                }

//                                catalogueListAdapter.addItems(result?.data.list)
                                refreshingMutable.value = false
                                isLoading = false

                                layoutManager = null
                            }
                            motos.removeObservers(lifecycleOwner)
                        }
                        Result.Status.LOADING -> {
                            if (pageIndex != PAGE_START) {
//                                catalogueListAdapter.addLoading()
                            } else {
                                //show global loading
                                loadingVisibility.value = View.VISIBLE
                            }
                        }
                        Result.Status.ERROR -> {
                            loadingVisibility.value = View.GONE
                            errorMessage.value = result.message
                            motos.removeObservers(lifecycleOwner)
                            layoutManager = null
                        }
                    }
                })
            }
        }
    }

    override fun onItemClick(id: String) {
        val extras = Bundle()
        extras.putString(MOTO_ID_KEY, id)
        screenNavigator.navigateToNext(CatalogueActivity.NAVIGATE_TO_DETAIL, extras)
    }

    override fun onRefresh() {
        itemCount = 0;
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
            viewModelScope.launch(Dispatchers.IO) {
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