package com.ecosnap.Controller

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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

class DataParser {
    fun parse(jsonData: String): List<HashMap<String, String>> {
        lateinit var jsonArray: JSONArray
        lateinit var jsonObj: JSONObject

        try {
            jsonObj = JSONObject(jsonData)
            jsonArray = jsonObj.getJSONArray("results")
            return getPlaces(jsonArray)
        } catch (e: JSONException) {
            Log.i("ECOSNAP.ERROR", e.toString())
            return emptyList()
        }
    }

    private fun getPlaces(jsonArray: JSONArray): List<HashMap<String, String>> {
        var placesCount = jsonArray.length()
        var placesList = ArrayList<HashMap<String, String>>()
        lateinit var placeMap: HashMap<String, String>

        for (i in 0..(placesCount - 1)) {
            try {
                placeMap = getPlace(jsonArray.get(i) as JSONObject)
                placesList.add(placeMap)
            } catch (e: JSONException) {
                Log.i("ECOSNAP.ERROR", e.toString())
            }
        }
        return placesList
    }

    private fun getPlace(googlePlaceJson: JSONObject): HashMap<String, String> {
        var googlePlaceMap = HashMap<String, String>()
        try {
            var placeName = "-NA-"
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name")
            }
            var vicinity = "-NA-"
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity")
            }
            var latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat")
            var longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng")
            var reference = googlePlaceJson.getString("reference")
            googlePlaceMap.put("place_name", placeName)
            googlePlaceMap.put("vicinity", vicinity)
            googlePlaceMap.put("lat", latitude)
            googlePlaceMap.put("lng", longitude)
            googlePlaceMap.put("reference", reference)
            return googlePlaceMap
        } catch (e: JSONException) {
            Log.i("ECOSNAP.ERROR", e.toString())
            return googlePlaceMap
        }
    }
}

class GetNearbyRecycleCenters: AsyncTask<Any, String, String>() {
    private lateinit var googlePlaceData: String
    private lateinit var map: GoogleMap
    private lateinit var url: String

    override fun doInBackground(vararg objects: Any): String {
        var dataObj = objects[0] as HashMap<Int, Any>
        var mapFromObj = dataObj[0]
        var urlFromObj = dataObj[1]
        Log.i("ECOSNAP.ERROR", urlFromObj.toString())
        map = mapFromObj as GoogleMap
        url = urlFromObj as String
        try {
            val downloadUrl = DownloadUrl()
            googlePlaceData = downloadUrl.readUrl(url)
        } catch (e: Exception) {
            Log.i("ECOSNAP.ERROR Line 120", e.toString())
        }
        return googlePlaceData
    }

    override fun onPostExecute(result: String) {
        lateinit var nearbyPlacesList: List<HashMap<String, String>>
        var dataParser = DataParser()
        nearbyPlacesList = dataParser.parse(result)
        showNearbyPlaces(nearbyPlacesList)
    }

    private fun showNearbyPlaces(nearbyPlacesList: List<HashMap<String, String>>) {
        Log.i("ECOSNAP.ERROR", nearbyPlacesList.size.toString())
        for (i in nearbyPlacesList.indices) {
            var markerOptions = MarkerOptions()
            var googlePlace: HashMap<String, String> = nearbyPlacesList.get(i)
            var lat = googlePlace.get("lat")?.toDouble()
            var lng = googlePlace.get("lng")?.toDouble()
            var placeName = googlePlace.get("place_name")
            var vicinity = googlePlace.get("vicinity")
            var latLng = LatLng(lat as Double, lng as Double)
            markerOptions.position(latLng)
            markerOptions.title(placeName + " : " + vicinity)
            map.addMarker(markerOptions)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        }
    }
}

class DownloadUrl {
    fun readUrl(strUrl: String): String {
        var data = ""
        lateinit var inputStream: InputStream
        lateinit var urlConnection: HttpURLConnection

        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            inputStream = urlConnection.inputStream
            val br = BufferedReader(InputStreamReader(inputStream))
            val sb = StringBuffer()
            var line = br.readLine()
            while (line != null) {
                sb.append(line)
                line = br.readLine()
            }
            data = sb.toString()
            br.close()
        } catch (e: Exception) {
            Log.i("ECOSNAP.ERROR", e.toString())
        } finally {
            inputStream.close()
            urlConnection.disconnect()
            return data
        }
    }
}