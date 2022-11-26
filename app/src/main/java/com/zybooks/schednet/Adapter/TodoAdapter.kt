package com.zybooks.schednet.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.TodoObject
import com.zybooks.schednet.R

class TodoAdapter(@Nullable private val onRibbonListener: OnRibbonListener?): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val diffCallback: DiffUtil.ItemCallback<TodoObject> = object : DiffUtil.ItemCallback<TodoObject>() {
        override fun areItemsTheSame(oldItem: TodoObject, newItem: TodoObject): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(oldItem: TodoObject, newItem: TodoObject): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
    private val differ: AsyncListDiffer<TodoObject> = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_ribbon_todo, parent, false), onRibbonListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if(holder is TodoViewHolder) {
            holder.bind(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitNewList(list: List<TodoObject>) {
        differ.submitList(list)
    }

    class TodoViewHolder constructor(
        self: View, @Nullable private val onRibbonListener: OnRibbonListener?
    ): RecyclerView.ViewHolder(self) {
        fun bind(todo: TodoObject) {
            //View Click Listeners
            itemView.setOnClickListener {
                onRibbonListener?.onClick(todo)
            }

            //DATA
            val checkBox: CheckBox = itemView.findViewById(R.id.sample_ribbon_checkbox)
            val label: TextView = itemView.findViewById(R.id.sample_ribbon_title)
            val pinButton: ImageButton = itemView.findViewById(R.id.sample_ribbon_priority)
            label.text = todo.eventName

            if(todo.isPinned) pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
            else pinButton.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)

            checkBox.isChecked = todo.isChecked
            if(todo.isChecked) itemView.setBackgroundColor(Color.parseColor("#EBEBEB"))
            else itemView.setBackgroundColor(Color.WHITE)

            pinButton.setOnClickListener {
                todo.isPinned = !todo.isPinned
                onRibbonListener?.onPin(absoluteAdapterPosition, todo)

                if(todo.isPinned) pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
                else pinButton.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)
            }

            checkBox.setOnCheckedChangeListener { _,isCheck ->
                todo.isChecked = isCheck
                onRibbonListener?.onCheck(absoluteAdapterPosition, todo)

                if(todo.isChecked) itemView.setBackgroundColor(Color.parseColor("#EBEBEB"))
                else itemView.setBackgroundColor(Color.WHITE)
            }
        }
    }

    interface OnRibbonListener {
        fun onClick(event: TodoObject)
        fun onPin(position: Int, event: TodoObject)
        fun onCheck(position: Int, event: TodoObject)
    }
}
