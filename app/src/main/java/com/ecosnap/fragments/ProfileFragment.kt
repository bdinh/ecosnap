package com.ecosnap.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.Model.Profile
import com.ecosnap.R
import com.ecosnap.Adapters.ProfilePagerAdapter
import com.ecosnap.Model.Settings
import com.ecosnap.Views.SettingsActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {
    private val ITEMS = 5
    private lateinit var profile: Profile
    private var listener: OnProfileFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profile = it.getSerializable("user") as Profile
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        arguments?.let {
            profile = it.getSerializable("user") as Profile
        }

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.btnSettings.setOnClickListener {
            val intent = Intent(view.context, SettingsActivity::class.java)
            view.context.startActivity(intent)
        }
        val mAdapter = ProfilePagerAdapter(fragmentManager)
        view.vp_profile.adapter = mAdapter
        view.tabs_profile.setupWithViewPager(view.vp_profile)
        view.txt_profile_title.text = profile.name
        view.txt_profile_bio.text = profile.descr
        // view.img_profile_pict.setImageDrawable(profile.img)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProfileFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnProfileFragmentInteractionListener {
    }
}
