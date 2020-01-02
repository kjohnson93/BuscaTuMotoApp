package com.buscatumoto.gateway.api

import android.util.Log
import com.buscatumoto.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BuscaTuMotoRetrofit {

    companion object {

        var sInstance: BuscaTuMotoRetrofit? = null

        fun getInstance(): BuscaTuMotoRetrofit? {
            if (sInstance == null) {
                sInstance = BuscaTuMotoRetrofit()
            }

            return sInstance
        }

    }

    private var retrofit: Retrofit? = null
    private var buscaTuMotoAPI: BuscaTuMotoAPI? = null

    init {
        val environment = Environment.DEVELOP
        val hostUrl = environment.path

        Log.d(Constants.MOTOTAG, "HOST ENVIRONMENT URL: $hostUrl")

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.header("Content-Type", "application/json")
                    return chain.proceed(requestBuilder.build())
                }
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(hostUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        buscaTuMotoAPI = retrofit!!.create(BuscaTuMotoAPI::class.java)
    }

    fun getBuscaTuMotoApi(): BuscaTuMotoAPI? {
        return buscaTuMotoAPI
    }
}