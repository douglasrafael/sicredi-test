package com.fsdevelopment.sicreditestapp.ui.event.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.databinding.EventItemBinding

class EventListAdapter : ListAdapter<Event, EventListAdapter.UserViewHolder>(Companion) {
    private var listener: OnItemClickListener? = null

    companion object : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem == newItem
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EventItemBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentEvent: Event = getItem(position)
        holder.binding.event = currentEvent
        holder.binding.executePendingBindings()

        listener?.apply {
            holder.itemView.setOnClickListener { onItemClick(currentEvent) }
        }
    }

    class UserViewHolder(val binding: EventItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClick(event: Event)
    }
}