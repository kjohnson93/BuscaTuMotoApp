package com.buscatumoto

import android.content.Context
import android.content.res.Configuration
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.buscatumoto.injection.AppInjector
import com.buscatumoto.utils.data.Environment
import com.buscatumoto.utils.global.DEBUG
import com.buscatumoto.utils.global.RELEASE
import com.buscatumoto.utils.global.ReleaseTree
import com.google.android.gms.ads.MobileAds
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class BuscaTuMotoApplication: LocalizationApplication(), HasAndroidInjector {

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

    private val localizationDelegate = LocalizationApplicationDelegate()

    //Gets current app's language
    override fun getDefaultLanguage(): Locale {
        return Locale.getDefault()
    }

    override fun attachBaseContext(base: Context) {
        localizationDelegate.setDefaultLanguage(base, Locale("es"))
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun onCreate() {
        super.onCreate()

        sInstance = this

        when (BuildConfig.BUILD_TYPE.toLowerCase()) {
            DEBUG.toLowerCase() -> {
                Timber.plant(Timber.DebugTree())
            }
            RELEASE.toLowerCase() -> {
                Timber.plant(ReleaseTree())
            }
        }

        AppInjector.init(this)

        //Google Ads initialization
        MobileAds.initialize(this)
    }

    fun getEnvironmentBaseUrl(): String {

        var enviroment = Environment.DEVELOP.path

        when (BuildConfig.BUILD_TYPE.toLowerCase()) {
            DEBUG.toLowerCase() -> {
                enviroment = Environment.DEVELOP.path
            }
            RELEASE.toLowerCase() -> {
                enviroment = Environment.RELEASE.path
            }
        }
        return enviroment
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}