package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.domain.features.catalogue.LoadCatalogueUseCase
import com.buscatumoto.ui.activities.CatalogueActivity
import com.buscatumoto.ui.adapters.CatalogueListAdapter
import com.buscatumoto.utils.ui.CatalogueItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CatalogueViewModel @Inject constructor(private val loadCatalogueUseCase: LoadCatalogueUseCase): BaseViewModel(),
    CatalogueItemClickListener{

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

    init {
        val context = BuscaTuMotoApplication.getInstance().applicationContext
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )

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
                                catalogueListAdapter.updateCatalogue(it)
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


}