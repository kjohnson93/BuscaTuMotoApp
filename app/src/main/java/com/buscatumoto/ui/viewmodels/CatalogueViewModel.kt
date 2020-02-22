package com.buscatumoto.ui.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.BuscaTuMotoService
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.remote.datasource.BuscaTuMotoDataSource
import com.buscatumoto.domain.features.catalogue.LoadCatalogueUseCase
import com.buscatumoto.ui.activities.CatalogueActivity
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.buscatumoto.ui.models.MotoUI
import com.buscatumoto.utils.global.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CatalogueViewModel @Inject constructor(private val loadCatalogueUseCase: LoadCatalogueUseCase): BaseViewModel()  {

    private val motosLiveData: MutableLiveData<List<MotoEntity>> = MutableLiveData()

    lateinit var lifecycleOwner: CatalogueActivity


    private var lastPageRequested: Int? = null

    private var errorMessage = MutableLiveData<String>()
    fun getErrorMessage() = errorMessage

    var retryClickListener = View.OnClickListener {
        loadCatalogue(lastPageRequested)
    }

    init {
        //Always loads first page of moto search/filter
        loadCatalogue(0)
    }

    private fun loadCatalogue(pageIndex: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            val motos = loadCatalogueUseCase.execute()

            withContext(Dispatchers.Main) {
                motos.observe(lifecycleOwner, Observer { result ->
                    when(result.status) {
                        Result.Status.SUCCESS -> {
                            Timber.d("Data: ${result.data}")
                            motosLiveData.value = result.data
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




}