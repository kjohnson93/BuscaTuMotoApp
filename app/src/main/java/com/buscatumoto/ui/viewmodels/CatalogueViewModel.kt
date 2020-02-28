package com.buscatumoto.ui.viewmodels

import android.content.Context
import android.content.IntentFilter
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.domain.features.catalogue.LoadCatalogueUseCase
import com.buscatumoto.ui.activities.CatalogueActivity
import com.buscatumoto.ui.adapters.CatalogueListAdapter
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

    private val motosLiveData: MutableLiveData<List<MotoEntity>> = MutableLiveData()

    lateinit var lifecycleOwner: CatalogueActivity

    private val appContext: Context = BuscaTuMotoApplication.getInstance().applicationContext

    val loadingVisibility = MutableLiveData<Int>()
    val noResultVisibility = MutableLiveData<Int>()

    //reference to adapter
    var catalogueListAdapter = CatalogueListAdapter(this)

    private var lastPageRequested: Int? = null

    private var errorMessage = MutableLiveData<String>()
    fun getErrorMessage() = errorMessage

    var retryClickListener = View.OnClickListener {
        loadCatalogue(lastPageRequested)
    }

    private var currentPage: Int = PAGE_START
    private var isLastPage = false
    private var isLoading = false
    var itemCount = 0

    var refreshingMutable = MutableLiveData<Boolean>()

    val layoutManager = LinearLayoutManager(
        appContext,
        RecyclerView.VERTICAL,
        false
    )

    val scrollListener = object: PaginationListener(layoutManager) {
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

    init {
        //Always loads first page of moto search/filter
        loadCatalogue(0)
    }

    private fun loadCatalogue(pageIndex: Int?) {
        Timber.d("pageIndex: $pageIndex")
        viewModelScope.launch(Dispatchers.IO) {
            val motos = loadCatalogueUseCase.execute(pageIndex)

            withContext(Dispatchers.Main) {
                motos.observe(lifecycleOwner, Observer { result ->
                    when(result.status) {
                        Result.Status.SUCCESS -> {
                            Timber.d("Data: ${result.data}")
                            motosLiveData.value = result.data
                            result.data?.let {

                                noResultVisibility.value = View.GONE

                                /*
                                FIXME: Added workaround to display correctly empty result.
                                 Caching is lost.
                                 Fixing pagination from WS should recover caching.
                                 Because we can control empty results and empty pages
                                 */
                                if (it.isEmpty() && catalogueListAdapter.itemCount == 0) {
                                    noResultVisibility.value = View.VISIBLE
                                }

                                loadingVisibility.value = View.GONE

                                if (pageIndex != PAGE_START) {
                                    catalogueListAdapter.removeLoading()
                                }

                                catalogueListAdapter.addItems(result?.data)
                                refreshingMutable.value = false
                                isLoading = false
                            }
                            motos.removeObservers(lifecycleOwner)
                        }
                        Result.Status.LOADING -> {
                            noResultVisibility.value = View.GONE
                            if (pageIndex != PAGE_START) {
                                catalogueListAdapter.addLoading()
                            } else {
                                //show global loading
                                loadingVisibility.value = View.VISIBLE
                            }
                        }
                        Result.Status.ERROR -> {
                            noResultVisibility.value = View.GONE
                            loadingVisibility.value = View.GONE
                            errorMessage.value = result.message
                            motos.removeObservers(lifecycleOwner)
                        }
                    }
                })
            }
        }
    }

    override fun onItemClick() {
    }

    override fun onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        catalogueListAdapter.clear()
        loadCatalogue(currentPage)
    }

    fun loadMoreItems() {
        isLoading = true
        currentPage++
        loadCatalogue(currentPage)
    }

    fun isLastPage(): Boolean {
        return isLastPage
    }

    fun isLoading(): Boolean {
        return isLoading
    }




}