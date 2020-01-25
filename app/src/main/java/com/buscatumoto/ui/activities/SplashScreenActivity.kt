package com.buscatumoto.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.os.Handler
import com.buscatumoto.R
import com.buscatumoto.utils.ui.BasicNavigator


/**
 * Class that represents a splash screen.
 * FIX THIS: Right now app contains 2 splash screen. TO BE determined which one will be picked.
 */
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val secondsDelayed = 1
        Handler().postDelayed(Runnable {
            this.navigate()
            this.finish()
        }, (secondsDelayed * 3000).toLong())
    }

    /**
     * Navigate to default next point.
     */
    private fun navigate() {
        var basicNavigator = BasicNavigator()
        basicNavigator.navigateToIntent(this, SearchActivity::class.java, null)
    }

}
