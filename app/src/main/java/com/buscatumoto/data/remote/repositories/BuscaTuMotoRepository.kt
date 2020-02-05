package com.buscatumoto.data.remote.repositories

import android.util.Log
import androidx.lifecycle.*
import com.buscatumoto.data.Result
import com.buscatumoto.data.local.SearchDao
import com.buscatumoto.data.local.dao.FieldsDao
import com.buscatumoto.data.local.entity.Fields
import com.buscatumoto.data.remote.datasource.BuscaTuMotoDataSource
import com.buscatumoto.data.remote.dto.response.FieldsResponse
import com.buscatumoto.data.resultLiveData
import com.buscatumoto.utils.global.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuscaTuMotoRepository @Inject constructor(private val buscaTuMotoDataSource: BuscaTuMotoDataSource, private val fieldsDao: FieldsDao) {

    init {
        Log.d(Constants.MOTOTAG, "Injection")
    }

    suspend fun getFieldsEmit() = liveData<Result<Fields>> {
        val disposable =emitSource(fieldsDao.getFieldsLiveData().map {
            Result.loading(it)
        })

        try {
            val response = buscaTuMotoDataSource.getFields()

            //Stop the previous emission to avoid dispatching the updated user as 'loading'
            disposable.dispose()
            //update the database
            response.data?.let {
                fieldsDao.insert(it)
            }
            //Re-establish the emission with success type
            emitSource(fieldsDao.getFieldsLiveData().map {
                Result.success(it)
            })


        } catch (exception: IOException) {
            emitSource(fieldsDao.getFieldsLiveData().map {
                Result.error("Error on getting fields repository", null)
            })
        }
    }


}