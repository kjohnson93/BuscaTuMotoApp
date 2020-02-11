package com.buscatumoto.data.remote.api

import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.utils.data.APIConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BuscaTuMotoService {

    @GET(APIConstants.GET_FIELDS_URL)
    suspend fun getFields(): Response<FieldsEntity>

    @GET(APIConstants.GET_BIKES_BY_BRAND)
    suspend fun getBikesByBrand(@Path("brand") brand: String): Response<List<MotoEntity>>




}