package com.buscatumoto.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.buscatumoto.ui.fragments.DetailContentFragment
import com.buscatumoto.ui.fragments.DetailRelatedFragment
import com.buscatumoto.ui.fragments.DetailSpecsFragment
import com.buscatumoto.ui.models.MotoDetailUi
import com.buscatumoto.utils.global.Constants


class DetailViewPagerAdapter(private val detailUi: MotoDetailUi?, private val idMoto: String?, fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val DETAIL_CONTENT_PAGE = 0
        const val DETAIL_RELATED_PAGE = 1
    }

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString(Constants.MOTO_ID_KEY, idMoto)
        bundle.putParcelable(Constants.MOTO_DETAIL_UI_KEY, detailUi)

        when (position) {
            DETAIL_CONTENT_PAGE -> {
                val detailContentFragment = DetailContentFragment()
                detailContentFragment.arguments = bundle
                return detailContentFragment
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
        return 2
    }
}