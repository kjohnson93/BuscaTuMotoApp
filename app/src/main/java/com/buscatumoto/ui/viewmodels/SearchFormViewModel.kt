package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.domain.features.search.FilterUseCase

import com.buscatumoto.domain.features.search.GetFieldsUseCase
import com.buscatumoto.domain.features.search.GetModelsUseCase
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.utils.ui.RetryErrorModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

import javax.inject.Inject

class SearchFormViewModel @Inject constructor(
    private val getFieldsUseCase: GetFieldsUseCase, private val getModelsUseCase: GetModelsUseCase,
    private val filterUseCase: FilterUseCase
): BaseViewModel() {

    lateinit var lifecycleOwner: FilterFormDialogFragment
    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    private var errorModel = MutableLiveData<RetryErrorModel>()
    fun getError() = errorModel

    private lateinit var retryErrorModel: RetryErrorModel
    private lateinit var lastBrandSelected: String

    lateinit var screenNavigator: ScreenNavigator

    fun getLoadingVisibility() = loadingVisibility

    private val errorClickListener = View.OnClickListener {
        when (retryErrorModel.requestType) {
            RetryErrorModel.FIELDS_ERROR -> {
                loadFields()
            }
            RetryErrorModel.MODELS_ERROR -> {
                loadModelsByBrand(lastBrandSelected)
            }
        }
    }

    fun getErrorClickListener() : View.OnClickListener = errorClickListener

    val models: MutableLiveData<List<String>> = MutableLiveData()
    val bikeTypes: MutableLiveData<List<String>> = MutableLiveData()
    val priceMinList: MutableLiveData<List<String>> = MutableLiveData()
    val priceMaxList: MutableLiveData<List<String>> = MutableLiveData()
    val powerMinList: MutableLiveData<List<String>> = MutableLiveData()
    val powerMaxList: MutableLiveData<List<String>> = MutableLiveData()
    val cilMinList: MutableLiveData<List<String>> = MutableLiveData()
    val cilMaxList: MutableLiveData<List<String>> = MutableLiveData()
    val weightMinList: MutableLiveData<List<String>> = MutableLiveData()
    val weightMaxList: MutableLiveData<List<String>> = MutableLiveData()
    val yearList: MutableLiveData<List<String>> = MutableLiveData()
    val licenses: MutableLiveData<List<String>> = MutableLiveData()

    val brandsMutableLiveData: MutableLiveData<List<String>> = MutableLiveData()



    init {
        loadFields()
    }

    fun loadFields() {

        viewModelScope.launch(Dispatchers.IO) {

            //Get livedata on background
            val fieldLiveData =  getFieldsUseCase.execute()

            //Observe on MainThread to update UI
            withContext(Dispatchers.Main) {
                fieldLiveData.observe(lifecycleOwner, Observer { result ->
                    when (result.status) {
                        Result.Status.SUCCESS -> {
                            onLoadFieldsSuccess(result.data)
                            fieldLiveData.removeObservers(lifecycleOwner)
                        }
                        Result.Status.LOADING -> {
                            onLoadingNetworkRequest()
                        }
                        Result.Status.ERROR ->{
                            onLoadFieldsError(result.message)
                            fieldLiveData.removeObservers(lifecycleOwner)
                        }
                    }
                })
            }
        }
    }

    private fun onLoadFieldsSuccess(data: FieldsEntity?) {
        loadingVisibility.value = View.GONE
        val fieldsLocalModified = getFieldsUseCase.setupFieldsData(data)
        brandsMutableLiveData.value = fieldsLocalModified.brandList
        models.value = fieldsLocalModified.modelslist
        bikeTypes.value = fieldsLocalModified.bikeTypesList
        priceMinList.value = fieldsLocalModified.priceMinList
        priceMaxList.value = fieldsLocalModified.priceMaxList
        powerMinList.value = fieldsLocalModified.powerMinList
        powerMaxList.value = fieldsLocalModified.powerMaxList
        cilMinList.value = fieldsLocalModified.cilMinList
        cilMaxList.value = fieldsLocalModified.cilMaxList
        weightMinList.value = fieldsLocalModified.weightMinList
        weightMaxList.value = fieldsLocalModified.weightMaxList
        yearList.value = fieldsLocalModified.yearList
        licenses.value = fieldsLocalModified.licenses
    }

    private fun onLoadingNetworkRequest() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onLoadFieldsError(message: String?) {
        loadingVisibility.value = View.GONE
        retryErrorModel = RetryErrorModel(R.string.load_fields_error, RetryErrorModel.FIELDS_ERROR)
        errorModel.value = retryErrorModel
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

    private fun onFilterSuccess(data: List<MotoEntity>?){
        screenNavigator.navigateToNext(FilterFormDialogFragment.NAVIGATE_TO_CATALOGUE, null)
    }

    private fun onFilterError(message: String?) {
        retryErrorModel = RetryErrorModel(R.string.load_filter_error, RetryErrorModel.FILTER_ERROR)
        errorModel.value = retryErrorModel
    }



    fun loadModelsByBrand(brand: String) {
        lastBrandSelected = brand

        viewModelScope.launch(Dispatchers.IO) {
            val motoModelsLiveData = getModelsUseCase.execute(brand)

            withContext(Dispatchers.Main) {
                motoModelsLiveData.observe(lifecycleOwner, Observer { result ->
                    when (result.status) {
                        Result.Status.SUCCESS -> {
                            onLoadModelsSuccess(result.data)
                            motoModelsLiveData.removeObservers(lifecycleOwner)
                        }
                        Result.Status.LOADING -> {
                            onLoadingNetworkRequest()
                        }
                        Result.Status.ERROR -> {
                            onLoadModelsError(result.message)
                            motoModelsLiveData.removeObservers(lifecycleOwner)
                        }
                    }
                })
            }
        }
    }


    fun onBrandSpinnerItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Timber.d("Position clicked: $position")
        val context = BuscaTuMotoApplication.getInstance().applicationContext
        val brand = brandsMutableLiveData.value?.get(position)
                if (brand.equals(context.getString(R.string.elegir_marca))) {
                    return
                } else {
                    brand?.let {
                        loadModelsByBrand(it)
                    }
                }
    }

    fun refreshData() {
        this.loadFields()
    }

    fun filter(brand: String? = null,
               model: String? = null,
               bikeType: String? = null,
               priceBottom: String? = null,
               priceTop: String? = null,
               powerBottom: String? = null,
               powerTop: String? = null,
               displacementBottom: String? = null,
               displacementTop: String? = null,
               weightBottom: String? = null,
               weightTop: String? = null,
               year: String? = null,
               license: String? = null
    ) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val response = filterUseCase.execute(brand, model,
                    bikeType,
                    priceBottom,
                    priceTop,
                    powerBottom,
                    powerTop,
                    displacementBottom,
                    displacementTop,
                    weightBottom,
                    weightTop,
                    year,
                    license,
                    0)
                withContext(Dispatchers.Main) {

                    response.observe(lifecycleOwner, Observer {
                            result ->
                        when (result.status) {
                            Result.Status.SUCCESS -> {
                                onFilterSuccess(result.data)
                            }
                            Result.Status.LOADING -> {
                                onLoadingNetworkRequest()
                            }
                            Result.Status.ERROR -> {
                                onLoadFieldsError(result.message)
                            }
                        }
                    })

                }
            } catch (exception: Exception) {
                Timber.e("Something went wrong building filter form")
                retryErrorModel = RetryErrorModel(R.string.build_fields_form_error, RetryErrorModel.FILTER_ERROR)
                errorModel.value = retryErrorModel
            }

        }

    }
}