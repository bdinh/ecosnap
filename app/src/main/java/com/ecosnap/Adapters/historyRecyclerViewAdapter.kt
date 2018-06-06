package com.ecosnap.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ecosnap.Model.DateHistory
import com.ecosnap.R
import kotlinx.android.synthetic.main.history_view_item.view.*

class HistoryRecyclerViewAdapter(val historyData: DateHistory) : RecyclerView.Adapter<CustomHistoryViewItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHistoryViewItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.history_view_item, parent, false)
        return CustomHistoryViewItemHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return historyData.historyList.size
    }

    override fun onBindViewHolder(holder: CustomHistoryViewItemHolder, position: Int) {
//        val historyItem = historyData.historyList.get(position)
//        holder.history_item_text_name.setText(historyItem.name)
//        holder.history_item_image_picture.setImageResource(historyItem.src)
//        holder.history_item_text_percentage.setText(historyItem.percentage)
//        holder.history_item_image_check.setImageResource(historyItem.isRecyclable)
    }

}

class CustomHistoryViewItemHolder(val view: View) :RecyclerView.ViewHolder(view) {
    val history_item_image_picture: ImageView = view.history_item_image_picture
    val history_item_text_name: TextView = view.history_item_text_name
    val history_item_text_percentage: TextView = view.history_item_text_percentage
    val history_item_image_check: ImageView = view.history_item_image_check
}