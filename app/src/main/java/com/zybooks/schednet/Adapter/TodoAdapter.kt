package com.zybooks.schednet.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.databinding.TodoRibbonFrameBinding

class TodoAdapter(private var TodoList: List<TodoModel>) : ListAdapter<TodoModel, TodoAdapter.TodoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoRibbonFrameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = TodoList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return TodoList.size
    }

    fun setTasks(list: MutableList<TodoModel>) {
        this.TodoList = list
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(private val binding: TodoRibbonFrameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TodoModel) {
            var desc: String
            binding.apply {
                binding.sampleRibbonCheckbox.isChecked = item.TodoStatus
                if(item.TodoStatus) sampleRibbonBody.setBackgroundColor(Color.parseColor("#D4D4D4"))
                binding.sampleRibbonTitle.text = item.TodoName
                desc = item.TodoDescription
                sampleRibbonCheckbox.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if(isChecked) {
                        sampleRibbonBody.setBackgroundColor(Color.parseColor("#D4D4D4"))
                    } else {
                        sampleRibbonBody.setBackgroundColor(Color.WHITE)
                    }
                    //getItem(layoutPosition)
                    //notifyItemChanged(layoutPosition)

                }
                sampleRibbonBody.setOnClickListener {


                    //Log.i("TodoAdapter", "Item named: " + sampleRibbonTitle.text.toString() + " w/ Descr: "+ desc)
                }
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: TodoModel, position: Int)
        fun onCheckBoxClick(task: TodoModel, isChecked: Boolean)
    }

    class DiffCallback: DiffUtil.ItemCallback<TodoModel>() {
        override fun areItemsTheSame(oldItem: TodoModel, newItem: TodoModel) =
            oldItem.TodoId == newItem.TodoId

        override fun areContentsTheSame(oldItem: TodoModel, newItem: TodoModel) =
            oldItem.toString() == newItem.toString()

    }
}



