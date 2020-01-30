package com.buscatumoto.data.remote.repositories

import android.arch.lifecycle.LiveData
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import com.buscatumoto.data.remote.dto.response.FieldsResponse
import com.buscatumoto.injection.component.DaggerViewModelComponent
import com.buscatumoto.injection.component.ViewModelComponent
import com.buscatumoto.injection.module.NetworkModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuscaTuMotoRepository @Inject constructor(private val webService: BuscaTuMotoService) {

//    fun getFields(): LiveData {
//                    buscaTuMotoService.getFields().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { onLoadFieldsStart() }
//            .doOnTerminate { onLoadFieldsFinish() }
//            .subscribe({ fieldResponse: FieldsResponse? -> onLoadFieldsSuccess(fieldResponse) }
//                , { throwableError: Throwable? -> onLoadFieldsError(throwableError) })
//    }

    @Inject
    lateinit var buscaTuMotoService: BuscaTuMotoService

    private val injector: ViewModelComponent = DaggerViewModelComponent.builder().networkModule(
        NetworkModule
    ).build()

    init {
        injector.inject(this)
    }

}