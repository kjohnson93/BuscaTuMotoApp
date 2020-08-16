package com.buscatumoto.data.remote.repositories

import androidx.lifecycle.*
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.data.local.dao.FieldsDao
import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.data.local.dao.SearchDao
import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.remote.datasource.BuscaTuMotoDataSource
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.local.entity.SearchEntity
import com.buscatumoto.data.remote.dto.response.MotoResponse
import com.buscatumoto.data.remote.dto.response.PagedListMotoEntity
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuscaTuMotoRepository @Inject constructor(
    private val buscaTuMotoDataSource: BuscaTuMotoDataSource,
    private val fieldsDao: FieldsDao, private val motoDao: MotoDao,
    private val searchDao: SearchDao
) {

    suspend fun getFieldsEmit() = liveData<Result<FieldsEntity>> {

        //Not using emitSource because it's not getting observed by subscribers for some reason
        emit(Result.loading())

        try {
            val response = buscaTuMotoDataSource.getFields()

            if (response.status == Result.Status.SUCCESS) {
                //update the database
                response.data?.let {
                    fieldsDao.insert(it)
                }
                //Re-establish the emission with success type
                emitSource(fieldsDao.getFieldsLiveData().map {
                    Result.success(it)
                })
            } else if (response.status == Result.Status.ERROR) {
                emitSource(fieldsDao.getFieldsLiveData().map {
                    Result.error(response.message, null)
                })
            }
        } catch (exception: IOException) {
            emitSource(fieldsDao.getFieldsLiveData().map {
                Result.error("Error on getting fields repository", null)
            })
        }
    }

    suspend fun getModelsByBrand(brand: String) = liveData<Result<FieldsEntity>> {

        //Not using emitSource because it's not getting observed by subscribers for some reason
        emit(Result.loading())

        try {
            val response = buscaTuMotoDataSource.getMotos(brand)

            if (response.status == Result.Status.SUCCESS) {
                response.data?.let {
                    var entity = fieldsDao.getFields()
                    entity.models = response.data
                    fieldsDao.updateModels(entity)
                }

                //Re-establish the emission with success type
                emitSource(fieldsDao.getFieldsLiveData().map {
                    Result.success(it)
                })
            } else if (response.status == Result.Status.ERROR) {
                emitSource(fieldsDao.getFieldsLiveData().map {
                    Result.error(response.message, data = null)
                })
            }
        } catch (exception: IOException) {
            emitSource(fieldsDao.getFieldsLiveData().map {
                Result.error("Error on getting moto repository", null)
            })
        }
    }

    suspend fun getMotos() = liveData<Result<List<MotoEntity>>> {
        val disposable = emitSource(motoDao.getMotoLiveData().map {
            Result.success(it)
        })
    }

    suspend fun filter(
        brand: String? = null,
        model: String? = null,
        bikeType: String? = null,
        priceBottom: Int? = null,
        priceTop: Int? = null,
        powerBottom: Double? = null,
        powerTop: Double? = null,
        displacementBottom: Double? = null,
        displacementTop: Double? = null,
        weightBottom: Double? = null,
        weightTop: Double? = null,
        year: Int? = null,
        license: String? = null,
        pageIndex: Int? = null
    ) = liveData<Result<PagedListMotoEntity>> {
        //Not using emitSource because it's not getting observed by subscribers for some reason
        emit(Result.loading())

        try {
            val response = buscaTuMotoDataSource.filter(
                brand, model, bikeType,
                priceBottom, priceTop, powerBottom, powerTop, displacementBottom, displacementTop,
                weightBottom, weightTop, year, license, pageIndex
            )

            if (response.status == Result.Status.SUCCESS) {
                response.data?.let { motoResponse ->

                    motoDao.delete()
                    searchDao.delete()
                    searchDao.insert(
                        SearchEntity(
                            1,
                            null,
                            brand,
                            model,
                            bikeType,
                            priceBottom.toString(),
                            priceTop.toString(),
                            powerBottom.toString(),
                            powerTop.toString(),
                            displacementBottom.toString(),
                            displacementTop.toString(),
                            weightBottom.toString(),
                            weightTop.toString(),
                            year.toString(),
                            license
                        )
                    )
                    motoDao.insert(motoResponse.motos)

                    emitSource(motoDao.getMotoLiveData().map {
                        Result.success(PagedListMotoEntity(it, motoResponse.totalElements))
                    })
                }

            } else if (response.status == Result.Status.ERROR) {
                emitSource(motoDao.getMotoLiveData().map {
                    Result.error(response.message, null)
                })
            }
        } catch (exception: IOException) {
            emitSource(motoDao.getMotoLiveData().map {
                Result.error("Filter motos error", null)
            })
        }
    }

    suspend fun filterNoLiveData(
        brand: String? = null,
        model: String? = null,
        bikeType: String? = null,
        priceBottom: Int? = null,
        priceTop: Int? = null,
        powerBottom: Double? = null,
        powerTop: Double? = null,
        displacementBottom: Double? = null,
        displacementTop: Double? = null,
        weightBottom: Double? = null,
        weightTop: Double? = null,
        year: Int? = null,
        license: String? = null,
        pageIndex: Int? = null
    ): MotoResponse? {

        val motoResponse = buscaTuMotoDataSource.filterNoLiveData(
            brand, model, bikeType,
            priceBottom, priceTop, powerBottom, powerTop, displacementBottom, displacementTop,
            weightBottom, weightTop, year, license, pageIndex
        )
        motoDao.delete()
        searchDao.delete()
        searchDao.insert(
            SearchEntity(
                1,
                null,
                brand,
                model,
                bikeType,
                priceBottom.toString(),
                priceTop.toString(),
                powerBottom.toString(),
                powerTop.toString(),
                displacementBottom.toString(),
                displacementTop.toString(),
                weightBottom.toString(),
                weightTop.toString(),
                year.toString(),
                license
            )
        )

        motoResponse?.let {
            motoDao.insert(it.motos)
        }

        return motoResponse
    }

    suspend fun search(search: String, pageIndex: Int?) = liveData<Result<PagedListMotoEntity>> {

        //Not using emitSource because it's not getting observed by subscribers for some reason
        emit(Result.loading())

        //try network request and dispose local process
        try {
            val response = buscaTuMotoDataSource.search(search, pageIndex)

            if (response.status == Result.Status.SUCCESS) {
                //save
                response.data?.let { motoResponse ->

                    motoDao.delete()
                    searchDao.delete()
                    searchDao.insert(
                        SearchEntity(
                            1, search, null, null, null, null, null,
                            null, null, null, null,
                            null, null, null, null
                        )
                    )
                    motoDao.insert(motoResponse.motos)

                    emitSource(motoDao.getMotoLiveData().map {
                        Result.success(PagedListMotoEntity(it, motoResponse.totalElements))
                    })
                }
            } else if (response.status == Result.Status.ERROR) {
                emitSource(motoDao.getMotoLiveData().map {
                    Result.error("An IO error has ocurred on search data fetch", null)
                })
            }
        } catch (exception: IOException) {
            emitSource(motoDao.getMotoLiveData().map {
                Result.error("An IO error has ocurred on search data fetch", null)
            })
        }


        //save and return
    }

    fun getSearchParams() = searchDao.getSearchParams()


}