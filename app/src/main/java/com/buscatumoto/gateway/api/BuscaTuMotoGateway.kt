package com.buscatumoto.gateway.api

import android.util.Log
import com.buscatumoto.Constants
import com.buscatumoto.gateway.model.response.FieldsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BuscaTuMotoGateway {


    fun getBrands() {

        val buscaTuMotoAPI = BuscaTuMotoRetrofit.getInstance()?.getBuscaTuMotoApi()
        val call: Call<ArrayList<String>> = buscaTuMotoAPI!!.getBrands()

        call.enqueue(object: Callback<ArrayList<String>> {
            override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                Log.d(Constants.MOTOTAG, "get Brands onFailure: ${t.message}")
            }

            override fun onResponse(
                call: Call<ArrayList<String>>,
                response: Response<ArrayList<String>>
            ) {
                Log.d(Constants.MOTOTAG, "get brands response ${response.body()}")
            }

        })


    }

    fun getFields() {
        val buscaTuMotoAPI = BuscaTuMotoRetrofit.getInstance()?.getBuscaTuMotoApi()
        val call: Call<FieldsResponse> = buscaTuMotoAPI!!.getFields()

        call.enqueue(object: Callback<FieldsResponse> {
            override fun onFailure(call: Call<FieldsResponse>, t: Throwable) {
                Log.d(Constants.MOTOTAG, "get fields onFailure: ${t.message}")
            }

            override fun onResponse(
                call: Call<FieldsResponse>,
                response: Response<FieldsResponse>
            ) {
                Log.d(Constants.MOTOTAG, "get fields response ${response.body()}")
            }


        })

    }
}