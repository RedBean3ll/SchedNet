package com.zybooks.schednet.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.R
import java.time.LocalDate

class CalendarAdapter(dayList: ArrayList<LocalDate?>, onItemListener: OnItemListener, eventPositions: ArrayList<Boolean?>): RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var dayList: ArrayList<LocalDate?>
    private val itemListener: OnItemListener
    private val eventList: ArrayList<Boolean?> = eventPositions

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams

        if(itemCount > 15) layoutParams.height = (parent.height * 0.166666666).toInt()
        else layoutParams.height = parent.height

        return CalendarViewHolder(view, itemListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date: LocalDate? = dayList[position]

        holder.cellDot.visibility = View.GONE
        if(date == null) {
            holder.cellLabel.text = ""
            holder.cellRoot.setBackgroundColor(ContextCompat.getColor(holder.cellRoot.context, R.color.calendar_gray))
        } else {
            holder.cellLabel.text = date.dayOfMonth.toString()
            holder.cellRoot.setBackgroundColor(Color.WHITE)
            holder.cellRoot.setOnClickListener {
                holder.onItemListener.onItemClick(holder.absoluteAdapterPosition, dayList[holder.absoluteAdapterPosition])
            }
            if(date == LocalDate.now()) {
                holder.cellRoot.setBackgroundColor(ContextCompat.getColor(holder.cellRoot.context, R.color.purple_500))
                holder.cellLabel.setTextColor(ContextCompat.getColor(holder.cellRoot.context, R.color.text_white))
                holder.cellLabel.setTypeface(holder.cellLabel.typeface, Typeface.BOLD)
            }
            if(eventList[position] == true) {
                holder.cellDot.visibility = View.VISIBLE
            }
        }
    }

    inner class CalendarViewHolder(@NonNull itemView: View, onItemListener: OnItemListener): RecyclerView.ViewHolder(itemView) {
        val cellRoot: View
        val cellLabel: TextView
        val cellDot: ImageView
        val onItemListener: OnItemListener

        init {
            cellRoot = itemView.findViewById(R.id.cell_root)
            cellLabel = itemView.findViewById(R.id.cell_label)
            cellDot = itemView.findViewById(R.id.cell_dot)
            this.onItemListener = onItemListener
            //itemView.setOnClickListener(this)
        }

        /*
        override fun onClick(v: View?) {
            onItemListener.onItemClick(absoluteAdapterPosition, dayList[absoluteAdapterPosition])
        }*/
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(dayList: ArrayList<LocalDate?>) {
        this.dayList = dayList
        notifyDataSetChanged()
    }

    fun updateClickPosition(position: Int) {
        //animation
    }

    override fun getItemCount(): Int {
        return  dayList.size
    }

    init {
        this.dayList = dayList
        itemListener = onItemListener
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
        fun onItemLongClick(position: Int)
    }


}
