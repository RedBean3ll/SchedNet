package com.zybooks.schednet.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.CalendarEvent
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager

class CalendarEventAdapter(context: Context, onRibbonClick: OnRibbonClick, eventList: ArrayList<CalendarEvent>): RecyclerView.Adapter<CalendarEventAdapter.EventViewHolder>() {
    var eventList: ArrayList<CalendarEvent>
    var gContext: Context
    val onClickEvent: OnRibbonClick

    init {
        this.eventList = eventList
        this.gContext = context
        this.onClickEvent = onRibbonClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_ribbon_calendar, parent, false)
        return EventViewHolder(view, this.onClickEvent)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.label.text = eventList[position].eventName
    }

    override fun getItemCount(): Int {
        return this.eventList.size
    }

    inner class EventViewHolder(@NonNull itemView: View, onRibbonClick: OnRibbonClick): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val checkBox: CheckBox
        val label: TextView
        val onClickEvent: OnRibbonClick

        init {
            checkBox = itemView.findViewById(R.id.sample_ribbon_checkbox)
            label = itemView.findViewById(R.id.sample_ribbon_title)
            this.onClickEvent = onRibbonClick
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickEvent.onClickEvent(eventList[absoluteAdapterPosition].eventId)
        }

    }

    fun removeAt(position: Int) {
        DatabaseManager(gContext).deleteCalendarEvent(eventList[position].eventId)
        eventList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun getContext(): Context {
        return gContext
    }

    interface OnRibbonClick {
        fun onClickEvent(eventId: Int)
    }
}