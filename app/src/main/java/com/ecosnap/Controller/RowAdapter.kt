package com.ecosnap.Controller

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.R
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.recycleview_row.view.*

class RowAdapter(context: Context, row: Array<Date>): RecyclerView.Adapter<RowAdapter.RowViewHolder>() {
    private val rowInflater = LayoutInflater.from(context)
    private val mRow: Array<Date> = row
    private val mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowAdapter.RowViewHolder {
        val view = rowInflater.inflate(R.layout.recycleview_row, parent, false)
        return RowViewHolder(mContext, view)
    }

    override fun getItemCount(): Int {
        return mRow.size
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val title = mRow.get(position)
        holder.itemView.txtDate.text = title.date
    }

    class RowViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            super.itemView
            val item1 = Item("Plastic", 90, true, "https://4.imimg.com/data4/EU/YB/MY-6282801/normal-plastic-bottle-500x500.jpg")
            val item2 = Item("Glass", 80, true, "https://4.imimg.com/data4/EU/YB/MY-6282801/normal-plastic-bottle-500x500.jpg")
            itemView.recyclerView_Items.layoutManager = GridLayoutManager(context, 2)
            itemView.recyclerView_Items.adapter = GridAdapter(context, arrayOf(item1, item2))
        }
    }
}