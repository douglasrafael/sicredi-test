package com.fsdevelopment.sicreditestapp.ui.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fsdevelopment.sicreditestapp.R
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.databinding.EventDetailFragmentBinding
import com.fsdevelopment.sicreditestapp.utils.toString
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.event_detail_fragment.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*
import kotlin.math.abs

private const val LOCATION_PERMISSION_REQUEST_CODE = 1

class EventDetailFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: EventDetailFragmentBinding
    private lateinit var eventSelected: Event

    private val viewModel: EventDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            eventSelected = EventDetailFragmentArgs.fromBundle(it).event
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.event_detail_fragment, container, false
        )
        binding.apply {
            eventViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.event = eventSelected

        initView()
        initObserver()

        binding.checkInButton.setOnClickListener {
            eventSelected.id?.let {
                val action = EventDetailFragmentDirections.toCheckInDialog(it)
                findNavController().navigate(action)
            }
        }
    }

    private fun initObserver() {
        viewModel.getEventStateView().observe(viewLifecycleOwner, {
            when (it) {
                is EventDetailViewState.SaveFavorite -> {
                    binding.event = it.event
                    Toast.makeText(
                        requireContext(), R.string.event_checked_favorite, Toast.LENGTH_SHORT
                    ).show()
                }
                is EventDetailViewState.RemoveFavorite -> {
                    binding.event = it.event
                    Toast.makeText(
                        requireContext(), R.string.event_unchecked_favorite, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun initView() {
        initCollapsingToolbar()
        initScrollView()

        val fragMap: SupportMapFragment = childFragmentManager
            .findFragmentById(R.id.locationMap) as SupportMapFragment
        fragMap.getMapAsync(this)

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.shareButton.setOnClickListener {
            shareEvent()
        }
    }

    private fun initScrollView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                showHideFloatingButton(
                    scrollY, oldScrollY
                )
            }
        }
    }

    /**
     * Configure style CollapsingToolbar.
     */
    private fun initCollapsingToolbar() {
        binding.collapsingToolbar.apply {
            setExpandedTitleColor(
                ContextCompat.getColor(requireContext(), android.R.color.transparent)
            )
            setCollapsedTitleTextColor(
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryLight)
            )
        }

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout
            .OnOffsetChangedListener { appBarLayout, verticalOffset ->
                when (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                    true -> {
                        appBarLayout.collapsingToolbar.title = eventSelected.title
                        appBarLayout.toolbar.closeButton.visibility = View.VISIBLE
                        appBarLayout.toolbar.setBackgroundColor(
                            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                        )
                    }
                    else -> {
                        appBarLayout.collapsingToolbar.title = ""
                        appBarLayout.toolbar.closeButton.visibility = View.GONE
                        appBarLayout.toolbar.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.transparent
                            )
                        )
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        // Only for android sdk version 23 above
        if (!hasLocationPermissions()) {
            requestLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        if (eventSelected.latitude.isEmpty() || eventSelected.longitude.isEmpty()) return

        val location = LatLng(eventSelected.latitude.toDouble(), eventSelected.longitude.toDouble())

        googleMap.addMarker(MarkerOptions().position(location).title(eventSelected.title))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        // get address
        try {
            Geocoder(requireContext(), Locale.getDefault()).getFromLocation(
                location.latitude, location.longitude, 1
            )[0].getAddressLine(0)?.let {
                eventSelected.address = it
                binding.event = eventSelected
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                googleMap.isMyLocationEnabled = hasLocationPermissions()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
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

    /**
     * Request Location permission.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) return

        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    /**
     * Callback permission.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }
    }

    /**
     * Expands or shrinks the bottom floating button.
     */
    private fun showHideFloatingButton(dy: Int, oldDy: Int) {
        if (dy > oldDy) {
            binding.checkInButton.shrink()
            return
        }
        binding.checkInButton.extend()
    }

    /**
     * Sharing Event
     */
    private fun shareEvent() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.event_share_title)
                    .plus("\n" + getString(R.string.title_event) + ": ")
                    .plus(eventSelected.title)
                    .plus("\n" + Date(eventSelected.date).toString(getString(R.string.format_date)))
                    .plus("\n" + eventSelected.address)
                    .plus("\n" + getString(R.string.price_format, eventSelected.price.toString()))
            )
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}