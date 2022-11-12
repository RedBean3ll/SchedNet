package com.zybooks.schednet.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.R
import com.zybooks.schednet.databinding.MenuRibbonFrameBinding
import com.zybooks.schednet.databinding.TodoRibbonFrameBinding

class MenuAdapter(private var menuList: List<ListModel>) : ListAdapter<ListModel, MenuAdapter.MenuViewHolder>(DiffCallback()) {
    private lateinit var mList: List<ListModel>

    init {
        mList = menuList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuRibbonFrameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = mList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setTasks(list: MutableList<ListModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    inner class MenuViewHolder(private val binding: MenuRibbonFrameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListModel) {

            var pinned: Boolean
            binding.apply {
                //Value Assignment
                binding.sampleRibbonTitle.text = item.ListName
                pinned = item.isPinned

                //State assignment
                if(pinned) {
                    binding.sampleRibbonPriority.setImageResource(R.drawable.ic_baseline_push_pin_24)
                }

                //Click Listeners
                sampleRibbonBody.setOnClickListener {
                    findNavController(it).navigate(R.id.show_todo_list)
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

    class DiffCallback: DiffUtil.ItemCallback<ListModel>() {
        override fun areItemsTheSame(oldItem: ListModel, newItem: ListModel) =
            oldItem.ListId == newItem.ListId

        override fun areContentsTheSame(oldItem: ListModel, newItem: ListModel) =
            oldItem.toString() == newItem.toString()

    }
}