package com.buscatumoto.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import com.buscatumoto.utils.global.Constants
import com.buscatumoto.R
import com.buscatumoto.databinding.BrandHighlightItemRowBinding
import com.buscatumoto.databinding.FragmentSearchBinding
import com.buscatumoto.injection.component.DaggerViewComponent
import com.buscatumoto.injection.component.DaggerViewModelComponent
import com.buscatumoto.injection.component.ViewComponent
import com.buscatumoto.injection.component.ViewModelComponent
import com.buscatumoto.injection.module.NetworkModule
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.buscatumoto.ui.viewmodels.FrontPageViewModel
import com.buscatumoto.utils.injection.ViewModelFactory
import javax.inject.Inject


class SearchFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    var activityReadyListener: ReadyListener? = null

    var bannerLogo: ImageView? = null
    var arrowDownImgBtn: ImageButton? = null
    var filtrarBtn: Button? = null

    var dialogFiltoFragment: FilterFormDialogFragment = FilterFormDialogFragment.newInstance()

    var mLastClickTime: Long = 0

    private val injector: ViewModelComponent = DaggerViewModelComponent.builder().networkModule(
        NetworkModule
    ).build()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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

        injector.inject(this)

//        binding.fragmentSearchBrandsRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        frontPageViewModel = ViewModelProviders.of(this, viewModelFactory).get(FrontPageViewModel::class.java)
        binding.viewModel = frontPageViewModel

        frontPageViewModel.getbrandSelected().observe(this,
            Observer { brand -> navigateWithBrand(brand) })

        getActivity()?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        arrowDownImgBtn?.setOnClickListener(this)
        filtrarBtn?.setOnClickListener(this)

        val brandListSize = frontPageViewModel.searchBrandsAdapter.itemCount
        binding.fragmentSearchBrandsRv.autoScroll(brandListSize, Constants.AUTO_SCROLL_TIME_ELLAPSE_MILLIS)

        return binding.root
    }

    private fun navigateWithBrand(brand: String?) {
        Log.d(Constants.MOTOTAG, "Navigate with brand: $brand")
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

        Log.d(Constants.MOTOTAG, "is FM Null $isFMnull")
        fragmentManager?.executePendingTransactions()
//        val fragmentTransactionAdd = fragmentManager?.beginTransaction()
        val prev = fragmentManager?.findFragmentByTag("dialog")
        // If there is no fragment yet with this tag then show it. Otherwise donothing
        //This is to prevent crash if user is clicking too fast
        if (prev == null) {
            dialogFiltoFragment.show(fragmentManager, "dialog")
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
}