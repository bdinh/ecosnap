package com.ecosnap.Views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ecosnap.Model.UserProfile
import com.ecosnap.R
import com.ecosnap.fragments.CredentialsFragment
import com.ecosnap.fragments.GeneralFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsController: AppCompatActivity(), CredentialsFragment.OnCredentialsFragmentInteractionListener,
        GeneralFragment.OnGeneralFragmentInteractionListener {

    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settings_close.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        val userProfile = intent.getSerializableExtra("user") as UserProfile

        fbAuth = FirebaseAuth.getInstance()
        val title = intent.getSerializableExtra("title") as String
        val fm = fragmentManager
        when (title) {
            "General" -> {
                val generalFragment = GeneralFragment()
                val args = Bundle()
                args.putSerializable("user", userProfile)
                generalFragment.arguments = args
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.settings_frame, generalFragment)
                transaction.commit()
            }
            "Security" -> {
                val credentialsFragment = CredentialsFragment()
                credentialsFragment.arguments = intent.extras
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.settings_frame, credentialsFragment)
                transaction.commit()
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