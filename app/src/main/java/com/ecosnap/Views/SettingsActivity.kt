package com.ecosnap.Views

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.ecosnap.Adapters.SettingsRecyclerViewAdapter
import com.ecosnap.Model.Settings
import com.ecosnap.Model.SettingsItem
import com.ecosnap.R
import com.ecosnap.fragments.CredentialsFragment
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity: AppCompatActivity(), CredentialsFragment.OnCredentialsFragmentInteractionListener {

//    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
//        fbAuth = FirebaseAuth.getInstance()
        val credentials = SettingsItem("Credentials", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_credentials, null)!!)
        val logout = SettingsItem("Logout", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_logout, null)!!)
        val settings = Settings(arrayOf(credentials, logout))

        settings_recycler_view.layoutManager = LinearLayoutManager(this)
        settings_recycler_view.adapter = SettingsRecyclerViewAdapter(settings)
//        initializeSettingsActivity(fbAuth)
    }

    override fun onCredentialsSelected() {

    }


//    fun initializeSettingsActivity(fbAuth: FirebaseAuth) {
//        settings_Logout.setOnClickListener {
//            handleSignOut(fbAuth)
//        }
//    }
//
//    fun handleSignOut(fbAuth: FirebaseAuth) {
//        fbAuth.signOut()
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
//    }
}