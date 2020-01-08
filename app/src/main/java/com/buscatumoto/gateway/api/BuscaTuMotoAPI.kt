package com.buscatumoto.gateway.api

import com.buscatumoto.gateway.model.forms.GetBrandsForm
import com.buscatumoto.gateway.model.response.FieldsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BuscaTuMotoAPI {

    @GET(APIConstants.GET_BRANDS_URL)
    fun getBrands(): Call<ArrayList<String>>

    @GET(APIConstants.GET_FIELDS_URL)
    fun getFields(): Call<FieldsResponse>

    @GET(APIConstants.GET_BIKES_BY_BRAND)
    fun getBikesByBrand(@Path("brand") brand: String): Call<ArrayList<String>>


}