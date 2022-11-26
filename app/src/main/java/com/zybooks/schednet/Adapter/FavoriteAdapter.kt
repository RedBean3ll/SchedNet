package com.zybooks.schednet.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.ListObject
import com.zybooks.schednet.R

class FavoriteAdapter(context: Context, list: ArrayList<ListObject>, mode: Boolean): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>()  {
   //Adapter may drop inp::list: ArrayList<ListModel> in favor of local management
    private var stand: ArrayList<ListObject> = list
    private var ctx: Context = context

    //Favorite TDO
    //private var spindleA: ArrayList<ListModel>

    //Favorite Cal
    //private var spindleB: ArrayList<ListModel>


    //create holder of ribbons
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.list_ribbon_menu, parent, false)
        return ViewHolder(view)
    }

    //bind data to currently displaying ribbons
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(stand[position])
    }

    //gets size of current list
    override fun getItemCount(): Int {
        return stand.size
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

        fun dataBind(strand: ListObject) {
            //Data
            rLabel.text = strand.listName
            rPin = strand.isPinned

            //Initial Display
            if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }

            //Interact Functions [Note: may move outside]
            rImmBee.setOnClickListener {
                rPin = !rPin
                if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }
                else { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_alt_24) }
            }
        }
    }

    //CRUD OPERATIONS [note: database and sorting will be implemented ]
    fun removeAt(position: Int) {
        stand.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun add(lst: ListObject) {
        stand.add(0, lst)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, itemCount)
    }

    fun changeMode(pedal: Boolean) {

    }

    fun updateListExt(arg: ArrayList<ListObject>) {
        this.stand.clear()
        stand.addAll(arg)
        notifyDataSetChanged()
    }

}