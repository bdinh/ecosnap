package com.ecosnap.Views

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.Model.Profile
import com.ecosnap.R
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {
    private val ITEMS = 5
    private lateinit var profile: Profile
//    private var name: String = ""
//    private var descr: String = ""
//    private var img: Int = 0
    private var listener: OnProfileFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profile = it.getSerializable("user") as Profile
//            name = it.getSerializable("name") as String
//            descr = it.getSerializable("descr") as String
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        arguments?.let {
            profile = it.getSerializable("user") as Profile
//            name = it.getSerializable("name") as String
//            descr = it.getSerializable("descr") as String
        }

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val mAdapter = ProfilePagerAdapter(fragmentManager)
        view.vp_profile.adapter = mAdapter
        view.tabs_profile.setupWithViewPager(view.vp_profile)
        view.txt_profile_title.text = profile.name //name
        view.txt_profile_bio.text = profile.descr //descr
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
