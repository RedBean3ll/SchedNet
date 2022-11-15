package com.zybooks.schednet.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager


class MenuAdapter(context: Context, measure: Int): RecyclerView.Adapter<MenuAdapter.ViewHolder>()  {
    //Adapter may drop inp::list: ArrayList<ListModel> in favor of local management

    private lateinit var spindle: ArrayList<ListModel>
    private var ctx: Context = context
    private var clock: Long = 0

    //create holder of ribbons
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.menu_ribbon_frame, parent, false)
        return ViewHolder(view)
    }

    //bind data to currently displaying ribbons
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(spindle[position])
        holder.rBody.setOnClickListener {
            findNavController(it).navigate(R.id.show_todo_list)
        }
    }

    //gets size of current list
    override fun getItemCount(): Int {
        return spindle.size
    }

    fun getContext(): Context {
        return ctx
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

            //Interract Functions [Note: may move outside]
            rImmBee.setOnClickListener {
                rPin = !rPin

                val obj: ListModel = ListModel()
                obj.isPinned = rPin
                obj.ListId = rId

                val hey = DatabaseManager(ctx)
                hey.updateListPinValue(obj)
                if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }
                else { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_alt_24) }
            }
        }
    }


    //CRUD OPERATIONS [note: database and sorting will be implemented ]
    fun removeAt(position: Int) {
        val toRemove = ListModel()
        DatabaseManager(ctx).deleteListItem(spindle[position].ListId)
        spindle.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun add(lst: ListModel) {
        spindle.add(0, lst)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, itemCount)
    }

    fun callSpinner(context: Context, measure: Int): ArrayList<ListModel> {
        val spinner = DatabaseManager(context).readLists(measure)
        return spinner
    }

    fun callUpdateSpinner() {
        val updateSpinner = DatabaseManager(ctx).readListUpdate(clock)
        clock = System.currentTimeMillis()
        updateSpinner.forEach { thread ->
            add(thread)
        }

    }

    //INITIALIZATION LOGIC
    init {
        spindle = callSpinner(context, measure)
        clock = System.currentTimeMillis()
    }

    /*fun updateListExt(arg: ArrayList<ListModel>) {
        this.spindle.clear()
        spindle.addAll(arg)
        //notifyDataSetChanged()
    }
     */

}