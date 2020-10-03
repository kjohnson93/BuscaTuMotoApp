package com.buscatumoto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics

open class BaseFragment: Fragment() {

    protected lateinit var firebaseAnalytics: FirebaseAnalytics
    protected open val trackScreenView: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
    }

    override fun onResume() {
        super.onResume()

        if (trackScreenView) setCurrentScreen(this.javaClass.simpleName)
    }


    private fun setCurrentScreen(screenName: String) = firebaseAnalytics?.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, this@BaseFragment.javaClass.simpleName)
        logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}