package com.ecosnap.adapters

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ecosnap.model.History
import com.ecosnap.R
import kotlinx.android.synthetic.main.section_history_view_list.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class SectionHistoryRecyclerViewAdapter(val context: Context, val historyData: History) : RecyclerView.Adapter<CustomSectionHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomSectionHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.section_history_view_list, parent, false)
        return CustomSectionHistoryViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return this.historyData.data.size
    }

    override fun onBindViewHolder(holder: CustomSectionHistoryViewHolder, position: Int) {
        val dateHistory = historyData.data.get(position)
        val dateString = dateHistory.label
        if (dateString == SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())) {
            holder.section_history_text_label.setText("Today")
        } else {
            val p = PrettyTime()
            val format = SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)
            val date = format.parse(dateString)
            val prettyDate = p.format(date)
            holder.section_history_text_label.setText(prettyDate)
        }
        holder.section_history_recycler_view.layoutManager = GridLayoutManager(this.context, 2)
        holder.section_history_recycler_view.adapter = HistoryRecyclerViewAdapter(dateHistory)
    }

}

class CustomSectionHistoryViewHolder(val view: View) :RecyclerView.ViewHolder(view) {
    val section_history_text_label: TextView = view.section_history_text_label
    val section_history_recycler_view :RecyclerView = view.section_history_recycler_view
}