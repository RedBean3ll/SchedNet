package com.zybooks.schednet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.model.CalendarObject
import com.zybooks.schednet.R
import java.time.LocalDate

/**
 *  File: CalendarAdapter.kt
 *  @author Matthew Clark
 *  @author Amanda Thomas
 */

class CalendarAdapter(@Nullable private val onCellListener: OnCellListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback: DiffUtil.ItemCallback<CalendarObject> = object : DiffUtil.ItemCallback<CalendarObject>() {
        override fun areItemsTheSame(oldItem: CalendarObject, newItem: CalendarObject) = oldItem.calendarDay == newItem.calendarDay
        override fun areContentsTheSame(oldItem: CalendarObject, newItem: CalendarObject) = oldItem.toString() == newItem.toString()
    }
    private val differ: AsyncListDiffer<CalendarObject> = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams

        if(itemCount > 15) layoutParams.height = (parent.height * 0.166666666).toInt()
        else layoutParams.height = parent.height

        return CalendarViewHolder(view, onCellListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CalendarViewHolder) holder.bind(differ.currentList[position])
    }

    fun submitNewList(list: List<CalendarObject>) {
        differ.submitList(list)
    }

    override fun getItemCount() = differ.currentList.size

    class CalendarViewHolder constructor(
        self: View, @Nullable private val onCellListener: OnCellListener?
    ): RecyclerView.ViewHolder(self) {

        fun bind(day: CalendarObject?) {
            val cellRoot: ConstraintLayout = itemView.findViewById(R.id.cell_root)
            val cellLabel: TextView = itemView.findViewById(R.id.cell_label)
            val cellDot: ImageView = itemView.findViewById(R.id.cell_dot)


            if(day?.calendarDay != null) {
                cellLabel.text = day.calendarDay?.dayOfMonth.toString()
                cellRoot.setBackgroundColor(ContextCompat.getColor(cellRoot.context, R.color.white))
                cellLabel.setTextColor(ContextCompat.getColor(cellRoot.context, R.color.text_black))


                if(day.isActive) cellDot.visibility = View.VISIBLE
                else cellDot.visibility = View.GONE

                if(day.calendarDay == LocalDate.now()) {
                    cellRoot.setBackgroundColor(ContextCompat.getColor(cellRoot.context, R.color.purple_500))
                    cellLabel.setTextColor(ContextCompat.getColor(cellRoot.context, R.color.text_white))
                }

                itemView.setOnClickListener {
                    onCellListener?.onCellClick(absoluteAdapterPosition, day)
                }

            } else {
                cellLabel.text = ""
                cellDot.visibility = View.GONE
                cellRoot.setBackgroundColor(ContextCompat.getColor(cellRoot.context, R.color.calendar_gray))
            }
        }
    }

    interface OnCellListener {
        fun onCellClick(position: Int, date: CalendarObject)
    }


    /*
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
     */


}
