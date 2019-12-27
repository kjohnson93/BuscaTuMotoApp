package com.buscatumoto

import android.app.Application

class BuscaTuMotoApplication: Application() {

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
    }
}