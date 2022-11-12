package com.zybooks.schednet.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.R
import com.zybooks.schednet.databinding.TodoRibbonFrameBinding

class TodoAdapter(private var menuList: MutableList<TodoModel>) : ListAdapter<TodoModel, TodoAdapter.TodoViewHolder>(DiffCallback()) {
    private lateinit var mTodos: List<TodoModel>

    init {
        mTodos = menuList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoRibbonFrameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = mTodos[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mTodos.size
    }

    fun setTasks(list: MutableList<TodoModel>) {
        this.mTodos = list
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(private val binding: TodoRibbonFrameBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: TodoModel) {
            var pinned: Boolean = false

            binding.apply {
                binding.sampleRibbonCheckbox.isChecked = item.TodoStatus
                binding.sampleRibbonTitle.text = item.TodoName
                pinned = item.TodoStatus

                if(item.TodoStatus) sampleRibbonBody.setBackgroundColor(Color.parseColor("#D4D4D4"))

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
                    Navigation.findNavController(it).navigate(R.id.show_todo_edit)
                    //Log.i("TodoAdapter", "Item named: " + sampleRibbonTitle.text.toString() + " w/ Descr: "+ desc)
                }

                sampleRibbonPriority.setOnClickListener {
                    pinned = !pinned
                    if(pinned) {
                        binding.sampleRibbonPriority.setImageResource(R.drawable.ic_baseline_push_pin_24)
                    } else {
                        binding.sampleRibbonPriority.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)
                    }
                }
            }

        }
    }

    class DiffCallback: DiffUtil.ItemCallback<TodoModel>() {
        override fun areItemsTheSame(oldItem: TodoModel, newItem: TodoModel) =
            oldItem.TodoId == newItem.TodoId

        override fun areContentsTheSame(oldItem: TodoModel, newItem: TodoModel) =
            oldItem.toString() == newItem.toString()

    }
}