package com.ecosnap.fragments

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_credentials.*

class CredentialsFragment : Fragment() {
    private var listener: OnCredentialsFragmentInteractionListener? = null
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fbAuth = FirebaseAuth.getInstance()
        val user = fbAuth.currentUser
        creds_Email.setText(user?.email)
        btn_Creds_Save.setOnClickListener {
            editCredentials(user)
        }
        btn_Creds_Cancel.setOnClickListener {

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_credentials, container, false)
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
//                Toast.makeText(this, "Successfully updated user email", Toast.LENGTH_LONG).show()
            }
        }
        user?.updatePassword(password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                Toast.makeText(this, "Successfully update password", Toast.LENGTH_LONG).show()
            }
        }
    }

    interface OnCredentialsFragmentInteractionListener {
        fun onCredentialsSelected()
    }

}

