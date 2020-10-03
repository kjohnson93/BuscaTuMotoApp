package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.buscatumoto.R
import com.buscatumoto.ui.adapters.TabViewPageAdapter
import com.buscatumoto.utils.global.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics

class ContainerMainFragment: BaseFragment() {

    override val trackScreenView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        allowEnterTransitionOverlap = false
        allowReturnTransitionOverlap = false
        exitTransition = inflater.inflateTransition(R.transition.slide_left)
        enterTransition = inflater.inflateTransition(R.transition.slide_right_combo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPagerAdapter: TabViewPageAdapter = TabViewPageAdapter(requireActivity())
        val viewpager = view.findViewById<ViewPager2>(R.id.mainViewPager)

        viewPagerAdapter.addFragment(SearchFragment())
        viewPagerAdapter.addFragment(FilterFragment())
        viewpager.adapter = viewPagerAdapter

        val iconList = arrayOf(resources
            .getDrawable(R.drawable.icon_home_128, requireActivity().theme),
        resources.getDrawable(R.drawable.icon_filter_128, requireActivity().theme))

        val tabLayout = view.findViewById<TabLayout>(R.id.mainTabLayout)
        TabLayoutMediator(tabLayout, viewpager) {
            tab, position ->
            tab.icon = iconList[position]
        }.attach()

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    SEARCH_FRAGMENT_TAB_POSITION -> {
                        sendSearchTabSelectedAnalytics()
                    }
                    FILTER_FRAGMENT_TAB_POSITION -> {
                        sendFilterTabSelectedAnalytics()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun sendFilterTabSelectedAnalytics() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FILTER_TAB_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, TAB_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun sendSearchTabSelectedAnalytics() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, SEARCH_TAB_ID)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, TAB_CONTENT_TYPE)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

}