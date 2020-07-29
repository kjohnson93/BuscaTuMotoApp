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


class DetailViewPagerAdapter(private val detailUi: MotoDetailUi?, private val idMoto: String?,
                             fragmentManager: FragmentManager):
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentList: MutableList<Fragment> = ArrayList()
    private var fragmentTitleList : MutableList<String> = ArrayList()

    override fun getPageTitle(position: Int): CharSequence? {

        return fragmentTitleList[position]
    }

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString(Constants.MOTO_ID_KEY, idMoto)
        bundle.putParcelable(Constants.MOTO_DETAIL_UI_KEY, detailUi)

        val fragment = fragmentList[position]
        fragment.arguments = bundle

        return fragment
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }
}