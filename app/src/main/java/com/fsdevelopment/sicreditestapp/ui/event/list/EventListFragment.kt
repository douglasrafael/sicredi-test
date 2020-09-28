package com.fsdevelopment.sicreditestapp.ui.event.list

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fsdevelopment.sicreditestapp.R
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.databinding.EventListFragmentBinding
import com.fsdevelopment.sicreditestapp.ui.event.EventViewModel
import com.fsdevelopment.sicreditestapp.ui.event.EventViewState
import com.tapadoo.alerter.Alerter
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.android.synthetic.main.provide_location_message.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val LOCATION_PERMISSION_REQUEST_CODE = 1

class EventListFragment : Fragment() {
    private lateinit var binding: EventListFragmentBinding

    private val viewModel: EventViewModel by viewModel()
    private val eventListAdapter = EventListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.event_list_fragment, container, false)
        binding.apply {
            // Defines the viewModel for data binding,
            // this allows layout-linked access to all data in VieWModel
            this.viewModel = viewModel
            this.adapter = eventListAdapter

            // Specifies to view the fragment as the owner of the link lifecycle.
            // This is used so that the call can watch LiveData updates
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        initEventStateView()
        viewModel.loadEvents()
    }

    private fun initEventStateView() {
        viewModel.getEventStateView().observe(viewLifecycleOwner, {
            when (it) {
                is EventViewState.Loading -> displayLoading(it.isActive)
                is EventViewState.ShowData -> it.events.let(eventListAdapter::submitList)
                is EventViewState.Error -> showMessageError(it.message)
            }
        })
    }

    private fun initView() {
        initEventListAdapter()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadEvents()
        }
        initEventListAdapter()
        initLocationRequired()
    }

    private fun initLocationRequired() {
        binding.boxRequireLocation.provideLocationButton?.setOnClickListener {
            val intent = Intent(
                ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", requireActivity().packageName, null)
            )
            startActivity(intent)
        }
    }

    private fun initEventListAdapter() {
        eventListAdapter.setOnClickListener(object : EventListAdapter.OnItemClickListener {
            override fun onItemClick(event: Event) {
                val action = EventListFragmentDirections.actionEventListToEventDetailFragment(event)
                findNavController().navigate(action)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        // Only for android sdk version 23 above
        when (hasLocationPermissions()) {
            true -> binding.boxRequireLocation.visibility = View.GONE
            else -> requestLocationPermission()
        }
    }

    /**
     * Update the view to the loading status or not according to the value of the isActive parameter.
     */
    private fun displayLoading(isActive: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = false

        when (isActive) {
            true -> {
                binding.boxPlaceholder.loadSkeleton()
                binding.boxPlaceholder.visibility = View.VISIBLE
                binding.boxRecyclerView.visibility = View.GONE
            }
            else -> {
                binding.boxPlaceholder.hideSkeleton()
                binding.boxPlaceholder.visibility = View.GONE
                binding.boxRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun showMessageError(message: Int) {
        Alerter.create(requireActivity())
            .setTitle("ERROR")
            .setIcon(R.drawable.ic_action_bad)
            .setBackgroundColorRes(R.color.colorDanger)
            .setText(message)
            .setDuration(7000)
            .show()
    }

    /**
     * Checks whether the location permission was given.
     *
     * @return boolean
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasLocationPermissions(): Boolean {
        return checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Request Location permission.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            binding.boxRequireLocation.visibility = View.VISIBLE
            return
        }
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
        )
    }

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
}