package com.buscatumoto.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.buscatumoto.ui.models.MotoDetailUi
import com.buscatumoto.utils.global.MOTO_ID_KEY


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
        bundle.putString(MOTO_ID_KEY, idMoto)

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