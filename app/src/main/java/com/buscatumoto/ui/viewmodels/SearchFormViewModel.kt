package com.buscatumoto.ui.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import com.buscatumoto.injection.component.DaggerViewComponent
import com.buscatumoto.injection.component.DaggerViewModelComponent
import com.buscatumoto.injection.component.ViewComponent
import com.buscatumoto.injection.component.ViewModelComponent
import com.buscatumoto.injection.module.NetworkModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchFormViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()



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

    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}