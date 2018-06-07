package com.ecosnap.Views

import android.content.Intent
import android.os.Bundle
import android.os.UserHandle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.ecosnap.Adapters.SettingsRecyclerViewAdapter
import com.ecosnap.Model.Settings
import com.ecosnap.Model.SettingsItem
import com.ecosnap.Model.UserProfile
import com.ecosnap.R
import com.ecosnap.fragments.CredentialsFragment
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity: AppCompatActivity(), CredentialsFragment.OnCredentialsFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val general = SettingsItem("General", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_person, null)!!)
        val security = SettingsItem("Security", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_credentials, null)!!)
        val logout = SettingsItem("Logout", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_logout, null)!!)
        val settings = Settings(arrayOf(general, security, logout))

        var userProfile = intent.getSerializableExtra("user")
        userProfile = userProfile as UserProfile
        settings_close.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
        settings_recycler_view.layoutManager = LinearLayoutManager(this)
        settings_recycler_view.adapter = SettingsRecyclerViewAdapter(settings, userProfile)
    }
}