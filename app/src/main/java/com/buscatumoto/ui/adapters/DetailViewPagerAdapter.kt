package com.buscatumoto.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.buscatumoto.ui.fragments.DetailContentFragment
import com.buscatumoto.ui.fragments.DetailRelatedFragment
import com.buscatumoto.ui.fragments.DetailSpecsFragment


class DetailViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val DETAIL_CONTENT_PAGE = 0
        const val DETAIL_SPECS_PAGE = 1
        const val DETAIL_RELATED_PAGE = 2
    }

    override fun getItem(position: Int): Fragment = when (position) {
        DETAIL_CONTENT_PAGE -> {
            DetailContentFragment()
        }
        DETAIL_SPECS_PAGE -> {
            DetailSpecsFragment()
        }
        DETAIL_RELATED_PAGE -> {
            DetailRelatedFragment()
        }
        else -> {
            //default fallback
            DetailContentFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }
}