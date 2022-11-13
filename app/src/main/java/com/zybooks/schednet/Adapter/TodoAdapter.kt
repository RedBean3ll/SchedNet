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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.R

class TodoAdapter(context: Context, list: ArrayList<TodoModel>): RecyclerView.Adapter<TodoAdapter.ViewHolder>()  {
   //Adapter may drop inp::list: ArrayList<TodoModel> in favor of local management
    private var spindle: ArrayList<TodoModel> = list
    private var ctx: Context = context

    //create holder of ribbons
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.todo_ribbon_frame, parent, false)

        val holder: ViewHolder = ViewHolder(view)
        return holder
    }

    //bind data to currently displaying ribbons
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(spindle[position])
    }

    //gets size of current list
    override fun getItemCount(): Int {
        return spindle.size
    }

    fun getContext(): Context {
        return ctx
    }

    //ribbon Collection [Nick: spool]
    inner class ViewHolder(viewHolder: View) : RecyclerView.ViewHolder(viewHolder) {
        val rCheckBox: CheckBox = viewHolder.findViewById(R.id.sample_ribbon_checkbox)
        val rLabel: TextView = viewHolder.findViewById(R.id.sample_ribbon_title)
        val rImmBee: ImageButton = viewHolder.findViewById(R.id.sample_ribbon_priority)
        var rPin: Boolean = false
        val rBody: ConstraintLayout = viewHolder.findViewById(R.id.sample_ribbon_body)

        val v: View = viewHolder

        fun dataBind(strand: TodoModel) {
            //Data
            rLabel.text = strand.TodoName
            rPin = strand.TodoPinned

            //Initial Display
            if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }

            //Interract Functions [Note: may move outside]
            rImmBee.setOnClickListener {
                rPin = !rPin
                if(rPin) { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_24) }
                else { rImmBee.setImageResource(R.drawable.ic_baseline_push_pin_alt_24) }
            }

            rBody.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.show_todo_edit)
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

    fun add(lst: TodoModel) {
        spindle.add(0, lst)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, itemCount)
    }

    fun updateListExt(arg: ArrayList<TodoModel>) {
        this.spindle.clear()
        spindle.addAll(arg)
        notifyDataSetChanged()
    }

}