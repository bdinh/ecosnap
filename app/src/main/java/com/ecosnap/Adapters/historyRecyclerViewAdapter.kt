package com.ecosnap.Adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ecosnap.Model.DateHistory
import com.ecosnap.R
import kotlinx.android.synthetic.main.history_view_item.view.*
import java.io.File

class HistoryRecyclerViewAdapter(val historyData: DateHistory) : RecyclerView.Adapter<CustomHistoryViewItemHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHistoryViewItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.history_view_item, parent, false)
        this.context = parent.context
        return CustomHistoryViewItemHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return historyData.historyList.size
    }

    override fun onViewRecycled(holder: CustomHistoryViewItemHolder) {
        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: CustomHistoryViewItemHolder, position: Int) {
        val historyItem = historyData.historyList.get(position)
        if (historyItem.type == "recyclable") {
            holder.history_item_image_check.setImageResource(R.drawable.ic_pass)
            holder.history_item_text_name.setText(context.resources.getString(R.string.recyclable))
            holder.history_item_image_picture.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))

        } else {
            holder.history_item_image_check.setImageResource(R.drawable.ic_reject)
            holder.history_item_text_name.setText(context.resources.getString(R.string.nonrecyclable))
            holder.history_item_image_picture.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
        }
        val file = File(historyItem.imgPath)
        if (file.exists()) {
            val bitMap = BitmapFactory.decodeFile(file.absolutePath)
            val requestOption = RequestOptions()
            requestOption.fitCenter()
            requestOption.placeholder(R.drawable.ic_ecosnap_icon_hdpi)
            Glide.with(context).asBitmap().load(bitMap).apply(requestOption).into(holder.history_item_image_picture)
//            holder.history_item_image_picture.setImageBitmap(bitMap)
        }
        val formattedFloat = "%.2f".format(historyItem.confidence) + "%"
        holder.history_item_text_percentage.setText(formattedFloat)
    }

}

class CustomHistoryViewItemHolder(val view: View) :RecyclerView.ViewHolder(view) {
    val history_item_image_picture: ImageView = view.history_item_image_picture
    val history_item_text_name: TextView = view.history_item_text_name
    val history_item_text_percentage: TextView = view.history_item_text_percentage
    val history_item_image_check: ImageView = view.history_item_image_check
}