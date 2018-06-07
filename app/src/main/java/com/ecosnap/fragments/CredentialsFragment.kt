package com.ecosnap.fragments

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ecosnap.model.UserProfile
import com.ecosnap.R
import com.ecosnap.activity.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_credentials.*
import kotlinx.android.synthetic.main.fragment_credentials.view.*

class CredentialsFragment : Fragment() {
    private var listener: OnCredentialsFragmentInteractionListener? = null
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var userID: String
    private lateinit var profileRef: DatabaseReference
    private lateinit var dataRef: DatabaseReference
    private lateinit var profile: UserProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =  inflater!!.inflate(R.layout.fragment_credentials, container, false)
        initalizeCredentialsActivity(view)
        return view
    }

    fun initalizeCredentialsActivity(view: View) {
        fbAuth = FirebaseAuth.getInstance()
        val user = fbAuth.currentUser
        view.creds_Email.setText(user?.email)
        view.btn_Creds_Save.setOnClickListener {
            editCredentials(user, view)
            val intent = Intent(view.context, SettingsActivity::class.java)
            view.context.startActivity(intent)
        }
        view.btn_Creds_Cancel.setOnClickListener {
            val intent = Intent(view.context, SettingsActivity::class.java)
            view.context.startActivity(intent)
        }
        db = FirebaseDatabase.getInstance()
        userID = fbAuth.currentUser?.uid as String
        profileRef = db.getReference("users").child(userID).child("profile")
        profileRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    profile = dataSnapshot.getValue(UserProfile::class.java) as UserProfile
                    view.creds_last_name.setText(profile.lastName)
                    view.creds_first_name.setText(profile.firstName)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.i("ECOSNAP FIREBASE", "firebase database retrieving profile error: " + p0.toString())
            }
        })

    }

    fun editCredentials(user: FirebaseUser?, view: View) {
        val email = creds_Email.getText().toString()
        val password = creds_Password.getText().toString()
        user?.updateEmail(email)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Successfully updated user email", Toast.LENGTH_LONG).show()
            }
        }
        if (password != "******") {
            user?.updatePassword(password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Successfully update password", Toast.LENGTH_LONG).show()
                }
            }
        }
        profileRef.child("firstName").setValue(view.creds_first_name.getText().toString())
        profileRef.child("lastName").setValue(view.creds_last_name.getText().toString())
        profileRef.child("email").setValue(view.creds_Email.getText().toString())
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnCredentialsFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCredentialsFragmentInteractionListener")
        }
    }

    interface OnCredentialsFragmentInteractionListener {
    }

}

