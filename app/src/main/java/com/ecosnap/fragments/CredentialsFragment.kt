package com.ecosnap.fragments

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.ecosnap.Model.UserProfile
import com.ecosnap.R
import com.ecosnap.Views.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_credentials.view.*

class CredentialsFragment : Fragment() {
    private var listener: OnCredentialsFragmentInteractionListener? = null
    private lateinit var profile: UserProfile


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =  inflater!!.inflate(R.layout.fragment_credentials, container, false)
        setListener(view.creds_last_name)
        setListener(view.creds_Email)
        setListener(view.creds_Password)
        arguments?.let {
            profile = it.getSerializable("user") as UserProfile
        }
        initalizeCredentialsActivity(view)
        return view
    }

    private fun setListener(e: EditText) {
        e.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) {
            e.setSelection(e.text.length)
        } }
    }

    fun initalizeCredentialsActivity(view: View) {
        view.creds_last_name.setText(profile.lastName)
        view.creds_first_name.setText(profile.firstName)
        view.creds_Email.setText(profile.email)


        view.btn_Creds_Save.setOnClickListener {
            editCredentials(view)
            val intent = Intent(view.context, SettingsActivity::class.java)
            intent.putExtra("user", profile)
            view.context.startActivity(intent)
        }
        view.btn_Creds_Cancel.setOnClickListener {
            val intent = Intent(view.context, SettingsActivity::class.java)
            intent.putExtra("user", profile)
            view.context.startActivity(intent)
        }
    }

    fun editCredentials(view: View) {
        val fbAuth = FirebaseAuth.getInstance()
        val user = fbAuth.currentUser
        val db = FirebaseDatabase.getInstance()
        val userID = fbAuth.currentUser?.uid as String
        val profileRef = db.getReference("users").child(userID).child("profile")
        val email = view.creds_Email.getText().toString()
        val password = view.creds_Password.getText().toString()
        profile.firstName = view.creds_first_name.text.toString()
        profile.lastName = view.creds_last_name.text.toString()
        profile.email = email

        user?.updateEmail(profile.email)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Successfully updated user email", Toast.LENGTH_LONG).show()
            }
        }
        user?.updatePassword(password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Successfully update password", Toast.LENGTH_LONG).show()
            }
        }
        profileRef.child("firstName").setValue(profile.firstName)
        profileRef.child("lastName").setValue(profile.lastName)
        profileRef.child("email").setValue(profile.email)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnCredentialsFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCredentialsFragmentInteractionListener")
        }
    }

    interface OnCredentialsFragmentInteractionListener {}
}

