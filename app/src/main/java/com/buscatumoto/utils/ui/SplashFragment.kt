package com.buscatumoto.utils.ui

import android.os.Bundle
import android.os.Handler
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.buscatumoto.R

class SplashFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transitionInflater = TransitionInflater.from(requireContext())
        allowEnterTransitionOverlap = false
        allowReturnTransitionOverlap = false
        enterTransition = transitionInflater.inflateTransition(R.transition.fade)
        exitTransition = transitionInflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_containerMainFragment)
        }, 5000)

    }
}