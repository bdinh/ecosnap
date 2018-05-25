package com.ecosnap.Views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ecosnap.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initializeSignUpActivity()
    }



    fun initializeSignUpActivity() {
        fbAuth = FirebaseAuth.getInstance()
        btnSignUp_SU.setOnClickListener {
            handleSignUp()
        }
    }

    fun handleSignUp() {
        var email = email_SU.text.toString()
        val password = password_SU.text.toString()
        val passwordConf = passwordConf_SU.text.toString()

        if (password == passwordConf) {
            fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, {task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("id", fbAuth.currentUser?.email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "error: " + task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                })
        }
    }
}
