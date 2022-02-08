package com.funtease.practice.adapters

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.funtease.practice.R
import com.funtease.practice.fragments.HomeFragment
import com.google.android.material.progressindicator.CircularProgressIndicator


class ViewPagerAdapter( fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList: ArrayList<HomeFragment> = ArrayList()
    private val titleList: ArrayList<String> = ArrayList()

    fun addFragment(fragment: HomeFragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }

    fun getTitle(position: Int): String = titleList[position]

    fun getFragment(position: Int): HomeFragment = fragmentList[position]

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int): HomeFragment {
        return return fragmentList[position]
    }

}