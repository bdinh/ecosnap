package com.ecosnap.Views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ecosnap.R
import com.ecosnap.fragments.CredentialsFragment
import com.google.firebase.auth.FirebaseAuth

class SettingsController: AppCompatActivity(), CredentialsFragment.OnCredentialsFragmentInteractionListener {

    override fun onCredentialsSelected() {

    }

    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        fbAuth = FirebaseAuth.getInstance()
        val title = intent.getSerializableExtra("title") as String
        when (title) {
            "General" -> {
                val credentialsFragment = CredentialsFragment()
                credentialsFragment.arguments = intent.extras
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.settings_frame, credentialsFragment)
                transaction.commit()
            }
            "Security" -> {

            }
            "Logout" -> {
                this.handleSignOut(fbAuth)
            }
        }
    }

    fun handleSignOut(fbAuth: FirebaseAuth) {
        fbAuth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}