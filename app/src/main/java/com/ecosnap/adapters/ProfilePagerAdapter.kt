package com.ecosnap.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.ecosnap.controller.getChartString
import com.ecosnap.model.ProfileChartData
import com.ecosnap.fragments.DayFragment
import com.ecosnap.fragments.MonthFragment
import com.ecosnap.fragments.WeekFragment

class ProfilePagerAdapter(fm: FragmentManager?, profileData: ProfileChartData) : FragmentStatePagerAdapter(fm) {
    private val ITEMS = 3
    val data = profileData
    val weekDataString = getChartString(data.weekData.weekData)
    val monthDataString = getChartString(data.monthData.monthData)
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return DayFragment().newInstance(data.dayData)
            1 -> return WeekFragment().newInstance(weekDataString)
            else -> return MonthFragment().newInstance(monthDataString)
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
    }
}