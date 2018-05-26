package com.ecosnap.Controller

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.R
import kotlinx.android.synthetic.main.recycleview_item.view.*

class GridAdapter(context: Context, item: Array<Item>): RecyclerView.Adapter<GridAdapter.GridViewHolder>() {
    private val gridInflater = LayoutInflater.from(context)
    private val mItem: Array<Item> = item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder{
        val view = gridInflater.inflate(R.layout.recycleview_item, parent, false)
        return GridViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mItem.size
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = mItem.get(position)
        holder.itemView.txtItemName.text = item.name
        holder.itemView.txtPercent.text = item.percent.toString()
    }

    class GridViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }
}