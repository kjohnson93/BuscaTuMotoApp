package com.buscatumoto.gateway.api

import android.util.Log
import com.buscatumoto.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscaTuMotoGateway {


    fun getBrands() {

        val buscaTuMotoAPI = BuscaTuMotoRetrofit.getInstance()?.getBuscaTuMotoApi()
        val call: Call<ArrayList<String>> = buscaTuMotoAPI!!.getBrands()

        call.enqueue(object: Callback<ArrayList<String>> {
            override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<ArrayList<String>>,
                response: Response<ArrayList<String>>
            ) {
                Log.d(Constants.MOTOTAG, "response $response")
            }

        })
    }
}