package fr.graux.cocktailscornerproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import fr.graux.cocktailscornerproject.databinding.ActivityMainBinding

class Map_Page : Fragment(), OnMapReadyCallback{

    private lateinit var mMap:GoogleMap
    private lateinit var binding: ActivityMainBinding

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
        val sydney:LatLng = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}