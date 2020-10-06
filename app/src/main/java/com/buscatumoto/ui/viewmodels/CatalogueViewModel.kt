package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.lifecycle.*
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.dto.response.MotoResponse
import com.buscatumoto.domain.features.catalogue.LoadCatalogueUseCase
import com.buscatumoto.utils.ui.CatalogueItemClickListener
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
    private val _isLastPageMutable = MutableLiveData<Boolean>()
    var isLastPageLiveData: LiveData<Boolean> = _isLastPageMutable
    private val _currentPageMutable = MutableLiveData<Int>()
    val currentPageLiveData = _currentPageMutable
    val catalogueData = MutableLiveData<Result<MotoResponse>>()
    private val _navigateMutable = MutableLiveData<Boolean>()
    val navigateLiveData: LiveData<Boolean> = _navigateMutable
    private val motoSelectedMutable = MutableLiveData<MotoEntity>()
    val motoSelectedLiveData: LiveData<MotoEntity> = motoSelectedMutable


    //Error management
    private var errorMessage = MutableLiveData<String>()
    fun getErrorMessage() = errorMessage
    var retryClickListener = View.OnClickListener {
        loadCatalogue(lastPageRequested)
    }

    //Utils
    var currentPage: Int = PAGE_START
    var lastPageRequested = currentPage

    init {
        //Only load when entering from first time.
            loadCatalogue(PAGE_START)
    }

    /**
     * Loads next page including next 20 items
     */
    fun loadMoreItems() {
            currentPage++
        lastPageRequested = currentPage
            _currentPageMutable.value = currentPage
        catalogueData.value = Result.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
                val locale = BuscaTuMotoApplication.getInstance().getDefaultLanguage()
                val motoResponse = loadCatalogueUseCase.
                getMotosCatalogue(locale.language, currentPage)
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

    /**
     * On item click we find and save that motoEntity to DAO.
     * So it can be recovered by MotoDetail Screen later on.
     */
    override fun onItemClick(id: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val motosList = catalogueData.value?.data?.motos
            val moto = motosList?.find { it.id == id }
            //Assigning 1 because dao is intended to only have a single record.
            moto?.internalId = 1
            loadCatalogueUseCase.saveMoto(moto)

            withContext(Dispatchers.Main) {
                motoSelectedMutable.value = moto
                _navigateMutable.value = true
            }
        }

    }

    private fun loadCatalogue(pageIndex: Int) {
        lastPageRequested = pageIndex
        catalogueData.value = Result.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            val locale = BuscaTuMotoApplication.getInstance().getDefaultLanguage()
            val motoResponse = loadCatalogueUseCase
                .getMotosCatalogue(locale.language, pageIndex)
            withContext(Dispatchers.Main) {
                catalogueData.value = motoResponse
            }
        }
    }

    fun onRefresh() {
        loadCatalogue(PAGE_START)
    }
}