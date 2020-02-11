package com.buscatumoto.data.remote.repositories

import android.util.Log
import androidx.lifecycle.*
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.local.dao.FieldsDao
import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.remote.datasource.BuscaTuMotoDataSource
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.utils.global.Constants
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuscaTuMotoRepository @Inject constructor(private val buscaTuMotoDataSource: BuscaTuMotoDataSource,
                                                private val fieldsDao: FieldsDao, private val motoDao: MotoDao) {

    init {
        Log.d(Constants.MOTOTAG, "Injection")
    }

    suspend fun getFieldsEmit() = liveData<Result<FieldsEntity>> {
        val disposable = emitSource(fieldsDao.getFieldsLiveData().map {
            Result.loading(it)
        })

        try {
            val response = buscaTuMotoDataSource.getFields()

            //Stop the previous emission to avoid dispatching the updated value as 'loading'
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

    suspend fun getModelsByBrand(brand: String) = liveData<Result<List<MotoEntity>>> {
        val disposable = emitSource(motoDao.getMotoLiveData().map {
            Result.loading(it)
        })

        try {
            val response = buscaTuMotoDataSource.getMotos(brand)

            //Stop the previous emission to avoid dispatching the updated value as 'loading'
            disposable.dispose()

            response.data?.let {
                motoDao.deleteMotos()
                motoDao.insert(it)
            }

            //Re-establish the emission with success type
            emitSource(motoDao.getMotoLiveData().map {
                Result.success(it)
            })
        } catch (exception: IOException) {
            emitSource(motoDao.getMotoLiveData().map {
                Result.error("Error on getting moto repository", null)
            })
        }
    }


}