package com.buscatumoto

import android.app.Application
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

        when (BuildConfig.BUILD_TYPE.toLowerCase()) {
            Constants.DEBUG.toLowerCase() -> {
                enviroment = Environment.DEVELOP.path
            }
            Constants.RELEASE.toLowerCase() -> {
                enviroment = Environment.RELEASE.path
            }
        }
        return enviroment
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}