package com.zybooks.schednet.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.TodoEvent
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager

class TodoAdapter(context: Context, id: Int, listId: Int, onRibbonClick: OnRibbonListener): RecyclerView.Adapter<TodoAdapter.ViewHolder>()  {
    private var spindle: ArrayList<TodoEvent>
    private var ctx: Context
    private var gId: Int
    private var gListId: Int
    private var gOnRibbonClick: OnRibbonListener

    //create holder of ribbons
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.list_ribbon_todo, parent, false)
        return ViewHolder(view)
    }

    //bind data to currently displaying ribbons
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rLabel.text = spindle[position].eventName
        holder.rPin = spindle[position].isPinned
        holder.rId = spindle[position].eventId
        holder.rCheckBox.isChecked = spindle[position].isSelected

        if(holder.rCheckBox.isChecked) {
            holder.rBody.setBackgroundColor(ResourcesCompat.getColor(ctx.resources, R.color.gray_classic, null))
        }
        if(holder.rPin) { holder.rPinButton.setImageResource(R.drawable.ic_baseline_push_pin_24) }
        holder.dataBind()

        holder.rBody.setOnClickListener {
            gOnRibbonClick.onClick(it)
        }
    }

    //ribbon Collection [Nick: spool]
    inner class ViewHolder(viewHolder: View) : RecyclerView.ViewHolder(viewHolder) {
        val rBody: ConstraintLayout = viewHolder.findViewById(R.id.sample_ribbon_body)
        val rCheckBox: CheckBox = viewHolder.findViewById(R.id.sample_ribbon_checkbox)
        val rLabel: TextView = viewHolder.findViewById(R.id.sample_ribbon_title)
        var rId: Int = 0
        var rPin: Boolean = false
        val rPinButton: ImageButton = viewHolder.findViewById(R.id.sample_ribbon_priority)

        fun dataBind() {
            /* Interact Functions [Note: may move outside] */
            rPinButton.setOnClickListener {
                rPin = !rPin
                DatabaseManager(ctx).updateTodoPinValue(rId, rPin)
                if(rPin) { rPinButton.setImageResource(R.drawable.ic_baseline_push_pin_24) }
                else { rPinButton.setImageResource(R.drawable.ic_baseline_push_pin_alt_24) }
            }

            rCheckBox.setOnCheckedChangeListener { _, isChecked ->
                DatabaseManager(ctx).updateTodoCheckboxValue(rId, rCheckBox.isChecked)
                if(rCheckBox.isChecked) { rBody.setBackgroundColor(ResourcesCompat.getColor(ctx.resources, R.color.gray_classic, null)) }
                else { rBody.setBackgroundColor(Color.WHITE)}
            }
        }
    }

    //INITIALIZATION LOGIC
    init {
        ctx = context
        gId = id
        gListId = listId
        spindle = callSpinner()
        gOnRibbonClick = onRibbonClick
    }

    fun callSpinner(): ArrayList<TodoEvent> {
        val spinner = DatabaseManager(ctx).readTodos(gId, gListId)
        return  spinner
    }

    //GENERAL LOGIC
    override fun getItemCount(): Int {
        return spindle.size
    }

    fun getContext(): Context {
        return ctx
    }

    //CRUD OPERATIONS [note: database and sorting will be implemented ]
    fun removeAt(position: Int) {
        DatabaseManager(ctx).deleteTodo(spindle[position].eventId)
        spindle.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun add(event: TodoEvent) {
        spindle.add(0, event)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, itemCount)
    }

    interface OnRibbonListener {
        fun onClick(view: View)
    }


}