package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.remote.api.Result
import com.buscatumoto.databinding.DetailRelatedFragmentBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.adapters.CatalogueListAdapter
import com.buscatumoto.ui.viewmodels.DetailRelatedViewModel
import com.buscatumoto.utils.global.*
import com.buscatumoto.utils.injection.ViewModelFactory
import com.buscatumoto.utils.ui.CatalogueItemClickListener
import com.buscatumoto.utils.ui.PaginationListener
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class DetailRelatedFragment: BaseFragment(), Injectable,
    CatalogueItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: DetailRelatedFragmentBinding
    private lateinit var viewModel: DetailRelatedViewModel
    private lateinit var catalogueListAdapter : CatalogueListAdapter

    private var snackbarError: Snackbar? = null
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = PAGE_START


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_related_fragment, container, false)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailRelatedViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.getErrorMessage().observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                showErrorMessage(errorMessage)
            } else {
                hideError()
            }
        })


        var layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        catalogueListAdapter = CatalogueListAdapter(this, viewLifecycleOwner,
            BuscaTuMotoApplication.getInstance().applicationContext)
        binding.catalagueContentRv.adapter = catalogueListAdapter
        binding.catalagueContentRv.layoutManager = layoutManager
        binding.catalagueContentRv.addOnScrollListener(getScrollableListener(layoutManager))

        /**
         * To hide swipe refresh animation
         */
        binding.swipeRefresh.isRefreshing = false
        binding.swipeRefresh.isEnabled = false
        /**
         * To hide swipe refresh animation
         */

        return binding.root
    }

    private fun getScrollableListener(layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener {

        return object: PaginationListener(layoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                viewModel.loadMoreItems()
            }
            override fun isLastPage(): Boolean {
                return isLastPage
            }
            override fun isLoading(): Boolean {
                return isLoading
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.catalogueFragmentPbar.visibility = View.VISIBLE

        /**
         * Observer section
         */

        viewModel.relatedMotosData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.SUCCESS -> {
                    binding.catalogueFragmentPbar.visibility = View.GONE
                    binding.catalogueNoResults.visibility = View.GONE
                    isLoading = false

                    it.data?.let { list ->
                        if (it.data.motos.isEmpty()) {
                            binding.catalogueNoResults.visibility = View.VISIBLE
                        } else {

                            if (currentPage != PAGE_START) {
                                catalogueListAdapter.removeLoading()
                            }

                            binding.catalogueNoResults.visibility = View.GONE
                            catalogueListAdapter.addItems(it.data.motos)
                            binding.swipeRefresh.isRefreshing = false

                            //layoutManager = null
                        }
                    }
                }
                Result.Status.LOADING -> {
                    if (currentPage != PAGE_START) {
                        catalogueListAdapter.addLoading()
                    } else {
                        //Show global loading
                        binding.catalogueFragmentPbar.visibility = View.VISIBLE
                    }
                }
                Result.Status.ERROR -> {
                    binding.catalogueFragmentPbar.visibility = View.GONE
                    showErrorMessage(it.message)
                    //layoutManager = null
                }
            }
        })


        viewModel.isLastPageLiveData.observe(viewLifecycleOwner, Observer {
                result ->
            isLastPage = true
        })

        viewModel.currentPageLiveData.observe(viewLifecycleOwner, Observer {
                result ->
            this.currentPage = result
        })

        viewModel.navigateLiveData.observe(viewLifecycleOwner, Observer {
                result ->
            if (result) {
                findNavController().navigate(R.id.motoDetailHostFragment)
            }
        })

        viewModel.motoSelectedLiveData.observe(viewLifecycleOwner, Observer {
                result ->
            sendMotoSelectedAnalytics(result)
        })

        /**
         * Observer section
         */

        /**
         * Google ads
         */
        // Step 1 - Create an AdView and set the ad unit ID on it.
        val adView = AdView(requireContext())
        adView.adUnitId = GOOGLE_AD_TEST_UNIT_ID
        binding.adContainer.addView(adView)

        val adSize = getAdSize(requireActivity())

        // Step 4 - Set the adaptive ad size on the ad view.
        adView.adSize = adSize

        // Step 5 - Start loading the ad in the background.
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.adListener = object: AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                sendOnAdLeftApplicationAnalytics()
            }

            override fun onAdOpened() {
                super.onAdOpened()
                sendOnAdOpenedAnalytics()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                sendOnAdClickedAnalytics()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
                sendOnAdFailedToLoadAnalytics()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                sendOnAdClosedAnalytics()
            }
        }

        /**
         * Google ads
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }

    private fun hideError() {
        snackbarError?.dismiss()
    }

    private fun showErrorMessage(errorMessage: String?) {
        snackbarError =
            Snackbar.make(binding.root, errorMessage.toString(), Snackbar.LENGTH_INDEFINITE)
        snackbarError?.setAction(R.string.retry, viewModel.retryClickListener)
        snackbarError?.show()
    }

    /**
     * Google Analytics
     */

    private fun sendOnAdFailedToLoadAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, DETAIL_RELATED_AD_FAILED_TO_LOAD_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ADS_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendOnAdOpenedAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, DETAIL_RELATED_AD_OPENED_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ADS_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendOnAdClickedAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, DETAIL_RELATED_AD_CLICKED_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ADS_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendOnAdLeftApplicationAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, DETAIL_RELATED_AD_LEFT_APP_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ADS_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendOnAdClosedAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, DETAIL_RELATED_AD_CLOSED_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ADS_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendMotoSelectedAnalytics(result: MotoEntity?)  = firebaseAnalytics.run {
        val brandBundle = Bundle()
        brandBundle.putString(FirebaseAnalytics.Param.ITEM_ID, CATALOGUE_ITEM_BRAND_SELECTED_ID)
        brandBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CATALOGUE_CONTENT_TYPE)
        brandBundle.putString(FirebaseAnalytics.Param.VALUE, result?.brand)
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, brandBundle)

        val modelBundle = Bundle()
        modelBundle.putString(FirebaseAnalytics.Param.ITEM_ID, CATALOGUE_ITEM_MODEL_SELECTED_ID)
        modelBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CATALOGUE_CONTENT_TYPE)
        modelBundle.putString(FirebaseAnalytics.Param.VALUE, result?.model)
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, modelBundle)

        val displacementBundle = Bundle()
        displacementBundle.putString(FirebaseAnalytics.Param.ITEM_ID, CATALOGUE_ITEM_DISPLACEMENT_SELECTED_ID)
        displacementBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CATALOGUE_CONTENT_TYPE)
        displacementBundle.putString(FirebaseAnalytics.Param.VALUE, result?.displacement.toString())
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, displacementBundle)

        val weightBundle = Bundle()
        weightBundle.putString(FirebaseAnalytics.Param.ITEM_ID, CATALOGUE_ITEM_WEIGHT_SELECTED_ID)
        weightBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CATALOGUE_CONTENT_TYPE)
        weightBundle.putString(FirebaseAnalytics.Param.VALUE, result?.weight.toString())
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, weightBundle)

        val powerBundle = Bundle()
        powerBundle.putString(FirebaseAnalytics.Param.ITEM_ID, CATALOGUE_ITEM_POWER_SELECTED_ID)
        powerBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CATALOGUE_CONTENT_TYPE)
        powerBundle.putString(FirebaseAnalytics.Param.VALUE, result?.power.toString())
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, powerBundle)

        val priceBundle = Bundle()
        priceBundle.putString(FirebaseAnalytics.Param.ITEM_ID, CATALOGUE_ITEM_PRICE_SELECTED_ID)
        priceBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CATALOGUE_CONTENT_TYPE)
        priceBundle.putString(FirebaseAnalytics.Param.VALUE, result?.price.toString())
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, priceBundle)

        val yearBundle = Bundle()
        yearBundle.putString(FirebaseAnalytics.Param.ITEM_ID, CATALOGUE_ITEM_YEAR_SELECTED_ID)
        yearBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CATALOGUE_CONTENT_TYPE)
        yearBundle.putString(FirebaseAnalytics.Param.VALUE, result?.year.toString())
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, yearBundle)

        val licenseBundle = Bundle()
        licenseBundle.putString(FirebaseAnalytics.Param.ITEM_ID, CATALOGUE_ITEM_LICENSES_SELECTED_ID)
        licenseBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CATALOGUE_CONTENT_TYPE)
        licenseBundle.putString(FirebaseAnalytics.Param.VALUE, result?.licenses?.size.toString())
        this.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, licenseBundle)
    }

    /**
     * Google Analytics
     */


    override fun onItemClick(id: String) {
        viewModel.onItemClick(id)
    }
}