package com.fsdevelopment.sicreditestapp.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fsdevelopment.sicreditestapp.R
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.databinding.EventSearchFragmentBinding
import com.fsdevelopment.sicreditestapp.ui.EventViewModel
import com.fsdevelopment.sicreditestapp.ui.EventViewState
import com.fsdevelopment.sicreditestapp.ui.adapter.EventListAdapter
import com.tapadoo.alerter.Alerter
import koleton.api.loadSkeleton
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventSearchFragment : Fragment(), EventListAdapter.OnItemClickListener {
    private lateinit var binding: EventSearchFragmentBinding

    private val viewModel: EventViewModel by viewModel()
    private val eventListAdapter = EventListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.event_search_fragment, container, false
        )

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
    }

    private fun initView() {
        initEventStateView()

        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.title_search)
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

    /**
     * Update the view to the loading status or not according to the value of the isActive parameter.
     */
    private fun displayLoading(isActive: Boolean) {
        when (isActive) {
            true -> {
                binding.placeholder.loadSkeleton()
                binding.placeholder.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.boxNoData.visibility = View.GONE
            }
            else -> {
                if (binding.boxNoData.isVisible) return
                binding.placeholder.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun displayNoData() {
        binding.boxNoData.visibility = View.VISIBLE
        binding.placeholder.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showMessageError(message: Int) {
        Alerter.create(requireActivity())
            .setTitle(R.string.title_error)
            .setIcon(R.drawable.ic_action_bad)
            .setBackgroundColorRes(R.color.colorDanger)
            .setText(message)
            .setDuration(7000)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)

        val searchManager = requireActivity().getSystemService(
            Context.SEARCH_SERVICE
        ) as SearchManager?
            ?: return

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView?
        searchView?.let {
            it.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            it.maxWidth = Int.MAX_VALUE
            it.onActionViewExpanded()
            it.queryHint = getString(R.string.search_hint)
        }

        // listening to search query text changes
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.loadEvents(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                onQueryTextSubmit(query)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onEventClick(event: Event) {
        val action = EventSearchFragmentDirections.toEventDetail(event)
        findNavController().navigate(action)
    }
}