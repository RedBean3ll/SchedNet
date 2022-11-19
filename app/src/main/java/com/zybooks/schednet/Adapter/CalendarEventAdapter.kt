package com.zybooks.schednet.Adapter

import android.media.metrics.Event
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.R

class CalendarEventAdapter(eventList: ArrayList<Event>, height: Int): RecyclerView.Adapter<CalendarEventAdapter.EventViewHolder>() {
    lateinit var eventList: ArrayList<Event>

    init {
        this.eventList = eventList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_ribbon_calendar, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return this.eventList.size
    }

    inner class EventViewHolder(@NonNull itemView: View): RecyclerView.ViewHolder(itemView) {
        //CheckBox
        //text
        val rPin: ImageView = itemView.findViewById(R.id.sample_ribbon_priority)

        init {
            rPin.visibility = View.GONE
        }

    }
}