package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.buscatumoto.R
import com.buscatumoto.data.Result
import com.buscatumoto.data.local.entity.Fields

import com.buscatumoto.data.remote.dto.response.FieldsResponse
import com.buscatumoto.data.remote.dto.response.MotoResponseItemModel
import com.buscatumoto.data.remote.repositories.BuscaTuMotoRepository
import com.buscatumoto.domain.GetFieldsUseCase
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.buscatumoto.utils.global.Constants
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.Executors

import javax.inject.Inject

class SearchFormViewModel @Inject constructor(val searchRepository: BuscaTuMotoRepository, val getFieldsUseCase: GetFieldsUseCase): BaseViewModel() {

//    private lateinit var subscription: Disposable

    lateinit var lifecycleOwner: FilterFormDialogFragment
    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    private val errorMessage: MutableLiveData<Int> = MutableLiveData()

    val fields by lazy { }



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
    val brandsLiveData: LiveData<List<String>>
        get() = brandsMutableLiveData



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
                            brandsMutableLiveData.value = result.data?.brandList
                        }
                        Result.Status.LOADING -> {

                        }
                        Result.Status.ERROR ->{

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

    private fun processResult(result: Result<Fields>) {

        when (result.status) {
            Result.Status.SUCCESS -> {
                loadingVisibility.value = View.GONE
            }
            Result.Status.LOADING -> {
                loadingVisibility.value = View.VISIBLE
            }
            Result.Status.ERROR -> {
                loadingVisibility.value = View.GONE
            }
        }
    }

//    private fun onLoadFieldsSuccess(fieldsResponse: Fields?) {
//        brands.value =
//            (fieldsResponse?.brandList as ArrayList<String>).apply {
//                this.remove("")
//                this.add(0, "-Marca-") }
//        models.value = ArrayList<String>().apply {
//            this.remove("")
//            this.add(0, "-Elegir Marca-") }
//        bikeTypes.value = (fieldsResponse?.bikeTypesList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Tipo de moto-"
//            )
//        }
//        priceMinList.value = (fieldsResponse?.priceMinList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Precio desde-"
//            )
//        }
//        priceMaxList.value = (fieldsResponse?.priceMaxList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Precio hasta-"
//            )
//        }
//        powerMinList.value = (fieldsResponse?.powerMinList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Potencia desde-"
//            )
//        }
//        powerMaxList.value = (fieldsResponse?.powerMaxList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Potencia hasta-"
//            )
//        }
//        cilMinList.value = (fieldsResponse?.cilMinList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Cilindrada desde-"
//            )
//        }
//        cilMaxList.value = (fieldsResponse?.cilMaxList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Cilindrada hasta-"
//            )
//        }
//        weightMinList.value = (fieldsResponse?.weightMinList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Peso desde-"
//            )
//        }
//        weightMaxList.value = (fieldsResponse?.weightMaxList as ArrayList<String>).apply {
//            this.remove("")
//            this.add(
//                0,
//                "-Peso hasta-"
//            )
//        }
//        yearList.value =
//            (fieldsResponse?.yearList as ArrayList<String>).apply { this.add(0, "-Año-") }
//        licenses.value =
//            (fieldsResponse?.licenses as ArrayList<String>).apply { this.add(0, "-Permiso-") }
//    }

    private fun onLoadFieldsError(throwableError: Throwable?) {
        Log.e(Constants.MOTOTAG, "error is ${throwableError?.message}")
        errorMessage.value = R.string.load_fields_error
    }

    fun loadModelsByBrand(brand: String) {
//        subscription = buscaTuMotoService.getBikesByBrand(brand)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { onLoadFieldsStart() }
//            .doOnTerminate { onLoadFieldsFinish() }
//            .subscribe( { respose -> onLoadModelsSuccess(respose) }, { throwableError -> onLoadModelsError(throwableError)})
    }

    private fun onLoadModelsSuccess(respose: List<MotoResponseItemModel>) {

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
//        subscription.dispose()
//        observableToDispose.removeObserver { Log.d(Constants.MOTOTAG, "Observable disposed onCleared from SearchFromViewModel!") }
    }

    fun refreshData() {
        this.loadFields()
    }

}