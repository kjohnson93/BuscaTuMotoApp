package com.buscatumoto.data.remote.configuration

import android.util.Log
import com.buscatumoto.utils.data.Environment
import com.buscatumoto.utils.global.Constants
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
                sInstance =
                    BuscaTuMotoRetrofit()
            }

            return sInstance
        }

    }

    private var retrofit: Retrofit? = null
    private var buscaTuMotoService: BuscaTuMotoService? = null

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

        buscaTuMotoService = retrofit!!.create(BuscaTuMotoService::class.java)
    }

    fun getBuscaTuMotoApi(): BuscaTuMotoService? {
        return buscaTuMotoService
    }
}

// Decorator/wrapper for handling network responses.
sealed class APIGatewayResponse<T> {

    interface SuccessListener<T> {
        fun onResponse(response: T)
    }

    interface ErrorListener {
        fun onError(errorResponse: String?)
    }

}