package com.ecosnap.Views

import android.app.Fragment
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import android.support.v4.content.ContextCompat
import android.support.annotation.ColorRes
import android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.ecosnap.Model.Profile
import com.ecosnap.*
import com.ecosnap.Model.DateHistory
import com.ecosnap.Model.History
import com.ecosnap.Model.HistoryItem
import com.ecosnap.R.id.btnLogout_M
import com.ecosnap.fragments.HistoryFragment
import com.ecosnap.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ProfileFragment.OnProfileFragmentInteractionListener, HistoryFragment.OnHistoryFragmentInteractionListener {
    private lateinit var fbAuth: FirebaseAuth
    private var backstackFlag = "curr"
  
    override fun onHistoryDetailedView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
  

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
                0 -> initLocateFragment()
                1 -> initHistoryFragment()
                2 -> initCameraFragment()
                3 -> initProfileFragment()
            }
            // Do something cool here...
            true
        }
    }

    fun initProfileFragment() {
        val profileExample = Profile("Aaron Nguyen", "Recycling newbie here! It’s about time that I started to care.", 0)
        val args = Bundle()
        args.putSerializable("user", profileExample)
        val profileFragment = ProfileFragment()
        profileFragment.arguments = args
        val fm = supportFragmentManager
        fm.popBackStack(backstackFlag, POP_BACK_STACK_INCLUSIVE)
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.frame, profileFragment)
        transaction.addToBackStack(backstackFlag)
        transaction.commit()
    }

    fun initCameraFragment() {

    }

    fun initHistoryFragment() {
        val historyItem_1 = HistoryItem("Soda Can", R.drawable.sodacan, "74%", R.drawable.ic_pass)
        val historyItem_2 = HistoryItem("Glass Bottle", R.drawable.glassbottle, "87%", R.drawable.ic_pass)
        val historyItem_3 = HistoryItem("Stuffed Animal", R.drawable.teddybear, "91%", R.drawable.ic_reject)
        val dateHistory_1 = DateHistory("Today", arrayOf(historyItem_1, historyItem_2, historyItem_3))
        val dateHistory_2 = DateHistory("Yesterday", arrayOf(historyItem_1, historyItem_2, historyItem_3))
        val history = History(arrayOf(dateHistory_1, dateHistory_2))
        val args = Bundle()
        args.putSerializable("history", history)
        val historyFragment = HistoryFragment()
        historyFragment.arguments = args
        val fm = supportFragmentManager
        fm.popBackStack(backstackFlag, POP_BACK_STACK_INCLUSIVE)
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.frame, historyFragment)
        transaction.addToBackStack(backstackFlag)
        transaction.commit()
    }

    fun initLocateFragment() {

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
