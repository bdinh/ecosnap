package com.ecosnap.Views


import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.Controller.RowAdapter
import com.ecosnap.R
import kotlinx.android.synthetic.main.fragment_history.view.*

class HistoryFragment : Fragment() {

    lateinit var activityCommander: HistoryListener

    interface HistoryListener {
        fun beginHistoryView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_history, container, false)
        Log.i("fragment", "created!!")

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.i("fragment", "attatched!!")
        try {
            activityCommander = context as HistoryListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString())
        }
    }

}


