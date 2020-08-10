package com.buscatumoto.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.domain.features.search.GetFieldsUseCase
import com.buscatumoto.domain.features.search.GetModelsUseCase
import com.buscatumoto.ui.adapters.FilterRecyclerAdapter
import com.buscatumoto.ui.adapters.FilterRecyclerItem
import com.buscatumoto.ui.models.BrandRecyclerUiModel
import com.buscatumoto.utils.ui.RetryErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FilterViewModel @Inject constructor(private val getFieldsUseCase: GetFieldsUseCase
                                          , private val getModelsUseCase: GetModelsUseCase) : BaseViewModel(),
    FilterRecyclerAdapter.FilterItemClickListener {

    val brandExpanded = MutableLiveData<Boolean> ()
    val bikeTypeExpanded = MutableLiveData<Boolean> ()

    val loadingVisibility = MutableLiveData<Int> ()

    val itemClick = MutableLiveData<Boolean> ()
    var brandRecyclerAdapter = FilterRecyclerAdapter(this)
    private lateinit var retryErrorModel: RetryErrorModel
    private var errorModel = MutableLiveData<RetryErrorModel>()
    fun getError() = errorModel

    private lateinit var lastBrandSelected: String
    val models: MutableLiveData<List<String>> = MutableLiveData()


    private val errorClickListener = View.OnClickListener {
        when (retryErrorModel.requestType) {
            RetryErrorModel.FIELDS_ERROR -> {
                loadRecyclerValues()
            }
            RetryErrorModel.MODELS_ERROR -> {
                loadModelsByBrand(lastBrandSelected)
            }
        }
    }

    fun getErrorClickListener() : View.OnClickListener = errorClickListener

    init {
        brandExpanded.value = false
        bikeTypeExpanded.value = false
        itemClick.value = false

        loadRecyclerValues()
    }

    private fun loadRecyclerValues() {
        viewModelScope.launch(Dispatchers.IO) {
            //Get livedata on background
            val fieldLiveData =  getFieldsUseCase.execute()

            //Observe on MainThread to update UI
            withContext(Dispatchers.Main) {
                fieldLiveData.observeForever(Observer { result ->
                    when (result.status) {
                        Result.Status.SUCCESS -> {
//                            onLoadFieldsSuccess(result.data)
//                            brandRecyclerAdapter.updateFilterItemsList(result.data.)
                            loadBrandRecycler(result.data?.brandList)
                            fieldLiveData.removeObserver {
                                Timber.d("Observer removed")
                            }
                        }
                        Result.Status.LOADING -> {
                            onLoading()
                        }
                        Result.Status.ERROR ->{
                            onLoadFieldsError(result.message)
                            fieldLiveData.removeObserver {
                                Timber.d("Observer removed")
                            }
                        }
                    }
                })
            }
        }

    }

    private fun loadBrandRecycler(data: List<String>?) {
        val context = BuscaTuMotoApplication.getInstance().baseContext
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.brand_logos_array)

        val  brandFilterList = ArrayList<FilterRecyclerItem>()

        var index = 0

        while (index < drawabletypedArray.length()) {
            val filterRecyclerItem = FilterRecyclerItem(data?.get(index), drawabletypedArray.getDrawable(index))
            brandFilterList.add(filterRecyclerItem)
            index ++
        }

        brandRecyclerAdapter.updateFilterItemsList(brandFilterList.toList())
    }

    fun onBrandLayoutClick() {
        brandExpanded.value = brandExpanded.value?.not()
    }

    fun onBikeTypeLayoutClick() {
        bikeTypeExpanded.value = bikeTypeExpanded.value?.not()
    }

    override fun onClick(filterItem: FilterRecyclerItem, position: Int) {
        val isItemSelected = filterItem.isSelected

        if (isItemSelected) {
            filterItem.isSelected = false
        } else {
            brandRecyclerAdapter.filterItemsList.forEach {
                it.isSelected = false
            }
            brandRecyclerAdapter.filterItemsList[position].isSelected = true
        }

        brandRecyclerAdapter.notifyDataSetChanged()
    }

    fun onLoading() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onLoadFieldsError(message: String?) {
        loadingVisibility.value = View.GONE
        retryErrorModel = RetryErrorModel(R.string.load_fields_error, RetryErrorModel.FIELDS_ERROR)
        errorModel.value = retryErrorModel
    }

    fun loadModelsByBrand(brand: String) {
        lastBrandSelected = brand

        viewModelScope.launch(Dispatchers.IO) {
            val motoModelsLiveData = getModelsUseCase.execute(brand)

            withContext(Dispatchers.Main) {
                motoModelsLiveData.observeForever { result ->
                    when (result.status) {
                        Result.Status.SUCCESS -> {
                            onLoadModelsSuccess(result.data)
                            motoModelsLiveData.removeObserver {
                                Timber.d("Observer removed")
                            }
                        }
                        Result.Status.LOADING -> {
                            onLoading()
                        }
                        Result.Status.ERROR -> {
                            onLoadModelsError(result.message)
                            motoModelsLiveData.removeObserver {
                                Timber.d("Observer removed")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onLoadModelsSuccess(data: FieldsEntity?) {
        loadingVisibility.value = View.GONE
        models.value = data?.models
    }

    private fun onLoadModelsError(message: String?) {
        loadingVisibility.value = View.GONE
        retryErrorModel = RetryErrorModel(R.string.load_models_error, RetryErrorModel.MODELS_ERROR)
        errorModel.value = retryErrorModel
    }




}