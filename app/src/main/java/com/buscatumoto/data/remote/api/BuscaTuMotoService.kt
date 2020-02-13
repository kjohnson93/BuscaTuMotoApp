package com.buscatumoto.data.remote.api

import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.utils.data.APIConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BuscaTuMotoService {

    @GET(APIConstants.GET_FIELDS_URL)
    suspend fun getFields(): Response<FieldsEntity>

    @GET(APIConstants.GET_BIKES_BY_BRAND)
    suspend fun getBikesByBrand(@Path("brand") brand: String): Response<List<MotoEntity>>
//
//    @GET(APIConstants.MOTO_FILTER_URL)
//    suspend fun filter(
//        @Query("brand") brand: String,
//        @Query("model") model: String,
//        @Query("tipo") bikeType: String,
//        @Query("precio_d") priceBottom: Int,
//        @Query("precio_u") priceTop: Int,
//        @Query("power_d") powerBottom: Double,
//        @Query("power_u") powerTop: Double,
//        @Query("cil_d") displacementBottom: Double,
//        @Query("cil_u") displacementTop: Double,
//        @Query("weight_d") weightBottom: Double,
//        @Query("weight_u") weightTop: Double,
//        @Query("year") year: Int,
//        @Query("licenses") license: String): Response<List<MotoEntity>>

    @GET(APIConstants.MOTO_FILTER_URL)
    suspend fun filter(@Query("brand") brand: String? = "",
                       @Query("model") model: String,
                       @Query("tipo") bikeType: String? = "",
                       @Query("precio_d") priceBottom: Int,
                       @Query("precio_u") priceTop: Int,
                       @Query("power_d") powerBottom: Double,
                       @Query("power_u") powerTop: Double,
                       @Query("cil_d") displacementBottom: Double,
                       @Query("cil_u") displacementTop: Double,
                       @Query("weight_d") weightBottom: Double,
                       @Query("weight_u") weightTop: Double,
                       @Query("year") year: Int,
                       @Query("licenses") license: String): Response<List<MotoEntity>>

    @GET(APIConstants.MOTO_SEARCH_URL)
    suspend fun search(@Path("search") search: String): Response<List<MotoEntity>>


}