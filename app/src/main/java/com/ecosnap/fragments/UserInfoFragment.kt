package com.ecosnap.fragments

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest



class UserInfoFragment : Fragment() {
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateProfile()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    fun updateProfile() {
        val user = fbAuth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("")
//                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener(OnCompleteListener<Void> { task ->
                    if (task.isSuccessful) {

                    }
                })
    }
}
