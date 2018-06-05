package com.ecosnap.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.Model.History
import com.ecosnap.R
import com.ecosnap.Adapters.SectionHistoryRecyclerViewAdapter


class HistoryFragment : Fragment() {
    private var history: History? = null
    private var listener: OnHistoryFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            history = it.getSerializable("history") as History
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        arguments?.let {
            history = it.getSerializable("history") as History
        }

        val view = inflater.inflate(R.layout.history_view_list, container, false) as RecyclerView
        view.layoutManager = LinearLayoutManager(context)
        view.adapter = SectionHistoryRecyclerViewAdapter(context!!, this.history as History)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnHistoryFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun onHistoryDetailedView() {

    }

    interface OnHistoryFragmentInteractionListener {
    }
}
