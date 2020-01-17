package com.buscatumoto.injection.module

import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Module that will provide any object required to be injected related with REST API service.
 */
@Module
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun providesPostService(retrofit: Retrofit): BuscaTuMotoService {
        return retrofit.create(BuscaTuMotoService::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BuscaTuMotoApplication.getInstance().getEnvironmentBaseUrl()).addConverterFactory(MoshiConverterFactory.create()).build()
    }
}