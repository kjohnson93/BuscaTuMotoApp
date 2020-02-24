package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

    //reference to adapter
    var catalogueListAdapter = CatalogueListAdapter(this)

    private var lastPageRequested: Int? = null

    private var errorMessage = MutableLiveData<String>()
    fun getErrorMessage() = errorMessage

    var retryClickListener = View.OnClickListener {
        loadCatalogue(lastPageRequested)
    }

    private var currentPage: Int = PaginationListener.PAGE_START
    private var isLastPage = false
    private val totalPage = 10
    private var isLoading = false
    var itemCount = 0

    var refreshingMutable = MutableLiveData<Boolean>()

    init {
        //Always loads first page of moto search/filter
        loadCatalogue(0)
    }

    private fun loadCatalogue(pageIndex: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            val motos = loadCatalogueUseCase.execute(pageIndex)

            withContext(Dispatchers.Main) {
                motos.observe(lifecycleOwner, Observer { result ->
                    when(result.status) {
                        Result.Status.SUCCESS -> {
                            Timber.d("Data: ${result.data}")
                            motosLiveData.value = result.data
                            result.data?.let {

                                if (currentPage != PAGE_START) {
                                    catalogueListAdapter.removeLoading()
                                }

                                catalogueListAdapter.addItems(result?.data)
                                refreshingMutable.value = false

                                if (currentPage < totalPage) {
                                    catalogueListAdapter.addLoading()
                                } else {
                                    isLastPage = true
                                }

                                isLoading = false
                            }
                        }
                        Result.Status.LOADING -> {

                        }
                        Result.Status.ERROR -> {
                            errorMessage.value = result.message
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