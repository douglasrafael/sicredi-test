package com.fsdevelopment.sicreditestapp.ui.event.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fsdevelopment.sicreditestapp.R
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber
import java.util.*


class EventDetailFragment : Fragment(), OnMapReadyCallback {
    private lateinit var event: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            event = EventDetailFragmentArgs.fromBundle(it).event
            Timber.d("DETAIL %s", event)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.event_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fragMap: SupportMapFragment = childFragmentManager
            .findFragmentById(R.id.locationMap) as SupportMapFragment
        fragMap.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
        googleMap.setOnMyLocationClickListener {
            Timber.d("LOCATION %s", it)
        }

        val addresses: List<Address>
        val geocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())

        addresses = geocoder.getFromLocation(
            -30.037878,
            -51.2148497,
            1
        )

        val address = addresses[0].getAddressLine(0)
        val city = addresses[0].subLocality
        val state = addresses[0].adminArea
        val zip = addresses[0].postalCode
        val country = addresses[0].countryName
    }

    /**
     * Checks whether the location permission was given.
     *
     * @return boolean
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}