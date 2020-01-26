package com.buscatumoto.ui.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.buscatumoto.R
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import com.buscatumoto.data.remote.dto.response.FieldsResponse
import com.buscatumoto.data.remote.dto.response.MotoResponseItemModel
import com.buscatumoto.utils.global.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchFormViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

    private lateinit var subscription: Disposable

    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    private val errorMessage: MutableLiveData<Int> = MutableLiveData()

    fun getLoadingVisibility() = loadingVisibility
    fun getErrorMessage(): MutableLiveData<Int> = errorMessage

    private val errorClickListener = View.OnClickListener {
        loadFields()
    }

    fun getErrorClickListener() : View.OnClickListener = errorClickListener

    val brands: MutableLiveData<List<String>> = MutableLiveData()
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

    init {
        loadFields()
    }

    fun loadFields() {
        subscription = buscaTuMotoService.getFields().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoadFieldsStart() }
            .doOnTerminate { onLoadFieldsFinish() }
            .subscribe({ fieldResponse: FieldsResponse? -> onLoadFieldsSuccess(fieldResponse) }
                , { throwableError: Throwable? -> onLoadFieldsError(throwableError) })
    }

    private fun onLoadFieldsStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onLoadFieldsFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onLoadFieldsSuccess(fieldsResponse: FieldsResponse?) {
        brands.value =
            (fieldsResponse?.brandList as ArrayList<String>).apply {
                this.remove("")
                this.add(0, "-Marca-") }
        models.value = ArrayList<String>().apply {
            this.remove("")
            this.add(0, "-Elegir Marca-") }
        bikeTypes.value = (fieldsResponse?.bikeTypesList as ArrayList<String>).apply {
            this.remove("")
            this.add(
                0,
                "-Tipo de moto-"
            )
        }
        priceMinList.value = (fieldsResponse?.priceMinList as ArrayList<String>).apply {
            this.remove("")
            this.add(
                0,
                "-Precio desde-"
            )
        }
        priceMaxList.value = (fieldsResponse?.priceMaxList as ArrayList<String>).apply {
            this.remove("")
            this.add(
                0,
                "-Precio hasta-"
            )
        }
        powerMinList.value = (fieldsResponse?.powerMinList as ArrayList<String>).apply {
            this.remove("")
            this.add(
                0,
                "-Potencia desde-"
            )
        }
        powerMaxList.value = (fieldsResponse?.powerMaxList as ArrayList<String>).apply {
            this.remove("")
            this.add(
                0,
                "-Potencia hasta-"
            )
        }
        cilMinList.value = (fieldsResponse?.cilMinList as ArrayList<String>).apply {
            this.remove("")
            this.add(
                0,
                "-Cilindrada desde-"
            )
        }
        cilMaxList.value = (fieldsResponse?.cilMaxList as ArrayList<String>).apply {
            this.remove("")
            this.add(
                0,
                "-Cilindrada hasta-"
            )
        }
        weightMinList.value = (fieldsResponse?.weightMinList as ArrayList<String>).apply {
            this.remove("")
            this.add(
                0,
                "-Peso desde-"
            )
        }
        weightMaxList.value = (fieldsResponse?.weightMaxList as ArrayList<String>).apply {
            this.remove("")
            this.add(
                0,
                "-Peso hasta-"
            )
        }
        yearList.value =
            (fieldsResponse?.yearList as ArrayList<String>).apply { this.add(0, "-Año-") }
        licenses.value =
            (fieldsResponse?.licenses as ArrayList<String>).apply { this.add(0, "-Permiso-") }
    }

    private fun onLoadFieldsError(throwableError: Throwable?) {
        Log.e(Constants.MOTOTAG, "error is ${throwableError?.message}")
        errorMessage.value = R.string.load_fields_error
    }

    fun loadModelsByBrand(brand: String) {
        subscription = buscaTuMotoService.getBikesByBrand(brand)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoadFieldsStart() }
            .doOnTerminate { onLoadFieldsFinish() }
            .subscribe( { respose -> onLoadModelsSuccess(respose) }, { throwableError -> onLoadModelsError(throwableError)})
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
        subscription.dispose()
    }

    fun refreshData() {
        this.loadFields()
    }

}