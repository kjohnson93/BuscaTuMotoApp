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
import retrofit2.http.Query
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuscaTuMotoRepository @Inject constructor(private val buscaTuMotoDataSource: BuscaTuMotoDataSource,
                                                private val fieldsDao: FieldsDao, private val motoDao: MotoDao) {

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

    suspend fun getMotos() = liveData<Result<List<MotoEntity>>>{
        val disposable = emitSource(motoDao.getMotoLiveData().map {
            Result.success(it)
        })
    }

    suspend fun filter(
        brand: String? = "",
        model: String,
        bikeType: String? = "",
        priceBottom: Int,
        priceTop: Int,
        powerBottom: Double,
        powerTop: Double,
        displacementBottom: Double,
        displacementTop: Double,
        weightBottom: Double,
        weightTop: Double,
        year: Int,
        license: String
    ) = liveData<Result<List<MotoEntity>>> {
        val disposable = emitSource(motoDao.getMotoLiveData().map {
            Result.loading(it)
        })

        disposable.dispose()


        try {
            val response = buscaTuMotoDataSource.filter(
                brand, model, bikeType,
                priceBottom, priceTop, powerBottom, powerTop, displacementBottom, displacementTop,
                weightBottom, weightTop, year, license
            )

            response.data?.let {
                motoDao.deleteMotos()
                motoDao.insert(it)
            }

            emitSource(motoDao.getMotoLiveData().map {
                Result.success(it)
            })
        } catch (exception: IOException) {
            emitSource(motoDao.getMotoLiveData().map {
                Result.error("Filter motos error", null)
            })
        }


    }


}