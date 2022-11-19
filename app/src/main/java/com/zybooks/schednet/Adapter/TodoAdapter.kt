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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager

class TodoAdapter(context: Context, id: Int, listId: Int): RecyclerView.Adapter<TodoAdapter.ViewHolder>()  {
   //user id

    private var spindle: ArrayList<TodoModel>
    private var ctx: Context
    private var clock: Long = 0 //Track Updates
    private var measuringTape: Int //User
    private var ruler: Int //List

    //create holder of ribbons
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.list_ribbon_todo, parent, false)
        return ViewHolder(view)
    }

    //bind data to currently displaying ribbons
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(spindle[position])
    }

    //ribbon Collection [Nick: spool]
    inner class ViewHolder(viewHolder: View) : RecyclerView.ViewHolder(viewHolder) {
        val rCheckBox: CheckBox = viewHolder.findViewById(R.id.sample_ribbon_checkbox)
        val rLabel: TextView = viewHolder.findViewById(R.id.sample_ribbon_title)
        val rImmBee: ImageButton = viewHolder.findViewById(R.id.sample_ribbon_priority)
        var rPin: Boolean = false
        var rId: Int = 0
        val rBody: ConstraintLayout = viewHolder.findViewById(R.id.sample_ribbon_body)

        fun dataBind(strand: TodoModel) {
            //Data
            rLabel.text = strand.TodoName
            rPin = strand.TodoPinned
            rId = strand.TodoId
            rCheckBox.isChecked = strand.TodoSelected

            //Initial Display
            if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }
            if(rCheckBox.isChecked) { rBody.setBackgroundColor(ResourcesCompat.getColor(ctx.resources, R.color.gray_classic, null)) }

            /* Interact Functions [Note: may move outside] */
            rImmBee.setOnClickListener {
                rPin = !rPin
                val dbm = DatabaseManager(ctx)
                dbm.updateTodoPinValue(rId, rPin)
                if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }
                else { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_alt_24) }
            }

            rCheckBox.setOnCheckedChangeListener { _, isChecked ->
                DatabaseManager(ctx).updateTodoCheckboxValue(rId, rCheckBox.isChecked)
                if(rCheckBox.isChecked) { rBody.setBackgroundColor(ResourcesCompat.getColor(ctx.resources, R.color.gray_classic, null)) }
                else { rBody.setBackgroundColor(Color.WHITE)}
            }

            rBody.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.show_todo_edit)
            }
        }
    }


    //INITIALIZATION LOGIC
    init {
        ctx = context
        measuringTape = id
        ruler = listId
        spindle = callSpinner()
    }

    fun callSpinner(): ArrayList<TodoModel> {
        val spinner = DatabaseManager(ctx).readTodos(measuringTape, ruler)
        clock = threadCurrentTime()
        return  spinner
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

    //CRUD OPERATIONS [note: database and sorting will be implemented ]
    fun removeAt(position: Int) {
        DatabaseManager(ctx).deleteTodo(spindle[position].TodoId)
        spindle.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun callUpdateSpinner() {
        val updateSpinner = DatabaseManager(ctx).readTodoUpdate(measuringTape, ruler, clock)
        clock = threadCurrentTime()
        updateSpinner.forEach { thread ->
            add(thread)
        }
    }

    fun add(lst: TodoModel) {
        spindle.add(0, lst)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, itemCount)
    }



}