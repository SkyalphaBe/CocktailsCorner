package fr.graux.cocktailscornerproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng

class Map_Page : Fragment(),
    OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback{

    private lateinit var mMap:GoogleMap
    private var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:Int = 1
    private var locationPermissionGranted:Boolean = false

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

    private fun zoomToCurrentLocation(){
        val locationManager:LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria:Criteria = Criteria()
        val location:Location? = locationManager.getBestProvider(criteria,false)
            ?.let { locationManager.getLastKnownLocation(it) }
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude),17.0f))
        }
    }
}