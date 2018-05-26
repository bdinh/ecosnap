package com.ecosnap.Views

import android.content.Intent
import android.graphics.Color
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
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.ecosnap.Controller.Date
import com.ecosnap.Controller.Item
import com.ecosnap.Controller.RowAdapter
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*

class MainActivity : AppCompatActivity(), HistoryFragment.HistoryListener {
    private lateinit var fbAuth: FirebaseAuth


    private val item1 = Item("Plastic", 90, true, "https://4.imimg.com/data4/EU/YB/MY-6282801/normal-plastic-bottle-500x500.jpg")
    private val item2 = Item("Glass", 80, true, "https://4.imimg.com/data4/EU/YB/MY-6282801/normal-plastic-bottle-500x500.jpg")
    private val date1 = Date("Today", arrayOf(item1, item2))
    private val date2 = Date("Yesterday", arrayOf(item1, item2))
    private val list = arrayOf(date1, date2)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createBottomNav()

//        recyclerView_History.layoutManager = LinearLayoutManager(this)
//        recyclerView_History.adapter = RowAdapter(this, list)
//


        initializeMainActivity()
    }

    override fun onResume() {
        super.onResume()
        recyclerView_History.setBackgroundColor(Color.BLUE)
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

        bottomNavigation.currentItem = 1
        bottomNavigation.defaultBackgroundColor = fetchColor(R.color.colorPrimary)
        bottomNavigation.accentColor = fetchColor(R.color.navSelect)
        bottomNavigation.inactiveColor = fetchColor(R.color.navUnselect)

        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            // Navbar views to be implemented...
            when (position) {
                // 0 -> beginLocateView()
                1 -> beginHistoryView()
                // 2 -> beginCameraView()
                // 3 -> beginProfileView()
            }
            true
        }
    }

    // bundle parcelable and pass into HistoryFragment
    override fun beginHistoryView() {
        val bundle = Bundle()
        bundle.putParcelableArray("list", list)
        val history = HistoryFragment()
        history.arguments = bundle
        val fragTrans = fragmentManager.beginTransaction()
        fragTrans.replace(R.id.frame, history).commit()
        Log.i("main", "test")


//        recyclerView_History.setBackgroundColor(Color.BLUE)
//        recyclerView_History.layoutManager = LinearLayoutManager(this)
//        recyclerView_History.adapter = RowAdapter(this, list)
    }

    private fun fetchColor(@ColorRes color: Int): Int {
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
