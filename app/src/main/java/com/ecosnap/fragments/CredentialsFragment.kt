package com.ecosnap.fragments

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ecosnap.R
import com.ecosnap.Views.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_credentials.*
import kotlinx.android.synthetic.main.fragment_credentials.view.*

class CredentialsFragment : Fragment() {
    private var listener: OnCredentialsFragmentInteractionListener? = null
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =  inflater!!.inflate(R.layout.fragment_credentials, container, false)
        fbAuth = FirebaseAuth.getInstance()
        val user = fbAuth.currentUser
        view.creds_Email.setText(user?.email)
        view.btn_Creds_Save.setOnClickListener {
            editCredentials(user)
            val intent = Intent(view.context, SettingsActivity::class.java)
            view.context.startActivity(intent)
        }
        view.btn_Creds_Cancel.setOnClickListener {
            val intent = Intent(view.context, SettingsActivity::class.java)
            view.context.startActivity(intent)
        }
        return view
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

    fun editCredentials(user: FirebaseUser?) {
        val email = creds_Email.getText().toString()
        val password = creds_Password.getText().toString()
        user?.updateEmail(email)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Successfully updated user email", Toast.LENGTH_LONG).show()
            }
        }
        user?.updatePassword(password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Successfully update password", Toast.LENGTH_LONG).show()
            }
        }
    }

    interface OnCredentialsFragmentInteractionListener {
        fun onCredentialsSelected()
    }

}

