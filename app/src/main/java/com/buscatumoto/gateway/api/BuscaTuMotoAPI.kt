package com.buscatumoto.gateway.api

import com.buscatumoto.gateway.model.forms.GetBrandsForm
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface BuscaTuMotoAPI {

    @GET(APIConstants.GET_BRANDS_URL)
    fun getBrands(): Call<ArrayList<String>>
}