package com.buscatumoto.data.remote.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buscatumoto.data.Result
import com.buscatumoto.data.remote.BaseDataSource
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BuscaTuMotoDataSource @Inject constructor(private val buscaTuMotoService: BuscaTuMotoService): BaseDataSource() {

//    networkCall: suspend () -> Result<A>,

//    suspend fun getFields(): Result<FieldsResponse>{
//        buscaTuMotoService.getFields().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe( { return }, { throwableError -> onLoadModelsError(throwableError)})
//
//    }
//

    suspend fun getFields() = getResult { buscaTuMotoService.getFields() }

}