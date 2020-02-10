package com.buscatumoto.data.remote.configuration

import com.buscatumoto.data.local.entity.FieldsEntity
import com.buscatumoto.utils.data.APIConstants
import retrofit2.Response
import retrofit2.http.GET

interface BuscaTuMotoService {

//    @GET(APIConstants.GET_BRANDS_URL)
//    fun getBrands(): Call<ArrayList<String>>

    @GET(APIConstants.GET_FIELDS_URL)
    suspend fun getFields(): Response<FieldsEntity>
//
//    @GET(APIConstants.GET_BIKES_BY_BRAND)
//    fun getBikesByBrand(@Path("brand") brand: String): Observable<List<MotoResponseItemModel>>




}