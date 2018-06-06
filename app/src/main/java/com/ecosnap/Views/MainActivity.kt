package com.ecosnap.Views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import android.support.v4.content.ContextCompat
import android.support.annotation.ColorRes
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.ecosnap.*
import com.ecosnap.Controller.fbDatabase.insertHistoryItem
import com.ecosnap.Model.*
import com.ecosnap.R
import com.ecosnap.fragments.CameraFragment
import com.ecosnap.fragments.HistoryFragment
import com.ecosnap.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ProfileFragment.OnProfileFragmentInteractionListener, MapFragment.OnFragmentInteractionListener,
        HistoryFragment.OnHistoryFragmentInteractionListener, CameraFragment.OnCameraFragmentInteractionListener {
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var userID: String
    private lateinit var profileRef: DatabaseReference
    private lateinit var dataRef: DatabaseReference
    private lateinit var profile: UserProfile
    private var dbData = mutableListOf<DateHistory>()

    override fun onCaptureButton() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createBottomNav()
        initializeMainActivity()
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        btnLogout_M.visibility = View.INVISIBLE
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

    fun initLocateFragment() {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.frame, MapFragment())
        transaction.commit()
    }

    fun initProfileFragment() {
        val profileExample = Profile("Aaron Nguyen", "Recycling newbie here! It’s about time that I started to care.", 0)
        val args = Bundle()
        args.putSerializable("user", profileExample)
        val profileFragment = ProfileFragment()
        profileFragment.arguments = args
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.frame, profileFragment)
        transaction.commit()
    }

    fun initCameraFragment() {
        val args = Bundle()
        args.putString("userID", this.userID)
        val cameraFragment = CameraFragment()
        cameraFragment.arguments = args
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.frame, cameraFragment)
        transaction.commit()
    }

    fun initHistoryFragment() {
        val history = History(this.dbData)
        println(this.dbData)
//        val historyItem_1 = HistoryItem("Soda Can", R.drawable.sodacan, "74%", R.drawable.ic_pass)
//        val historyItem_2 = HistoryItem("Glass Bottle", R.drawable.glassbottle, "87%", R.drawable.ic_pass)
//        val historyItem_3 = HistoryItem("Stuffed Animal", R.drawable.teddybear, "91%", R.drawable.ic_reject)
//        val dateHistory_1 = DateHistory("Today", arrayOf(historyItem_1, historyItem_2, historyItem_3))
//        val dateHistory_2 = DateHistory("Yesterday", arrayOf(historyItem_1, historyItem_2, historyItem_3))
//        val history = History(arrayOf(dateHistory_1, dateHistory_2))
        val args = Bundle()
        args.putSerializable("history", history)
        val historyFragment = HistoryFragment()
        historyFragment.arguments = args
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.frame, historyFragment)
        transaction.commit()
    }

    fun fetchColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(this, color)
    }

    fun initializeMainActivity() {
        fbAuth = FirebaseAuth.getInstance()
        userID = fbAuth.currentUser?.uid as String
        db = FirebaseDatabase.getInstance()
        profileRef = db.getReference("users").child(userID).child("profile")
        dataRef = db.getReference("users").child(userID).child("data")

        profileRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    profile = dataSnapshot.getValue(UserProfile::class.java) as UserProfile
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.i("ECOSNAP FIREBASE", "firebase database retrieving profile error: " + p0.toString())
            }
        })

        dataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dbData.clear()
                    dataSnapshot.children.forEach {
                        val key = it.key as String
                        val items: MutableList<dbHistoryItem> = mutableListOf()
                        val map = it.value as Map<String, Map<String, Any>>
                        map.forEach {
                            val item = it.value
                            val type = item.get("type") as String
                            var confidence = item.get("confidence")
                            val datetime = item.get("datetime") as String
                            val imgpath = item.get("imgPath") as String
                            val historyItem = dbHistoryItem(type, confidence.toString().toFloat(), datetime, imgpath)
                            items.add(historyItem)
                        }
                        items.sortWith(compareBy({it.datetime}))
                        items.reverse()
                        val dh = DateHistory(key, items)
                        dbData.add(dh)
                    }
                }
                dbData.reverse()
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.i("ECOSNAP FIREBASE", "firebase database retrieving history item error: " + p0.toString())
            }
        })

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

