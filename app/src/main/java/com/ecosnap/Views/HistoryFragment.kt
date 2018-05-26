package com.ecosnap.Views


import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.R

class HistoryFragment : Fragment() {

    lateinit var activityCommander: HistoryListener

    interface HistoryListener {
        fun beginHistoryView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_history, container, false)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCommander = context as HistoryListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString())
        }
    }

}


