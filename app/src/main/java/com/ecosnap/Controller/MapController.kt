package com.ecosnap.Controller

import android.app.Activity
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices

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