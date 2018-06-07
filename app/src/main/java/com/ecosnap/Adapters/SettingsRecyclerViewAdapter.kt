package com.ecosnap.Adapters

import android.content.Intent
import android.view.View
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ecosnap.Model.UserProfile
import com.ecosnap.R
import com.ecosnap.Views.SettingsController
import kotlinx.android.synthetic.main.settings_view_item.view.*

class SettingsRecyclerViewAdapter(val settings: com.ecosnap.Model.Settings, val profile: UserProfile) : RecyclerView.Adapter<CustomSettingsViewItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomSettingsViewItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.settings_view_item, parent, false)
        return CustomSettingsViewItemHolder(cellForRow, "", profile)
    }

    override fun getItemCount(): Int {
        return settings.data.size
    }

    override fun onBindViewHolder(holder: CustomSettingsViewItemHolder, position: Int) {
        val item = settings.data.get(position)
        holder.view.settings_item_image_picture.setImageDrawable(item.img)
        holder.view.settings_item_title.setText(item.title)
        holder.title = item.title
    }

}

class CustomSettingsViewItemHolder(val view: View, var title: String, val profile: UserProfile) : RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener{
            val intent = Intent(view.context, SettingsController::class.java)
            intent.putExtra("title", title)
            intent.putExtra("user", profile)
            view.context.startActivity(intent)
        }
    }
}
