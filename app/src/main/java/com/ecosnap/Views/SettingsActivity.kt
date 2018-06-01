package com.ecosnap.Views

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.ecosnap.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class SettingsActivity: AppCompatActivity() {
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeSettingsActivity()
    }


    fun initializeSettingsActivity() {
        fbAuth = FirebaseAuth.getInstance()
        btnLogout_M.setOnClickListener {
            handleSignOut()
        }
    }

    fun handleSignOut() {
        fbAuth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}