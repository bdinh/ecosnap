package com.ecosnap.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.R
import kotlinx.android.synthetic.main.fragment_month.view.*

class MonthFragment : Fragment() {
    private var test = ""

    fun newInstance(str: String): MonthFragment {
        val monthFragment= MonthFragment()
        val args = Bundle()
        args.putString("month", str)
        monthFragment.arguments = args
        return monthFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        test = arguments?.getSerializable("month") as String
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_month, container, false)
        view.txt_profile_month.text = test
        return view
    }
}

