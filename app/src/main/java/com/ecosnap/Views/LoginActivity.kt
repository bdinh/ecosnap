package com.ecosnap.Views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ecosnap.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fbAuth = FirebaseAuth.getInstance()
        handleUserIsSignedIn(fbAuth.currentUser)
        initializeLoginActivity()
    }



    fun initializeLoginActivity() {
        fbAuth = FirebaseAuth.getInstance()
        btnLogin_LI.setOnClickListener {
            val email = email_LI.text.toString()
            val password = password_LI.text.toString()
            if (email != "" && password != "") {
                handleLogin(email_LI.text.toString(), password_LI.text.toString())
            }
        }
        toSignup_LI.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    fun handleUserIsSignedIn(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("id", fbAuth.currentUser?.email)
            startActivity(intent)
        }
    }

    fun handleLogin(email: String, password: String) {
        fbAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, {task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", fbAuth.currentUser?.uid)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "error: " + task.exception?.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}
