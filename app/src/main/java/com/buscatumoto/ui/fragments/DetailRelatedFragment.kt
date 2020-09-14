package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.buscatumoto.R
import com.buscatumoto.databinding.DetailRelatedFragmentBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.viewmodels.DetailRelatedViewModel
import com.buscatumoto.utils.global.*
import com.buscatumoto.utils.injection.ViewModelFactory
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class DetailRelatedFragment: BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: DetailRelatedFragmentBinding

    private lateinit var viewModel: DetailRelatedViewModel

    private lateinit var mAdView: AdView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_related_fragment, container, false)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailRelatedViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Google ads
         */

        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
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

    /**
     * Google Analytics
     */
}