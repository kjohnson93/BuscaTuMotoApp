package com.buscatumoto.data.remote.api

import com.buscatumoto.data.remote.model.response.FieldsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BuscaTuMotoService {

    @GET(APIConstants.GET_BRANDS_URL)
    fun getBrands(): Call<ArrayList<String>>

    @GET(APIConstants.GET_FIELDS_URL)
    fun getFields(): Call<FieldsResponse>

    @GET(APIConstants.GET_BIKES_BY_BRAND)
    fun getBikesByBrand(@Path("brand") brand: String): Call<ArrayList<String>>


}