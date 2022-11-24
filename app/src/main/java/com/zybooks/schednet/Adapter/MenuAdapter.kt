package com.zybooks.schednet.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.TodoList
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager


class MenuAdapter(@NonNull context: Context, @NonNull id: Int, @NonNull onRibbonListener: OnRibbonListener): RecyclerView.Adapter<MenuAdapter.ViewHolder>()  {
    //Adapter may drop inp::list: ArrayList<ListModel> in favor of local management

    private var spindle: ArrayList<TodoList>
    private var context: Context
    private var gOnRibbonListener: OnRibbonListener

    init {
        this.context = context
        spindle = callSpinner(id)
        this.gOnRibbonListener = onRibbonListener
    }

    //create holder of ribbons
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_ribbon_menu, parent, false))
    }

    //bind data to currently displaying ribbons
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rLabel.text = spindle[position].listName
        holder.rPin = spindle[position].isPinned
        holder.rId = spindle[position].listId
        holder.dataBind()

        holder.rBody.setOnClickListener {
            gOnRibbonListener.onClick(holder.rId, it)
        }
    }

    //ribbon Collection [Nick: thread]
    inner class ViewHolder(viewHolder: View) : RecyclerView.ViewHolder(viewHolder) {
        //val rCheckBox: CheckBox = viewHolder.findViewById(R.id.sample_ribbon_checkbox)
        val rBody: ConstraintLayout = viewHolder.findViewById(R.id.sample_ribbon_body)
        val rLabel: TextView = viewHolder.findViewById(R.id.sample_ribbon_title)
        val rImmBee: ImageButton = viewHolder.findViewById(R.id.sample_ribbon_priority)
        var rPin: Boolean = false
        var rId: Int = 0

        fun dataBind() {
            //Initial Display
            if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }

            //Interact Functions [Note: may move outside]
            rImmBee.setOnClickListener {
                rPin = !rPin
                DatabaseManager(context).updateListPinValue(rId, rPin)
                if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }
                else { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_alt_24) }
            }
        }
    }

    //CRUD OPERATIONS [note: database and sorting will be implemented ]
    fun removeAt(position: Int) {
        DatabaseManager(context).deleteList(spindle[position].listId)
        spindle.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun add(event: TodoList) {
        spindle.add(0, event)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, itemCount)
    }

    //INITIALIZATION LOGIC
    fun callSpinner(id: Int): ArrayList<TodoList> {
        val spinner = DatabaseManager(context).readLists(id)
        return spinner
    }

    //GENERAL LOGIC
    override fun getItemCount(): Int {
        return spindle.size
    }

    fun getContext(): Context {
        return context
    }

    interface OnRibbonListener {
        fun onClick(eventId: Int, view: View)
    }

}