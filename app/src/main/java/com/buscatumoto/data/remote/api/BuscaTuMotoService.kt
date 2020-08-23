package com.buscatumoto.data.remote.api

import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.dto.response.MotoResponse
import com.buscatumoto.utils.data.APIConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface that exposes API service operations to the app.
 */
interface BuscaTuMotoService {

    @GET(APIConstants.GET_FIELDS_URL)
    suspend fun getFields(): Response<FieldsEntity>

    @GET(APIConstants.GET_BIKES_BY_BRAND)
    suspend fun getBikesByBrand(@Path("brand") brand: String): Response<List<String>>

    @GET(APIConstants.MOTO_FILTER_URL)
    suspend fun filter(@Query("brand") brand: String? = null,
                       @Query("model") model: String? = null,
                       @Query("tipo") bikeType: String? = null,
                       @Query("precio_d") priceBottom: Int? = null,
                       @Query("precio_u") priceTop: Int? = null,
                       @Query("power_d") powerBottom: Double? = null,
                       @Query("power_u") powerTop: Double? = null,
                       @Query("cil_d") displacementBottom: Double? = null,
                       @Query("cil_u") displacementTop: Double? = null,
                       @Query("weight_d") weightBottom: Double? = null,
                       @Query("weight_u") weightTop: Double? = null,
                       @Query("year") year: Int? = null,
                       @Query("licenses") license: String? = null,
                       @Query("page") page: Int? = null): Response<MotoResponse>



    @GET(APIConstants.MOTO_SEARCH_URL)
    suspend fun search(@Path("search") search: String?, @Query("page") page: Int?): Response<MotoResponse>


}