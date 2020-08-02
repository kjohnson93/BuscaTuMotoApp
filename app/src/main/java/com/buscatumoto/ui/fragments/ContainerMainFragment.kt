package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.buscatumoto.R
import com.buscatumoto.ui.adapters.TabViewPageAdapter
import com.buscatumoto.ui.fragments.dialog.FilterFormDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ContainerMainFragment: Fragment() {

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
        viewPagerAdapter.addFragment(FilterFormDialogFragment())
        viewpager.adapter = viewPagerAdapter

        val iconList = arrayOf(resources
            .getDrawable(R.drawable.ic_home_128, requireActivity().theme),
        resources.getDrawable(R.drawable.filter_icon_128, requireActivity().theme))

        val tabLayout = view.findViewById<TabLayout>(R.id.mainTabLayout)
        TabLayoutMediator(tabLayout, viewpager) {
            tab, position ->
            tab.icon = iconList[position]
        }.attach()
    }

}