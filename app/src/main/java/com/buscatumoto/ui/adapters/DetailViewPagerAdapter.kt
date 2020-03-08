package com.buscatumoto.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.buscatumoto.ui.fragments.DetailContentFragment
import com.buscatumoto.ui.fragments.DetailRelatedFragment
import com.buscatumoto.ui.fragments.DetailSpecsFragment
import com.buscatumoto.utils.global.Constants


class DetailViewPagerAdapter(val idMoto: String?,fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val DETAIL_CONTENT_PAGE = 0
        const val DETAIL_SPECS_PAGE = 1
        const val DETAIL_RELATED_PAGE = 2
    }

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString(Constants.MOTO_ID_KEY, idMoto)

        when (position) {
            DETAIL_CONTENT_PAGE -> {
                val detailContentFragment = DetailContentFragment()
                detailContentFragment.arguments = bundle
                return detailContentFragment
            }
            DETAIL_SPECS_PAGE -> {
                val detailSpecsFragment = DetailSpecsFragment()
                detailSpecsFragment.arguments = bundle
                return detailSpecsFragment
            }
            DETAIL_RELATED_PAGE -> {
                val detailRelatedFragment = DetailRelatedFragment()
                detailRelatedFragment.arguments = bundle
                return detailRelatedFragment
            }
            else -> {
                val detailContentFragment = DetailContentFragment()
                detailContentFragment.arguments = bundle
                return detailContentFragment
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }
}