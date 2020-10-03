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
            val response = buscaTuMotoDataSource.getMotosByBrand(brand)

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
    ): Result<MotoResponse> {

        val motoResponse = buscaTuMotoDataSource.filter(
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

    suspend fun getMotosSearchResponse(
        search: String,
        pageIndex: Int? = null
    ): Result<MotoResponse> {

        val motoResponse = buscaTuMotoDataSource.search(search, pageIndex)

        searchDao.delete()
        searchDao.insert(
            SearchEntity(
                1, search, null, null, null, null, null,
                null, null, null, null,
                null, null, null, null
            )
        )

        return motoResponse
    }

    suspend fun getMotosSearchRelatedResponse(id: String, pageIndex: Int? = null): Result<MotoResponse> {

        return buscaTuMotoDataSource.searchRelated(id, pageIndex)
    }

    /**
     * Gets last search query from Dao
     */
    fun getSearchParams() = searchDao.getSearchParamsDao()

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

    suspend fun insertFilter(
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
        license: String? = null) {
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

    }

    suspend fun saveMoto(motoEntity: MotoEntity) {
        motoDao.insert(motoEntity)
    }
}