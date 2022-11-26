package com.zybooks.schednet.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.ListObject
import com.zybooks.schednet.R


class ListAdapter(@Nullable private val onRibbonListener: OnRibbonListener?): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    //Adapter may drop inp::list: ArrayList<ListModel> in favor of local management

    private val diffCallback: DiffUtil.ItemCallback<ListObject> = object : DiffUtil.ItemCallback<ListObject>() {
        override fun areItemsTheSame(oldItem: ListObject, newItem: ListObject): Boolean {
            return oldItem.listId == newItem.listId
        }
        override fun areContentsTheSame(oldItem: ListObject, newItem: ListObject): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
    private val differ: AsyncListDiffer<ListObject> = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_ribbon_menu, parent, false), onRibbonListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ListViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitNewList(list: List<ListObject>) {
        differ.submitList(list)
    }

    //create holder of ribbons
    class ListViewHolder constructor(
        self: View, @Nullable private val onRibbonListener: OnRibbonListener?
    ): RecyclerView.ViewHolder(self) {

        fun bind(list: ListObject) = with(itemView) {
            //VIEW CLICK LISTENERS
            itemView.setOnClickListener {
                onRibbonListener?.onClick(absoluteAdapterPosition, list)
            }
            itemView.setOnLongClickListener {
                onRibbonListener?.onLongClick(absoluteAdapterPosition, list)
                return@setOnLongClickListener true
            }

            //DATA
            val label: TextView = itemView.findViewById(R.id.sample_ribbon_title)
            val pinButton: ImageButton = itemView.findViewById(R.id.sample_ribbon_priority)
            label.text = list.listName

            if(list.isPinned) pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
            else pinButton.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)

            pinButton.setOnClickListener {
                //Simple invert (will add action later)
                list.isPinned = !list.isPinned
                onRibbonListener?.onPin(absoluteAdapterPosition, list)

                if(list.isPinned) pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
                else pinButton.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)
            }
        }
    }

    interface OnRibbonListener {
        fun onClick(position: Int, list: ListObject)
        fun onLongClick(position: Int, list: ListObject)
        fun onPin(position: Int, list: ListObject)
    }
}