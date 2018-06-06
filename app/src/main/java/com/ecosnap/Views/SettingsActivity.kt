package com.ecosnap.Views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.ecosnap.Adapters.SettingsRecyclerViewAdapter
import com.ecosnap.Model.Settings
import com.ecosnap.Model.SettingsItem
import com.ecosnap.R
import com.ecosnap.fragments.CredentialsFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity: AppCompatActivity(), CredentialsFragment.OnCredentialsFragmentInteractionListener {

    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        fbAuth = FirebaseAuth.getInstance()
        initializeSettingsActivity(fbAuth)
        val credentials = SettingsItem("Credentials", 1)
        val settings = Settings(arrayOf(credentials))
        settings_recycler_view.layoutManager = LinearLayoutManager(this)
        settings_recycler_view.adapter = SettingsRecyclerViewAdapter(settings)
    }

    override fun onCredentialsSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun initializeSettingsActivity(fbAuth: FirebaseAuth) {
        btnLogout.setOnClickListener {
            handleSignOut(fbAuth)
        }
    }

    fun handleSignOut(fbAuth: FirebaseAuth) {
        fbAuth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}