package com.ecosnap.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.ecosnap.Model.ProfileChartData
import com.ecosnap.fragments.DayFragment
import com.ecosnap.fragments.MonthFragment
import com.ecosnap.fragments.WeekFragment

class ProfilePagerAdapter(fm: FragmentManager?, profileData: ProfileChartData) : FragmentStatePagerAdapter(fm) {
    private val ITEMS = 3
    val data = profileData
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return DayFragment().newInstance("Day", data.dayData)
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