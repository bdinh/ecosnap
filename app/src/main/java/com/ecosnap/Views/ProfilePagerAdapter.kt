package com.ecosnap.Views

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class ProfilePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private val ITEMS = 3

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return DayFragment().newInstance("Day")
            1 -> return WeekFragment().newInstance("Week")
            else -> return MonthFragment().newInstance("Month")
        }
//        return DayFragment().newInstance("test1")
    }

    override fun getCount(): Int {
        return ITEMS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Day"
            1 -> return "Week"
            else -> return "Month"
        }
//        return super.getPageTitle(position)
    }
}