package com.buscatumoto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        viewPagerAdapter.addFragment(FilterFormDialogFragment())
        viewPagerAdapter.addFragment(FilterFormDialogFragment())
        viewpager.adapter = viewPagerAdapter

        val titleList = arrayOf("Content", "Related")

        val tabLayout = view.findViewById<TabLayout>(R.id.mainTabLayout)
        TabLayoutMediator(tabLayout, viewpager) {
            tab, position ->
            tab.text = titleList[position]
        }.attach()
    }

}