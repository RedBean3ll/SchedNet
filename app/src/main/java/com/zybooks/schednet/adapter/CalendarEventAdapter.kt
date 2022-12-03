package com.zybooks.schednet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.model.CalendarEvent
import com.zybooks.schednet.R
import com.zybooks.schednet.utils.DateConversions
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 *  File: CalendarEventAdapter.kt
 *  @author Matthew Clark
 */

class CalendarEventAdapter(@Nullable private val onRibbonClick: OnRibbonClick): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback: DiffUtil.ItemCallback<CalendarEvent> = object : DiffUtil.ItemCallback<CalendarEvent>() {
        override fun areItemsTheSame(oldItem: CalendarEvent, newItem: CalendarEvent) = oldItem.eventId == newItem.eventId
        override fun areContentsTheSame(oldItem: CalendarEvent, newItem: CalendarEvent) = oldItem.toString() == newItem.toString()
    }
    private val differ: AsyncListDiffer<CalendarEvent> = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_ribbon_calendar, parent, false)
        return EventViewHolder(view, onRibbonClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is EventViewHolder) holder.bind(differ.currentList[position])
    }

    fun submitNewList(list: List<CalendarEvent>) {
        differ.submitList(list)
    }

    override fun getItemCount() = differ.currentList.size

    class EventViewHolder constructor(@NonNull self: View, @Nullable private val onRibbonClick: OnRibbonClick?): RecyclerView.ViewHolder(self) {

        fun bind(event: CalendarEvent) {
            itemView.setOnClickListener {
                onRibbonClick?.onClickEvent(absoluteAdapterPosition, event)
            }

            val label: TextView = itemView.findViewById(R.id.sample_ribbon_title)
            val timeLabel: TextView = itemView.findViewById(R.id.sample_ribbon_time)
            val dateLabel: TextView = itemView.findViewById(R.id.sample_ribbon_date)
            label.text = event.eventName
            timeLabel.text = DateConversions.formatTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(event.eventDateTime), ZoneId.systemDefault()))
            dateLabel.text = DateConversions.formatListDate(ZonedDateTime.ofInstant(Instant.ofEpochMilli(event.eventDateTime), ZoneId.systemDefault()))
        }
    }

    interface OnRibbonClick {
        fun onClickEvent(position: Int, event: CalendarEvent)
    }
}