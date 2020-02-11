package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.R
import com.buscatumoto.data.remote.api.Result

import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.domain.features.search.GetFieldsUseCase
import com.buscatumoto.domain.features.search.GetModelsUseCase
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.buscatumoto.utils.global.Constants
import kotlinx.coroutines.*

import javax.inject.Inject

class SearchFormViewModel @Inject constructor(
    private val getFieldsUseCase: GetFieldsUseCase, private val getModelsUseCase: GetModelsUseCase
): BaseViewModel() {

    lateinit var lifecycleOwner: FilterFormDialogFragment
    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    private val errorMessage: MutableLiveData<Int> = MutableLiveData()


    fun getLoadingVisibility() = loadingVisibility
    fun getErrorMessage(): MutableLiveData<Int> = errorMessage

    private val errorClickListener = View.OnClickListener {
        loadFields()
    }

//    var fieldsResponse = MutableLiveData<FieldsResponse>()

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
            val fieldLiveData =  getFieldsUseCase.executeLiveData()

            //Observe on MainThread to update UI
            withContext(Dispatchers.Main) {
                fieldLiveData.observe(lifecycleOwner, Observer { result ->
                    when (result.status) {
                        Result.Status.SUCCESS -> {
                            onLoadFieldsFinish()
                            val fieldsLocalModified = getFieldsUseCase.setupFieldsData(result.data)
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
                        Result.Status.LOADING -> {
                            onLoadFieldsStart()
                        }
                        Result.Status.ERROR ->{
                            onLoadFieldsFinish()
                            onLoadFieldsError(result.message)
                        }
                    }
                })
            }
        }
    }

    private fun onLoadFieldsStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onLoadFieldsFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onLoadFieldsError(message: String?) {
        Log.e(Constants.MOTOTAG, "error is $message")
        errorMessage.value = R.string.load_fields_error
    }

    fun loadModelsByBrand(brand: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val motoModelsLiveData = getModelsUseCase.execute(brand)

            withContext(Dispatchers.Main) {
                motoModelsLiveData.observe(lifecycleOwner, Observer { result ->
                    when (result.status) {
                        Result.Status.SUCCESS -> {
                            onLoadFieldsFinish()
                            models.value = getModelsUseCase.setupModels(result.data)
                        }
                        Result.Status.LOADING -> {
                            onLoadFieldsStart()
                        }
                        Result.Status.ERROR -> {
                            onLoadFieldsFinish()
                        }
                    }
                })
            }
        }
    }

    fun onBrandSpinnerItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        Log.d(Constants.MOTOTAG, "Position clicked: $position")

        val brand = brandsMutableLiveData.value?.get(position)

                if (brand.equals("-Marca-")) {
                    return
                } else {
                    brand?.let {
                        loadModelsByBrand(it)
                    }
                }

    }

    private fun onLoadModelsSuccess(respose: List<MotoEntity>) {

        var brandModels = ArrayList<String>()

        respose.forEachIndexed { index, motoResponseItemModel ->
            brandModels.add(motoResponseItemModel.model)
        }

        brandModels.add(0, "-Elegir Marca-")

        models.value = brandModels
    }

    private fun onLoadModelsError(throwableError: Throwable) {
        Log.e(Constants.MOTOTAG, "error is ${throwableError?.message}")
        errorMessage.value = R.string.load_fields_error
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun refreshData() {
        this.loadFields()
    }

}