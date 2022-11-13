package com.zybooks.schednet.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.R

class MenuAdapter(context: Context, list: ArrayList<ListModel>): RecyclerView.Adapter<MenuAdapter.ViewHolder>()  {
   //Adapter may drop inp::list: ArrayList<ListModel> in favor of local management
    private var spindle: ArrayList<ListModel> = list
    private var ctx: Context = context

    //create holder of ribbons
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.menu_ribbon_frame, parent, false)

        val holder: ViewHolder = ViewHolder(view)
        return holder
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
        var rPin: Boolean = false
        val rBody: ConstraintLayout = viewHolder.findViewById(R.id.sample_ribbon_body)

        val v: View = viewHolder

        fun dataBind(strand: ListModel) {
            //Data
            rLabel.text = strand.ListName
            rPin = strand.isPinned

            //Initial Display
            if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }

            //Interract Functions [Note: may move outside]
            rImmBee.setOnClickListener {
                rPin = !rPin
                if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }
                else { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_alt_24) }
            }
        }
    }

    fun setOnTiemClickListener(rItemClickListener: AdapterView.OnItemClickListener) {

    }


    //CRUD OPERATIONS [note: database and sorting will be implemented ]
    fun removeAt(position: Int) {
        spindle.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun add(lst: ListModel) {
        spindle.add(0, lst)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, itemCount)
    }

    fun changeMode(pedal: Boolean) {

    }

    fun updateListExt(arg: ArrayList<ListModel>) {
        this.spindle.clear()
        spindle.addAll(arg)
        notifyDataSetChanged()
    }

}