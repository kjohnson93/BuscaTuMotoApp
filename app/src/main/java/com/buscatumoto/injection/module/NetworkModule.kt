package com.buscatumoto.injection.module

import android.app.Application
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.data.remote.configuration.BuscaTuMotoService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

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
        return Retrofit.Builder().baseUrl(BuscaTuMotoApplication.getInstance()
            .getEnvironmentBaseUrl())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create()).build()
    }

//    @Singleton
//    @Provides
//    fun provideDb(app: Application) = AppDatabase.getInstance(app)
//
//    @Provides
//    @Singleton
//    fun providesSearchDao(db: AppDatabase) = db.searchDao()
}