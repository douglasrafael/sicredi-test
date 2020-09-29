package com.fsdevelopment.sicreditestapp.ui.checkin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fsdevelopment.sicreditestapp.R
import com.fsdevelopment.sicreditestapp.databinding.BottomSheetCheckInBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tapadoo.alerter.Alerter
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventCheckInRegisterFragment : BottomSheetDialogFragment() {
    private lateinit var eventId: String

    private lateinit var binding: BottomSheetCheckInBinding
    private val viewModel: EventCheckInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            eventId = EventCheckInRegisterFragmentArgs.fromBundle(it).eventId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_sheet_check_in, container, false
        )

        binding.apply {
            // Defines the viewModel for data binding,
            // this allows layout-linked access to all data in VieWModel
            this.viewModeCheckInl = viewModel

            // Specifies to view the fragment as the owner of the link lifecycle.
            // This is used so that the call can watch LiveData updates
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        initObserver()
    }

    private fun initView() {
        binding.sendButton.setOnClickListener {
            viewModel.onSaveCheckIn(eventId)
        }
    }

    private fun initObserver() {
        viewModel.getEventStateView().observe(viewLifecycleOwner, {
            when (it) {
                is EventCheckInViewState.Loading -> displayLoading(it.isActive)
                is EventCheckInViewState.Checked -> displaySuccess()
                is EventCheckInViewState.Error -> showMessageError(it.message)
            }
        })
    }

    private fun displayLoading(active: Boolean) {
        binding.sendButton.isEnabled = false
        binding.progressBar.show()
    }

    private fun displaySuccess() {
        dismiss()
        Alerter.create(requireActivity())
            .setTitle(R.string.title_success)
            .setText(R.string.check_in_successful)
            .setBackgroundColorRes(R.color.colorPrimary)
            .setIcon(R.drawable.ic_action_mood)
            .setDuration(7000)
            .show()
    }

    private fun showMessageError(message: Int) {
        dismiss()
        Alerter.create(requireActivity())
            .setTitle(R.string.title_error)
            .setIcon(R.drawable.ic_action_bad)
            .setBackgroundColorRes(R.color.colorDanger)
            .setText(message)
            .setDuration(7000)
            .show()
    }
}