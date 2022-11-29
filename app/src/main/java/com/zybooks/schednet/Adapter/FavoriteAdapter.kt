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
import com.zybooks.schednet.Model.FavoriteObject
import com.zybooks.schednet.R

class FavoriteAdapter(@Nullable private val onRibbonListener: OnRibbonListener?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback: DiffUtil.ItemCallback<FavoriteObject> = object: DiffUtil.ItemCallback<FavoriteObject>() {
        override fun areItemsTheSame(oldItem: FavoriteObject, newItem: FavoriteObject): Boolean {
            return "${oldItem.favoriteId}:${oldItem.favoriteType}" == "${newItem.favoriteId}:${newItem.favoriteType}"
        }
        override fun areContentsTheSame(oldItem: FavoriteObject, newItem: FavoriteObject): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
    private val differ: AsyncListDiffer<FavoriteObject> = AsyncListDiffer(this@FavoriteAdapter, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_ribbon_favorite, parent, false), onRibbonListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is FavoriteViewHolder) holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    fun submitNewList(list: List<FavoriteObject>) {
        differ.submitList(list)
    }

    class FavoriteViewHolder constructor(
        self: View, @Nullable private val onRibbonListener: OnRibbonListener?
    ): RecyclerView.ViewHolder(self) {

        fun bind(favorite: FavoriteObject) {
            itemView.setOnClickListener {onRibbonListener?.onClick(absoluteAdapterPosition, favorite)}
            val label: TextView
            val addButton: ImageButton
            val pinButton: ImageButton
            label = itemView.findViewById(R.id.sample_ribbon_title)
            addButton = itemView.findViewById(R.id.sample_ribbon_add_action)
            pinButton = itemView.findViewById(R.id.sample_ribbon_pin)
            label.text = favorite.favoriteName

            addButton.setOnClickListener {
                onRibbonListener?.onAddClick(absoluteAdapterPosition, favorite)
            }

            if(favorite.isPinned) pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
            else pinButton.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)

            pinButton.setOnClickListener {
                favorite.isPinned = !favorite.isPinned
                onRibbonListener?.onPin(absoluteAdapterPosition, favorite)

                if(favorite.isPinned) pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
                else { pinButton.setImageResource(R.drawable.ic_baseline_push_pin_alt_24) }
            }
        }
    }

    interface OnRibbonListener {
        fun onClick(position: Int, favorite: FavoriteObject)
        fun onAddClick(position: Int, favorite: FavoriteObject)
        fun onPin(position: Int, favorite: FavoriteObject)
    }
}