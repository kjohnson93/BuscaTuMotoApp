package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.dto.response.MotoResponse
import com.buscatumoto.domain.features.detail.LoadMotoDetailUseCase
import com.buscatumoto.domain.features.detail.LoadRelatedMotosUseCase
import com.buscatumoto.ui.fragments.DetailRelatedFragment
import com.buscatumoto.utils.global.PAGE_START
import com.buscatumoto.utils.ui.PaginationListener
import com.buscatumoto.utils.ui.RelatedItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DetailRelatedViewModel @Inject constructor(val loadRelatedMotosUseCase:
                                                 LoadRelatedMotosUseCase,
                                                 private val loadMotoDetailUseCase: LoadMotoDetailUseCase) : ViewModel(),
 RelatedItemClickListener {

    //Visibilities
    val noResultVisibility = MutableLiveData<Int>()

    //Utils
    var currentPage: Int = PaginationListener.PAGE_START
    var lastPageRequested = currentPage
    var lastMotoId = ""

    //Mutables
    private val _isLastPageMutable = MutableLiveData<Boolean>()
    var isLastPageLiveData: LiveData<Boolean> = _isLastPageMutable
    var relatedMotosData = MutableLiveData<Result<MotoResponse>>()
    private val _currentPageMutable = MutableLiveData<Int>()
    val currentPageLiveData = _currentPageMutable
    private val _navigateMutable = MutableLiveData<Boolean>()
    val navigateLiveData: LiveData<Boolean> = _navigateMutable
    private val motoSelectedMutable = MutableLiveData<MotoEntity>()
    val motoSelectedLiveData: LiveData<MotoEntity> = motoSelectedMutable

    //Error management
    private var errorMessage = MutableLiveData<String>()
    fun getErrorMessage() = errorMessage
    var retryClickListener = View.OnClickListener {
        loadRelatedMotos(lastPageRequested)
    }

    init {
        loadRelatedMotos(PAGE_START)
    }

    private fun loadRelatedMotos(pageIndex: Int) {
        lastPageRequested = pageIndex
        relatedMotosData.value = Result.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            //Get id
            val moto = loadMotoDetailUseCase.getMoto()
            val id = moto.id

            val result = loadRelatedMotosUseCase.
            getMotosRelated(id, pageIndex)

            withContext(Dispatchers.Main) {
                relatedMotosData.value = result
            }
        }
    }

    fun onRefresh() {
        loadRelatedMotos(PAGE_START)
    }

    /**
     * Loads next page including next 20 items
     */
    fun loadMoreItems() {
        currentPage++
        lastPageRequested = currentPage
        _currentPageMutable.value = currentPage
        relatedMotosData.value = Result.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            val motoResponse = loadRelatedMotosUseCase.getMotosRelated(
                lastMotoId, currentPage)
            withContext(Dispatchers.Main) {
                motoResponse.data?.totalPages?.let {
                    if (motoResponse.data.number >= it - 1) {
                        _isLastPageMutable.value = true
                    }
                }
                relatedMotosData.value = motoResponse
            }
        }
    }

    override fun onItemClick(id: String) {
        _navigateMutable.value = true
    }
}