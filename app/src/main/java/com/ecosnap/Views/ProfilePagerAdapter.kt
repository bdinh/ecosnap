package com.ecosnap.Views

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class ProfilePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private val ITEMS = 3

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return DayFragment().newInstance("test1")
            1 -> return DayFragment().newInstance("test2")
            else -> return DayFragment().newInstance("test3")
        }
//        return DayFragment().newInstance("test1")
    }

    override fun getCount(): Int {
        return ITEMS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return super.getPageTitle(position)
    }
}