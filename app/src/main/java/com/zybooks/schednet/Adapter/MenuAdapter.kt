package com.zybooks.schednet.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Fragments.TodoFragment
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager


class MenuAdapter(context: Context, id: Int): RecyclerView.Adapter<MenuAdapter.ViewHolder>()  {
    //Adapter may drop inp::list: ArrayList<ListModel> in favor of local management

    private var spindle: ArrayList<ListModel>
    private var ctx: Context
    private var clock: Long = 0
    private var measuringTape: Int = 0

    //create holder of ribbons
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.list_ribbon_menu, parent, false)
        return ViewHolder(view)
    }

    //bind data to currently displaying ribbons
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(spindle[position])
        holder.rBody.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(TodoFragment.LISTKEY, holder.rId)
            findNavController(it).navigate(R.id.show_todo_list, bundle)
        }


    }

    //ribbon Collection [Nick: thread]
    inner class ViewHolder(viewHolder: View) : RecyclerView.ViewHolder(viewHolder) {
        val rCheckBox: CheckBox = viewHolder.findViewById(R.id.sample_ribbon_checkbox)
        val rLabel: TextView = viewHolder.findViewById(R.id.sample_ribbon_title)
        val rImmBee: ImageButton = viewHolder.findViewById(R.id.sample_ribbon_priority)
        val rBody: ConstraintLayout = viewHolder.findViewById(R.id.sample_ribbon_body)
        var rPin: Boolean = false
        var rId: Int = 0


        fun dataBind(strand: ListModel) {
            //Data

            rLabel.text = strand.ListName
            rPin = strand.isPinned
            rId = strand.ListId

            //Initial Display
            if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }

            //Interact Functions [Note: may move outside]
            rImmBee.setOnClickListener {
                rPin = !rPin
                val dbm = DatabaseManager(ctx)
                dbm.updateListPinValue(rId, rPin)
                if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }
                else { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_alt_24) }
            }
        }
    }

    //CRUD OPERATIONS [note: database and sorting will be implemented ]
    fun removeAt(position: Int) {
        DatabaseManager(ctx).deleteList(spindle[position].ListId)
        spindle.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        //clock = threadCurrentTime()
    }

    fun callUpdateSpinner() {
        val updateSpinner = DatabaseManager(ctx).readListUpdate(measuringTape, clock)
        clock = threadCurrentTime()
        updateSpinner.forEach { thread ->
            add(thread)
        }
    }

    fun add(lst: ListModel) {
        spindle.add(0, lst)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, itemCount)
    }

    //INITIALIZATION LOGIC
    init {
        ctx = context
        measuringTape = id
        clock = threadCurrentTime()
        spindle = callSpinner()
    }

    fun callSpinner(): ArrayList<ListModel> {
        val spinner = DatabaseManager(ctx).readLists(measuringTape)
        clock = threadCurrentTime()
        return spinner
    }

    //GENERAL LOGIC
    fun threadCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    override fun getItemCount(): Int {
        return spindle.size
    }

    fun getContext(): Context {
        return ctx
    }

}