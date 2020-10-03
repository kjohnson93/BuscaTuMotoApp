package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.transition.TransitionInflater
import android.util.DisplayMetrics
import android.view.*
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentSearchBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.adapters.SearchBrandsRecyclerAdapter
import com.buscatumoto.ui.models.BrandRecyclerUiModel
import com.buscatumoto.ui.navigation.ScreenNavigator
import com.buscatumoto.ui.viewmodels.FrontPageViewModel
import com.buscatumoto.utils.global.*
import com.buscatumoto.utils.injection.ViewModelFactory
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject


class SearchFragment : BaseFragment(), Injectable,
    ScreenNavigator, SearchBrandsRecyclerAdapter.BrandItemClickListener {

    //Adapters
    private val searchBrandsAdapter = SearchBrandsRecyclerAdapter(this)

    private var errorSnackbar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: FrontPageViewModel
    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        allowEnterTransitionOverlap = false
        allowReturnTransitionOverlap = false
        exitTransition = inflater.inflateTransition(R.transition.slide_left)
        enterTransition = inflater.inflateTransition(R.transition.slide_right_long)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search, container, false)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FrontPageViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        loadBrands()

        getActivity()?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding.brandsRecyclerView.setAdapter(searchBrandsAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val brandListSize = searchBrandsAdapter.itemCount
        binding.brandsRecyclerView.autoScroll(
            brandListSize,
            AUTO_SCROLL_TIME_ELLAPSE_MILLIS
        )

        /**
         * Observer section
         */
        viewModel.navigateMutable.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.catalogueFragment)
            }
        })

        viewModel.errorModelMutable.observe(viewLifecycleOwner, Observer { result ->
            if (result.errorMessage != null) {
                showError(result.errorMessage)
            } else {
                hideError()
            }
        })

        val slideUp = slideUpAnimation(requireContext())
        Handler().postDelayed({
            binding.brandsRecyclerView.visibility = View.VISIBLE
            binding.brandsRecyclerView.startAnimation(slideUp)
        }, 1500)

        viewModel.searchTextMutable.observe(viewLifecycleOwner, Observer {
            hideKeyboardFrom(requireContext(), binding.root)
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

    override fun onDestroy() {
        super.onDestroy()
        binding.brandsRecyclerView.stopAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**This line does what doc says. It tells viewmodels attached to this view that they are no longer used.
        This way we avoid any variable state undesirable.*/
        viewModelStore.clear()
    }

    override fun navigateToNext(event: Int, extras: Bundle?) {
        hideError()
        findNavController().navigate(R.id.action_containerMainFragment_to_catalogueFragment, extras)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.getErrorClickListener())
        errorSnackbar?.show()
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    private fun loadBrands() {
        //Load brands locally (no remote request required) and change mutable to tell binded view to display it's data
        val context = BuscaTuMotoApplication.getInstance().baseContext
        val brandNamesTypedArray = context.resources.obtainTypedArray(R.array.filter_brand_names_array)
        val drawabletypedArray = context.resources.obtainTypedArray(R.array.filter_brand_logos_array)

        val  brandRecyclerUiModelList = ArrayList<BrandRecyclerUiModel?>()

        var index = 0

        while (index < drawabletypedArray.length()) {
            val brandRecyclerUiModel = BrandRecyclerUiModel(
                brandNamesTypedArray.getString(index), drawabletypedArray.getDrawable(
                    index
                )
            )
            brandRecyclerUiModelList.add(brandRecyclerUiModel)
            index ++
        }

        val modifiedList: List<BrandRecyclerUiModel?> = listOf(brandRecyclerUiModelList.last()) + brandRecyclerUiModelList + listOf(
            brandRecyclerUiModelList.first()
        )

        searchBrandsAdapter.updateBrandHighLights(modifiedList as List<BrandRecyclerUiModel>)
    }

    override fun onBrandItemClick(brand: String) {
        sendBrandItemSelectedAnalytics()
        viewModel.onBrandItemClick(brand)
    }

    private fun sendBrandItemSelectedAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, SEARCH_BRAND_ITEM_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, SEARCH_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendOnAdFailedToLoadAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, SEARCH_AD_FAILED_TO_LOAD_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ADS_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendOnAdOpenedAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, SEARCH_AD_OPENED_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ADS_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendOnAdClickedAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, SEARCH_AD_CLICKED_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ADS_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendOnAdLeftApplicationAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, SEARCH_AD_LEFT_APP_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ADS_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendOnAdClosedAnalytics() = firebaseAnalytics.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, SEARCH_AD_CLOSED_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ADS_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }


}