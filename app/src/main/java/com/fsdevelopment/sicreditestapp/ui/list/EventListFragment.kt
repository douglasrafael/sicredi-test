package com.fsdevelopment.sicreditestapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fsdevelopment.sicreditestapp.R
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.databinding.EventListFragmentBinding
import com.fsdevelopment.sicreditestapp.ui.EventViewModel
import com.fsdevelopment.sicreditestapp.ui.EventViewState
import com.fsdevelopment.sicreditestapp.ui.adapter.EventListAdapter
import com.tapadoo.alerter.Alerter
import koleton.api.loadSkeleton
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventListFragment : Fragment(), EventListAdapter.OnItemClickListener {
    private lateinit var binding: EventListFragmentBinding

    private val viewModel: EventViewModel by viewModel()
    private val eventListAdapter = EventListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.event_list_fragment, container, false)
        binding.apply {
            // Defines the viewModel for data binding,
            // this allows layout-linked access to all data in VieWModel
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
        viewModel.loadEvents()
    }

    private fun initEventStateView() {
        viewModel.getEventStateView().observe(viewLifecycleOwner, {
            when (it) {
                is EventViewState.Loading -> displayLoading(it.isActive)
                is EventViewState.ShowData -> it.events.let(eventListAdapter::submitList)
                is EventViewState.NoData -> displayNoData()
                is EventViewState.Error -> showMessageError(it.message)
            }
        })
    }

    private fun initView() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadEvents()
        }
        initEventStateView()
    }

    private fun displayNoData() {
        binding.boxNoData.visibility = View.VISIBLE
        binding.placeholder.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
    }

    /**
     * Update the view to the loading status or not according to the value of the isActive parameter.
     */
    private fun displayLoading(isActive: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = false

        when (isActive) {
            true -> {
                binding.placeholder.loadSkeleton()
                binding.placeholder.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.boxNoData.visibility = View.GONE
            }
            else -> {
                binding.placeholder.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Display alert error!
     */
    private fun showMessageError(message: Int) {
        displayNoData()
        Alerter.create(requireActivity())
            .setTitle(R.string.title_error)
            .setIcon(R.drawable.ic_action_bad)
            .setBackgroundColorRes(R.color.colorDanger)
            .setText(message)
            .setDuration(7000)
            .show()
    }

    override fun onEventClick(event: Event) {
        val action = EventListFragmentDirections.toEventDetail(event)
        findNavController().navigate(action)
    }
}