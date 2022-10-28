package com.zybooks.schednet.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.CalendarPage
import com.zybooks.schednet.Model.CalModel
import com.zybooks.schednet.R

class CalAdapter(actin: CalendarPage) : RecyclerView.Adapter<CalAdapter.ViewHolder>() {

    //LIST AND TMI
    private var mActin: CalendarPage? = null
    private var mCalList: List<CalModel>? = null //LIST OF EVENTS FOR CAL

    init {
        mActin = actin
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //SPECIFY VIEWS TO INTERRACT WITH
        var RibbonName: TextView
        var RibbonDate: TextView

        init {
            RibbonName = itemView.findViewById(R.id.sample_ribbon_text)
            RibbonDate = itemView.findViewById(0) //Eg: R.id._
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { //ON CREATE
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.cal_ribbon_frame, parent, false)

        return ViewHolder(v)
    }

    fun setTasks(calList: List<CalModel>) { //TMI
        mCalList = calList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { //BIND DATA TO CARD
        var item: CalModel? = mCalList?.get(position)
        holder.RibbonName.text = item?.CalEventName
        holder.RibbonDate.text = "STRUCTURE OF EVENT START AND EVENT END"
    }

    override fun getItemCount(): Int { //SIZE OF LIST
        return mCalList?.size!!
    }

    fun convBool(num: Int): Boolean { //CONVERTS INT TO BOOLEAN
        return if(num == 1) true else false
    }

}



