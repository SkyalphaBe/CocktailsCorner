package fr.graux.cocktailscornerproject

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class Map_Page : Fragment(),
    OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback{

    private lateinit var mMap:GoogleMap
    private var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:Int = 1
    private var locationPermissionGranted:Boolean = false
    private val client = OkHttpClient()
    val barList:ArrayList<Bar> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View = inflater.inflate(R.layout.fragment_map__page,container,false)

        val mapFragment:SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        updateLocationUI()
    }

    private fun getLocationPermission(){
        if (this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
             requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
        else{
            locationPermissionGranted=true
            updateLocationUI()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION ->{
                if(grantResults.isNotEmpty()&&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationPermissionGranted=true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
                zoomToCurrentLocation()
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun zoomToCurrentLocation(){
        val locationManager:LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria:Criteria = Criteria()
        val location:Location? = locationManager.getBestProvider(criteria,false)
            ?.let { locationManager.getLastKnownLocation(it) }
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude),17.0f))
            run(location.latitude,location.longitude)
        }
    }

    private fun run(latitude:Double, longitude:Double) {
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latitude,$longitude&radius=1000&type=bar&key=${BuildConfig.MAPS_API_KEY}"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val reponseString = response.body()!!.string()
                //on crée l'object JSON
                val contactJSON= JSONObject(reponseString)
                //on crée le tableau JSON
                val infoJSONArray: JSONArray = contactJSON.getJSONArray("results")
                val size:Int = infoJSONArray.length()

                for (i in 0 until size) {
                    val objectDetailJSON: JSONObject =infoJSONArray.getJSONObject(i)
                    val bar:Bar = Bar()
                    bar.lat = objectDetailJSON
                        .getJSONObject("geometry")
                        .getJSONObject("location")
                        .getDouble("lat")
                    bar.lng = objectDetailJSON
                        .getJSONObject("geometry")
                        .getJSONObject("location")
                        .getDouble("lng")
                    bar.name = objectDetailJSON.getString("name")
                    barList.add(bar)
                }
                activity?.runOnUiThread{
                    for(bar:Bar in barList)
                        makeMarker(bar)
                    mMap.setOnInfoWindowClickListener { marker->
                        AlertDialog.Builder(requireContext())
                            .setTitle("favoris")
                            .setMessage("Voulez-vous mettre ce bar en favoris ?")
                            .setPositiveButton("Oui",DialogInterface.OnClickListener{
                                    _, _ ->
                                    for(bar:Bar in barList) {
                                        if (marker.position == LatLng(bar.lat, bar.lng)) {
                                            bar.favorite = true
                                        }
                                    }
                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icone__toile))
                                })
                            .setNegativeButton("Non",DialogInterface.OnClickListener{
                                _, _ ->
                            })
                            .show()
                    }
                }
            }
        })

    }

    private fun makeMarker(bar:Bar){
        if(bar.favorite)
            mMap.addMarker(MarkerOptions()
                .title(bar.name)
                .position(LatLng(bar.lat,bar.lng))
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icone__toile))
            )
        else
            mMap.addMarker(MarkerOptions()
                .title(bar.name)
                .position(LatLng(bar.lat,bar.lng))
                .draggable(false)
            )

    }
}

