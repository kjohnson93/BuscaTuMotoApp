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
import com.buscatumoto.utils.data.TotalElementsObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuscaTuMotoRepository @Inject constructor(
    private val buscaTuMotoDataSource: BuscaTuMotoDataSource,
    private val fieldsDao: FieldsDao, private val motoDao: MotoDao,
    private val searchDao: SearchDao
) {

    /**
     * Gets the fields values from a Dao Source
     * Dao source means that response is always ruled by DAO Single Source Of Truth (SSOT)
     * Api network call just updates a DAO but response is only retrieved from DAO.
     */
    suspend fun getFields() = liveData<Result<FieldsEntity>> {

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

    /**
     * Gets models of a specific brand and obtains them from Dao Source.
     * Dao source means that response is always ruled by DAO Single Source Of Truth (SSOT)
     * Api network call just updates a DAO but response is only retrieved from DAO.
     */
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

    /**
     * Gets motorcycles based on filter values and obtains them from Dao Source.
     * Dao source means that response is always ruled by DAO Single Source Of Truth (SSOT)
     * Api network call just updates a DAO but response is only retrieved from DAO.
     */
    suspend fun getMotosFilter(
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

    /**
     * Gets motorcycles based on filter values from API.
     * Response means that values are being collected directly by an
     * Api network call which is a API Source.
     */
    suspend fun getMotosFilterResponse(
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

        return motoResponse
    }

    /**
     * Inserts last search made in a DAO so it can be stored locally and used later.
     */
    suspend fun insertSearch(search: String) {
        searchDao.delete()
        searchDao.insert(
            SearchEntity(
                1, search, null, null, null, null, null,
                null, null, null, null,
                null, null, null, null
            )
        )
    }

    /**
     * Gets motorcycles based on search query and obtains them from Dao.
     * Dao source means that response is always ruled by DAO Single Source Of Truth (SSOT)
     * Api network call just updates a DAO but response is only retrieved from DAO.
     */
    suspend fun getMotosSearch(search: String, pageIndex : Int?)
            = liveData<Result<PagedListMotoEntity>> {

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
    }

    /**
     * Gets last search query from Dao
     */
    fun getSearchParams() = searchDao.getSearchParamsDao()

    suspend fun fetchCatalogueData(search: String?,
                                   page: Int,
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
                                   license: String? = null): Result<MotoResponse> {
        return if (search != null) {
            buscaTuMotoDataSource.fetchCatalogueDataSearch(search, page)
        } else {
            buscaTuMotoDataSource.fetchCatalogueDataFilter(
                brand, model, bikeType,
                priceBottom, priceTop, powerBottom, powerTop,
                displacementBottom, displacementTop, weightBottom,
                weightTop, year, license, page
            )
        }

    }

    suspend fun deleteMotoDao(): List<MotoEntity> {
        motoDao.delete()
        return motoDao.getMotos()
    }

}