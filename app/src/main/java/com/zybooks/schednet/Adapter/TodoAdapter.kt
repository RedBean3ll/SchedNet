package com.zybooks.schednet.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.R
import com.zybooks.schednet.TodoLocal

class TodoAdapter(actin: TodoLocal) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private var mActin: TodoLocal? = null
    private var mTdoList: List<TodoModel>? = null

    init {
        mActin = actin
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val RibbonCheckBox: CheckBox
        val RibbonText: TextView

        init {
            RibbonCheckBox = itemView.findViewById(R.id.sample_ribbon_box)
            RibbonText = itemView.findViewById(R.id.sample_ribbon_text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.todo_ribbon_frame, parent, false)

        return ViewHolder(v)
    }



    fun setTasks(todoList: List<TodoModel>) {
        mTdoList = todoList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item: TodoModel? = mTdoList?.get(position)
        holder.RibbonText.text = item?.TodoName
        holder.RibbonCheckBox.isChecked = convBool(item?.TodoStatus!!)
        holder.RibbonCheckBox.tag = item?.TodoName
    }

    override fun getItemCount(): Int {
        return mTdoList?.size!!
    }

    fun convBool(num: Int): Boolean {
        return if(num == 1) true else false
    }

}



