package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.R
import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result

import com.buscatumoto.domain.features.search.GetFieldsUseCase
import com.buscatumoto.domain.features.search.GetModelsUseCase
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.utils.ui.RetryErrorModel
import kotlinx.coroutines.*
import timber.log.Timber

import javax.inject.Inject

class SearchFormViewModel @Inject constructor(
    private val getFieldsUseCase: GetFieldsUseCase, private val getModelsUseCase: GetModelsUseCase
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
                            onLoadFieldsStart()
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

    private fun onLoadFieldsStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onLoadFieldsError(message: String?) {
        loadingVisibility.value = View.GONE
        retryErrorModel = RetryErrorModel(R.string.load_fields_error, RetryErrorModel.FIELDS_ERROR)
        errorModel.value = retryErrorModel
    }

    private fun onLoadModelsSuccess(data: List<MotoEntity>?) {
        loadingVisibility.value = View.GONE
        models.value = getModelsUseCase.setupModels(data)
    }

    private fun onLoadModelsError(message: String?) {
        loadingVisibility.value = View.GONE
        retryErrorModel = RetryErrorModel(R.string.load_models_error, RetryErrorModel.MODELS_ERROR)
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
                            onLoadFieldsStart()
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
        val brand = brandsMutableLiveData.value?.get(position)
                if (brand.equals("-Marca-")) {
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

}