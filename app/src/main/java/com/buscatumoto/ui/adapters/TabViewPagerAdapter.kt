package com.buscatumoto.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabViewPageAdapter(activity: FragmentActivity):
    FragmentStateAdapter(activity) {
    val fragmentList:MutableList<Fragment> = ArrayList()

//    override fun getItem(position: Int): Fragment {
//        return fragmentList.get(position)
//    }


//    override fun getPageTitle(position: Int): CharSequence? {
//        return fragmentTitleList.get(position)
//    }

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}