package com.buscatumoto.data.remote.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.buscatumoto.data.local.SearchDao
import com.buscatumoto.data.local.dao.FieldsDao
import com.buscatumoto.data.remote.datasource.BuscaTuMotoDataSource
import com.buscatumoto.data.remote.dto.response.FieldsResponse
import com.buscatumoto.data.resultLiveData
import com.buscatumoto.utils.global.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuscaTuMotoRepository @Inject constructor(private val buscaTuMotoDataSource: BuscaTuMotoDataSource, private val fieldsDao: FieldsDao) {

    init {
        Log.d(Constants.MOTOTAG, "Injection")
    }

//    fun getFields(): LiveData<FieldsResponse> = buscaTuMotoDataSource.getFields()

//    fun getFields(): LiveData {
//                    buscaTuMotoService.getFields().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { onLoadFieldsStart() }
//            .doOnTerminate { onLoadFieldsFinish() }
//            .subscribe({ fieldResponse: FieldsResponse? -> onLoadFieldsSuccess(fieldResponse) }
//                , { throwableError: Throwable? -> onLoadFieldsError(throwableError) })
//    }

    //1. Only data source
    // if we follow the same implementation that executing services from viewmodel (as this was before)-> NO RECOMMENDED ->
    // -> AVOID OBSERVING/LIVEDATA BETWEEN VIEW MODEL AND OTHER SOURCES IT'S OK WITH UI))-> USE COUROTINES OR RXJAVA INSTEADA
    //Because this way we must observe data from datasources, or we can simplify to the point view model passes directly the livedata from the datasource
    //https://codelabs.developers.google.com/codelabs/kotlin-coroutines/#6 Example to it without coroutines
    //2. Data Source + DAO
    //In this case, coroutines comes to help with the combine use of both data sources (services and Daos)

    //3. Data Source + DAO + Error Handling

    //With coroutines
    //suspend function

    suspend fun refreshTitle() {
        delay(500)
    }

    fun observeFields() = resultLiveData(databaseQuery = { fieldsDao.getFields()},
                                        networkCall = { buscaTuMotoDataSource.getFields()},
                                        saveCallResult = {fieldsDao.insert(it)}
                                ).distinctUntilChanged()



}