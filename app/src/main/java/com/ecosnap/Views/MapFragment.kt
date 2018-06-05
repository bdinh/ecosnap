package com.ecosnap.Views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.Controller.GetNearbyRecycleCenters
import com.ecosnap.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


private const val LOCATION_PERMISSION_REQUEST_CODE = 1
private const val REQUEST_CHECK_SETTINGS = 2
private const val PROXIMITY_RADIUS = 10000
private const val API_KEY = "AIzaSyBLgt9kCOa6jnHzUVhZuVU6x4J3COAt80Q"
private const val NEARBY_PLACE = "recycling+center"


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private var listener: OnFragmentInteractionListener? = null
    private var mapFragment: SupportMapFragment? = null
    private lateinit var thisActivity: Activity
    private lateinit var thisContext: Context
    private lateinit var map: GoogleMap
    private lateinit var fCL: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallBack: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private lateinit var currLocation: LatLng

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_map, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        if (mapFragment == null) {
            var fm = fragmentManager
            var ft = fm?.beginTransaction()
            mapFragment = SupportMapFragment.newInstance()
            ft?.replace(R.id.map, mapFragment)?.commit()
        }
        mapFragment?.getMapAsync(this)
        fCL = LocationServices.getFusedLocationProviderClient(thisActivity)
        locationCallBack = object : LocationCallback() {}
        createLocationRequest()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
            thisContext = context
            thisActivity = context as MainActivity
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(thisContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(thisActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            setUpMap()
        } else {
            map.isMyLocationEnabled = true
            // Removes +, - (zoom controls) since they don't fit properly inside fragment
            map.uiSettings.isZoomControlsEnabled = false

            fCL.lastLocation.addOnSuccessListener(thisActivity) {location ->
                if (location != null) {
                    lastLocation = location
                    var currentLatLng = LatLng(location.latitude, location.longitude)
                    currLocation = currentLatLng
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                    getNearByRecyclingCenters(currLocation)
                }
            }
            map.setOnCameraIdleListener {
                //Removed map.clear()
                //Map clear here causes instant map refresh on every little movement
                val screenCenter = map.cameraPosition.target
                getNearByRecyclingCenters(screenCenter)
            }
        }
    }

    override fun onMarkerClick(p0: Marker?) = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fCL.removeLocationUpdates(locationCallBack)
    }

    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

    private fun getNearByRecyclingCenters(location: LatLng) {
        var url = getUrl(location.latitude, location.longitude, NEARBY_PLACE)
        var dataTransfer = HashMap<Int, Any>()
        dataTransfer[0] = map
        dataTransfer[1] = url
        var getNearbyRCenters = GetNearbyRecycleCenters()
        getNearbyRCenters.execute(dataTransfer)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(thisContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(thisActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fCL.requestLocationUpdates(locationRequest, locationCallBack, null)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(thisActivity)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener {e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(MainActivity(), REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    //Ignore the error
                }
            }
        }
    }

    private fun getUrl(lat: Double, lng: Double, nearbyPlace: String): String {
        var googlePlacesUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlacesUrl.append("location=" + lat + "," + lng)
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS)
        googlePlacesUrl.append("&keyword=" + nearbyPlace)
        googlePlacesUrl.append("&sensor=true")
        googlePlacesUrl.append("&key=" + API_KEY)
        return (googlePlacesUrl.toString())
    }

    interface OnFragmentInteractionListener {}
}
