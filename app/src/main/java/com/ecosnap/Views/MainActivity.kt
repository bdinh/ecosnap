package com.ecosnap.Views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ecosnap.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import android.support.v4.content.ContextCompat
import android.support.annotation.ColorRes

class MainActivity : AppCompatActivity(), ProfileFragment.OnProfileFragmentInteractionListener {
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createBottomNav()

        initializeMainActivity()
    }

    fun createBottomNav() {
        val bottomNavigation = findViewById<View>(R.id.bottom_navigation) as AHBottomNavigation

        val item1 = AHBottomNavigationItem(R.string.bottomnav_title_0, R.drawable.ic_locate, R.color.navSelect)
        val item2 = AHBottomNavigationItem(R.string.bottomnav_title_1, R.drawable.ic_history, R.color.navSelect)
        val item3 = AHBottomNavigationItem(R.string.bottomnav_title_2, R.drawable.ic_camera, R.color.navSelect)
        val item4 = AHBottomNavigationItem(R.string.bottomnav_title_3, R.drawable.ic_profile, R.color.navSelect)

        // Add items
        bottomNavigation.addItem(item1)
        bottomNavigation.addItem(item2)
        bottomNavigation.addItem(item3)
        bottomNavigation.addItem(item4)

        bottomNavigation.currentItem = 0
        bottomNavigation.defaultBackgroundColor = fetchColor(R.color.colorPrimary)
        bottomNavigation.accentColor = fetchColor(R.color.navSelect)
        bottomNavigation.inactiveColor = fetchColor(R.color.navUnselect)

        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            when(position) {
                3 -> initProfileFragment()
            }

            // Do something cool here...
            true
        }
    }

    fun initProfileFragment() {
        val args = Bundle()
        args.putString("name", "Aaron Nguyen")
        args.putString("descr", "Recycling newbie here! It’s about time that I started giving a shit.")
        val profileFragment = ProfileFragment()
        profileFragment.arguments = args
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, profileFragment)
        transaction.commit()
    }

    fun fetchColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(this, color)
    }

    fun initializeMainActivity() {
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
