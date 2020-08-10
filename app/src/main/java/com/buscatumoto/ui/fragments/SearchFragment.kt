package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.transition.TransitionInflater
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentSearchBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.ui.viewmodels.FrontPageViewModel
import com.buscatumoto.utils.global.AUTO_SCROLL_TIME_ELLAPSE_MILLIS
import com.buscatumoto.utils.global.hideKeyboardFrom
import com.buscatumoto.utils.global.slideUpAnimation
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.BasicNavigator
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class SearchFragment : androidx.fragment.app.Fragment(), Injectable,
    ScreenNavigator {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }

        const val NAVIGATE_TO_CATALOGUE = 1
    }

    var activityReadyListener: ReadyListener? = null

    var arrowDownImgBtn: ImageButton? = null
    var filtrarBtn: Button? = null

    private var errorSnackbar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var basicNavigator: BasicNavigator

    lateinit var frontPageViewModel: FrontPageViewModel
    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        allowEnterTransitionOverlap = false
        allowReturnTransitionOverlap = false
        exitTransition = inflater.inflateTransition(R.transition.slide_left)
        enterTransition = inflater.inflateTransition(R.transition.slide_right_long)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search, container, false)

        frontPageViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FrontPageViewModel::class.java)
        frontPageViewModel.lifeCycleOwner = this
        frontPageViewModel.screenNavigator = this
        binding.viewModel = frontPageViewModel
        binding.lifecycleOwner = this


        frontPageViewModel.errorModelMutable.observe(viewLifecycleOwner, Observer { result ->
            if (result.errorMessage != null) {
                showError(result.errorMessage)
            } else {
                hideError()
            }
        })

        getActivity()?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        val brandListSize = frontPageViewModel.searchBrandsAdapter.itemCount
        binding.FragmentSearchBrandsRV.autoScroll(
            brandListSize,
            AUTO_SCROLL_TIME_ELLAPSE_MILLIS
        )

        val slideUp = slideUpAnimation(requireContext())

        /*
        Observer section
         */

        frontPageViewModel.drawableLoadMutable.observe(viewLifecycleOwner, Observer {
            result ->
            Handler().postDelayed( {
                binding.FragmentSearchBrandsRV.visibility = View.VISIBLE
                binding.FragmentSearchBrandsRV.startAnimation(slideUp)
            }, 1500)
        })

        frontPageViewModel.searchTextMutable.observe(viewLifecycleOwner, Observer {
            hideKeyboardFrom(requireContext(), binding.root)
        })

        /*
        Observer section
         */

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.FragmentSearchBrandsRV.stopAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }

    interface ReadyListener {
        fun onReady()
        fun showError(errorResponse: String?) {
        }
    }

    override fun navigateToNext(event: Int, extras: Bundle?) {
        hideError()
        findNavController().navigate(R.id.action_containerMainFragment_to_catalogueFragment, extras)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, frontPageViewModel.getErrorClickListener())
        errorSnackbar?.show()
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}