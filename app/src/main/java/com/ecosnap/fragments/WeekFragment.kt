package com.ecosnap.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.R
import kotlinx.android.synthetic.main.fragment_week.view.*

class WeekFragment : Fragment() {
    private var test = ""

    fun newInstance(str: String): WeekFragment {
        val weekFragment= WeekFragment()
        val args = Bundle()
        args.putString("week", str)
        weekFragment.arguments = args
        return weekFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        test = arguments?.getSerializable("week") as String
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_week, container, false)
        view.txt_profile_week.text = test
        return view
    }
}
