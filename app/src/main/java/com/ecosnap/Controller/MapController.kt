package com.ecosnap.Controller

import android.app.Activity
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap

fun CheckGooglePlayServices(ctx: Context, activity: Activity): Boolean {
    val googleAPI = GoogleApiAvailability.getInstance()
    val result: Int = googleAPI.isGooglePlayServicesAvailable(ctx)
    if (result != ConnectionResult.SUCCESS) {
        if (googleAPI.isUserResolvableError(result)) {
            googleAPI.getErrorDialog(activity, result, 0).show()
        }
        return false
    }
    return true
}

@Synchronized
fun buildGoogleApiClient(ctx: Context, connCB: GoogleApiClient.ConnectionCallbacks, onConnFailListener: GoogleApiClient.OnConnectionFailedListener) {
    val mGoogleApiClient = GoogleApiClient.Builder(ctx)
            .addConnectionCallbacks(connCB)
            .addOnConnectionFailedListener(onConnFailListener)
            .addApi(LocationServices.API)
            .build()
    mGoogleApiClient.connect()
}

//fun getNearbyRecycleCneters(map: GoogleMap) {
//    map.clear()
//    val recycleCenters = "Recycle Centers"
//    val url: String = getUrl(latitude, longitude, recycleCenters)
//    val DataTransfer = []
//    DataTransfer[0] = map
//    DataTransfer[1] = url
//    val nearbyRecycleCenters = GetNearByPlacesData()
//}