package com.buscatumoto.ui.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import android.widget.Toast
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchFormViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener {
        loadFields()
    }



    init {
        loadFields()
    }

    fun loadFields() {
        subscription = buscaTuMotoService.getFields().
            subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoadFieldsStart()}
            .doOnTerminate { onLoadFieldsFinish() }
            .subscribe({onLoadFieldsSuccess()}, {onLoadFieldsError()})
    }

    private fun onLoadFieldsStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onLoadFieldsFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onLoadFieldsSuccess() {

    }

    private fun onLoadFieldsError() {
        errorMessage.value = R.string.load_fields_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}