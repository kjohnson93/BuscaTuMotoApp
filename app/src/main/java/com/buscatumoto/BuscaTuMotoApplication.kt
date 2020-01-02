package com.buscatumoto

import android.app.Application
import com.buscatumoto.gateway.api.BuscaTuMotoGateway

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

        buscaTuMotoGateway = BuscaTuMotoGateway()
        sInstance = this
    }
}