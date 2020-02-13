package com.buscatumoto.ui.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.buscatumoto.utils.global.Constants
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentSearchBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.ui.activities.CatalogueActivity
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.ui.viewmodels.FrontPageViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.BasicNavigator
import timber.log.Timber
import javax.inject.Inject


class SearchFragment : androidx.fragment.app.Fragment(), View.OnClickListener, Injectable, ScreenNavigator {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }

        const val NAVIGATE_TO_CATALOGUE = 1
    }

    var activityReadyListener: ReadyListener? = null

    var bannerLogo: ImageView? = null
    var arrowDownImgBtn: ImageButton? = null
    var filtrarBtn: Button? = null

    var dialogFiltoFragment: FilterFormDialogFragment = FilterFormDialogFragment.newInstance()

    var mLastClickTime: Long = 0

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var basicNavigator: BasicNavigator

    lateinit var frontPageViewModel: FrontPageViewModel
    private lateinit var binding: FragmentSearchBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search, container, false )
        binding.arrowDownImgBtn.setOnClickListener(this)
        binding.filtrarBtn.setOnClickListener(this)

        frontPageViewModel = ViewModelProviders.of(this, viewModelFactory).get(FrontPageViewModel::class.java)
        frontPageViewModel.lifeCycleOwner = this
        frontPageViewModel.screenNavigator = this
        binding.viewModel = frontPageViewModel
        binding.lifecycleOwner = this


        getActivity()?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        arrowDownImgBtn?.setOnClickListener(this)
        filtrarBtn?.setOnClickListener(this)

        val brandListSize = frontPageViewModel.searchBrandsAdapter.itemCount
        binding.fragmentSearchBrandsRv.autoScroll(brandListSize, Constants.AUTO_SCROLL_TIME_ELLAPSE_MILLIS)

        return binding.root
    }

    private fun navigateWithBrand(brand: String?) {
        Timber.d("Navigated with brand $brand")
        basicNavigator.navigateToIntent(requireActivity(), CatalogueActivity::class.java, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.fragmentSearchBrandsRv.stopAutoScroll()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.activityReadyListener?.let {
            this.activityReadyListener!!.onReady()
        }
    }

    interface ReadyListener {
        fun onReady()
        fun showError(errorResponse: String?) {
        }
    }

    fun setActivityDelegate(activity: ReadyListener) {
        this.activityReadyListener = activity
    }

    fun openFilterDialogFragment() {
        // Make sure the current transaction finishes first
        var isFMnull = true
        isFMnull = fragmentManager == null

        Timber.d("Is FM NULL $isFMnull")
        fragmentManager?.executePendingTransactions()
//        val fragmentTransactionAdd = fragmentManager?.beginTransaction()
        val prev = fragmentManager?.findFragmentByTag("dialog")
        // If there is no fragment yet with this tag then show it. Otherwise donothing
        //This is to prevent crash if user is clicking too fast
        if (prev == null) {
            fragmentManager?.let {
                dialogFiltoFragment.show(it, "dialog")
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.filtrarBtn.id -> {
                openFilterDialogFragment()

            }
            binding.arrowDownImgBtn.id -> {
                openFilterDialogFragment()
            }
        }
    }

    override fun navigateToNext(event: Int) {
        when (event) {
            NAVIGATE_TO_CATALOGUE -> {

            }
        }
    }
}