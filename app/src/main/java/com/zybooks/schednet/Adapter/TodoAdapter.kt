package com.zybooks.schednet.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.TodoActivity
import com.zybooks.schednet.databinding.ActivityTodoPageBinding
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



        init {
            /*binding.apply {
                root.setOnClickListener {
                    val position = layoutPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val task = getItem(layoutPosition)
                        listener.onItemClick(task)
                        //May not work
                    }
                }
                sampleRibbonCheckbox.setOnClickListener {
                    val position = layoutPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, sampleRibbonCheckbox.isChecked)
                    }
                }
            }*/
        }


        fun bind(item: TodoModel) {
            binding.apply {
                sampleRibbonCheckbox.isChecked = item.TodoStatus
                sampleRibbonTitle.paint.isStrikeThruText = item.TodoStatus
                if(item.TodoStatus) sampleRibbonBody.setBackgroundColor(Color.parseColor("#D4D4D4"))
                sampleRibbonTitle.text = item.TodoName
                sampleRibbonCheckbox.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if(isChecked) {
                        binding.sampleRibbonBody.setBackgroundColor(Color.parseColor("#D4D4D4"))
                    } else {
                        binding.sampleRibbonBody.setBackgroundColor(Color.WHITE)
                    }
                    //getItem(layoutPosition)
                    //notifyItemChanged(layoutPosition)

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



