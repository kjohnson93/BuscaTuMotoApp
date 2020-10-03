package com.buscatumoto.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.buscatumoto.R
import com.buscatumoto.databinding.FragmentHostMotoDetailBinding
import com.buscatumoto.injection.Injectable
import com.buscatumoto.ui.adapters.DetailViewPagerAdapter
import com.buscatumoto.ui.viewmodels.MotoDetailViewModel
import com.buscatumoto.utils.global.*
import com.buscatumoto.utils.injection.ViewModelFactory
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class MotoDetailHostFragment: BaseFragment(), Injectable {

    override val trackScreenView = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MotoDetailViewModel
    private lateinit var binding: FragmentHostMotoDetailBinding
    private lateinit var detailPagerAdapter: DetailViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right_combo)
        exitTransition = inflater.inflateTransition(R.transition.slide_left_combo)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_host_moto_detail, container, false)

        viewModel =  ViewModelProviders.of(this, viewModelFactory).get(MotoDetailViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            detailPagerAdapter = DetailViewPagerAdapter(childFragmentManager)
            detailPagerAdapter.addFragment(DetailContentFragment(), "Contenido")
            detailPagerAdapter.addFragment(DetailRelatedFragment(), "Relacionados")
            this.bindAdapter(detailPagerAdapter)

        binding.detailViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //Empty method
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    DETAIL_CONTENT_TAB_POSITION -> {
                        sendContentTabSelectedAnalytics()
                    }
                    DETAIL_RELATED_TAB_POSITION -> {
                        sendRelatedTabSelectedAnalytics()
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                //Empty method
            }

        })
    }

    private fun bindAdapter(detailViewPagerAdapter: DetailViewPagerAdapter) {
        binding.detailViewPager.adapter = detailViewPagerAdapter
        val dotsIndicator = binding.wormDotsIndicator
        dotsIndicator.setViewPager(binding.detailViewPager)
    }

    /**
     * Google Analytics
     */

    private fun sendContentTabSelectedAnalytics() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, DETAIL_CONTENT_TAB_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, DETAIL_CONTENT_TAB_ID)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendRelatedTabSelectedAnalytics() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, DETAIL_RELATED_TAB_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, DETAIL_RELATED_TAB_ID)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    /**
     * Google Analytics
     */
}