package com.ecosnap.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ecosnap.controller.fbDatabase.insertNewUserIntoDatabase
import com.ecosnap.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initializeSignUpActivity()
    }

    override fun onResume() {
        super.onResume()
        fbAuth = FirebaseAuth.getInstance()
        handleUserIsSignedIn(fbAuth.currentUser)
    }

    fun initializeSignUpActivity() {
        fbAuth = FirebaseAuth.getInstance()
        btnSignUp_SU.setOnClickListener {
            handleSignUp()
        }
        toLogin_SU.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun handleSignUp() {
        val firstName = fName_SU.text.toString()
        val lastName = lName_SU.text.toString()
        val email = email_SU.text.toString()
        val password = password_SU.text.toString()
        val passwordConf = passwordConf_SU.text.toString()

        if (checkSignupFields(firstName, lastName, email, password, passwordConf)) {
            fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, {task ->
                    if (task.isSuccessful) {
                        val db = FirebaseDatabase.getInstance()
                        val userID = fbAuth.currentUser?.uid as String
                        insertNewUserIntoDatabase(db, firstName, lastName, email, userID, "", "")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "error: " + task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    fun checkSignupFields(firstName: String, lastName: String, email: String, password: String, passwordConf: String): Boolean {
        if (firstName == "" || lastName == "" || email == "" || password == "" || passwordConf == "" || password != passwordConf) {
            return false
        }
        return true
    }

    fun handleUserIsSignedIn(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
