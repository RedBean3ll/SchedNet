package com.zybooks.schednet.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
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
        val checkBoi: CheckBox

        init {
            checkBoi = itemView.findViewById(R.id.sample_ribbon_box)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.todo_ribbon_frame, parent, false)

        return ViewHolder(v)
    }

    fun boolMe(num: Int?): Boolean {
        return num!=0
    }

    fun setTasks(todoList: List<TodoModel>) {
        mTdoList = todoList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item: TodoModel? = mTdoList?.get(position)
        holder.checkBoi.text = item?.mTaskNam
        holder.checkBoi.isChecked = boolMe(item?.mStatus)
        holder.checkBoi.tag = item?.mTaskNam
    }

    override fun getItemCount(): Int {
        return mTdoList?.size!!
    }

}



