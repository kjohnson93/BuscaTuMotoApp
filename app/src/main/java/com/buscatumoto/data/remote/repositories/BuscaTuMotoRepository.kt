package com.buscatumoto.data.remote.repositories

import android.util.Log
import com.buscatumoto.data.local.SearchDao
import com.buscatumoto.data.remote.datasource.BuscaTuMotoDataSource
import com.buscatumoto.utils.global.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuscaTuMotoRepository @Inject constructor(private val buscaTuMotoDataSource: BuscaTuMotoDataSource, val searchDao: SearchDao) {

    init {
        Log.d(Constants.MOTOTAG, "Injection")
    }

//    fun getFields(): LiveData {
//                    buscaTuMotoService.getFields().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { onLoadFieldsStart() }
//            .doOnTerminate { onLoadFieldsFinish() }
//            .subscribe({ fieldResponse: FieldsResponse? -> onLoadFieldsSuccess(fieldResponse) }
//                , { throwableError: Throwable? -> onLoadFieldsError(throwableError) })
//    }





}