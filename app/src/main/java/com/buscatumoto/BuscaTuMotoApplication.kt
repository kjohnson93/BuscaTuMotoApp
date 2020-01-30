package com.buscatumoto

import android.app.Activity
import android.app.Application
import androidx.databinding.library.BuildConfig
import com.buscatumoto.data.remote.datasource.BuscaTuMotoGateway
import com.buscatumoto.injection.AppInjector
import com.buscatumoto.utils.data.Environment
import com.buscatumoto.utils.global.Constants
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class BuscaTuMotoApplication: Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    //Static members
    companion object {
        //Singleton instance of app.
        private var sInstance: BuscaTuMotoApplication? = null

        //static getter. Should crash if null.
        public fun getInstance(): BuscaTuMotoApplication {
            return sInstance!!
        }
    }

    override fun onCreate() {
        super.onCreate()

        sInstance = this

        AppInjector.init(this)
    }

    fun getEnvironmentBaseUrl(): String {

        var enviroment = Environment.DEVELOP.path

        when (BuildConfig.BUILD_TYPE) {
            Constants.DEBUG -> {
                enviroment = Environment.DEVELOP.path
            }
            Constants.RELEASE -> {
                enviroment = Environment.RELEASE.path
            }
        }
        return enviroment
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}