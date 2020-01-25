package com.buscatumoto

import android.app.Application
import android.os.Build
import com.buscatumoto.data.remote.datasource.BuscaTuMotoGateway
import com.buscatumoto.utils.data.Environment
import com.buscatumoto.utils.global.Constants

class BuscaTuMotoApplication: Application() {

    var buscaTuMotoGateway: BuscaTuMotoGateway? = null

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

        buscaTuMotoGateway =
            BuscaTuMotoGateway()
        sInstance = this
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
}