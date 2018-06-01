package com.ecosnap.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.ecosnap.fragments.DayFragment
import com.ecosnap.fragments.MonthFragment
import com.ecosnap.fragments.WeekFragment

class ProfilePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private val ITEMS = 3

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return DayFragment().newInstance("Day")
            1 -> return WeekFragment().newInstance("Week")
            else -> return MonthFragment().newInstance("Month")
        }
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