package com.buscatumoto.data.remote.configuration

import com.buscatumoto.data.remote.dto.response.FieldsResponse
import com.buscatumoto.utils.data.APIConstants
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BuscaTuMotoService {

    @GET(APIConstants.GET_BRANDS_URL)
    fun getBrands(): Call<ArrayList<String>>

    @GET(APIConstants.GET_FIELDS_URL)
    fun getFields(): Observable<FieldsResponse>

    @GET(APIConstants.GET_BIKES_BY_BRAND)
    fun getBikesByBrand(@Path("brand") brand: String): Observable<ArrayList<String>>


}