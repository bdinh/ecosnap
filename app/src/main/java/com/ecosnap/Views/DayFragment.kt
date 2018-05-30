package com.ecosnap.Views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.R
import kotlinx.android.synthetic.main.fragment_day.view.*

class DayFragment : Fragment() {
    private var test = ""

    fun newInstance(str: String): DayFragment {
        val dayFragment= DayFragment()
        val args = Bundle()
        args.putString("day", str)
        dayFragment.arguments = args
        return dayFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        test = arguments?.getSerializable("day") as String
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        view.txt_day.text = test
        return view
    }


}
