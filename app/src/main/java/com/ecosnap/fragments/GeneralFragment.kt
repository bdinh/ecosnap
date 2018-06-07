package com.ecosnap.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.Controller.fbDatabase.insertNewUserIntoDatabase
import com.ecosnap.Model.UserProfile

import com.ecosnap.R
import com.ecosnap.Views.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_general.*
import kotlinx.android.synthetic.main.fragment_general.view.*

class GeneralFragment : Fragment() {
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var userID: String
    private var SELECTED_PICTURE: Int? = null
    private lateinit var imgUri: Uri
    private lateinit var profile: UserProfile
    private lateinit var profileRef: DatabaseReference
    private var listener: OnGeneralFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profile = it.getSerializable("user") as UserProfile
            SELECTED_PICTURE = 100
            fbAuth = FirebaseAuth.getInstance()
            db = FirebaseDatabase.getInstance()
            userID = fbAuth.currentUser?.uid as String
            profileRef = db.getReference("users").child(userID).child("profile")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_general, container, false)
        arguments?.let {
            profile = it.getSerializable("user") as UserProfile
            SELECTED_PICTURE = 100
            fbAuth = FirebaseAuth.getInstance()
            db = FirebaseDatabase.getInstance()
            userID = fbAuth.currentUser?.uid as String
            profileRef = db.getReference("users").child(userID).child("profile")
        }
        view.bio_general.setText(profile.descr)
        view.setting_General_IMG.setImageURI(Uri.parse(profile.imgpath))
        view.button_general.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECTED_PICTURE!!)
        }
        view.btn_save_general.setOnClickListener {
            insertNewUserIntoDatabase(db, profile.firstName, profile.lastName, profile.email, userID,
                    bio_general.text.toString(), imgUri.toString())

            val intent = Intent(view.context, SettingsActivity::class.java)
            intent.putExtra("user", profile)
            view.context.startActivity(intent)
        }
        view.btn_cancel_general.setOnClickListener {
            val intent = Intent(view.context, SettingsActivity::class.java)
            intent.putExtra("user", profile)
            view.context.startActivity(intent)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == SELECTED_PICTURE) {
            imgUri = data.data
            profile.imgpath = imgUri.toString()
            setting_General_IMG.setImageURI(imgUri)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnGeneralFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnGeneralFragmentInteractionListener {}
}
