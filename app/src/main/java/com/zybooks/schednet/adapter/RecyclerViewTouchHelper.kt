package com.zybooks.schednet.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.R

/**
 *  File: RecyclerViewTouchHelper.kt
 *  @author Matthew Clark
 */

class RecyclerViewTouchHelper(private val context: Context, private val swipeInteraction: OnSwipeInteraction):

    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(@NonNull recyclerView: RecyclerView, @NonNull viewHolder: RecyclerView.ViewHolder,
                        @NonNull target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position: Int = viewHolder.bindingAdapterPosition
        if(direction == ItemTouchHelper.LEFT) {
            Log.i("clickTest", "Swipe @ position: $position")
            swipeInteraction.onSwipeLeft(position)
        }
    }

    override fun onChildDraw(@NonNull c: Canvas, recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
            actionState: Int, isCurrentlyActive: Boolean) {

        super.onChildDraw( c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
         val backgroundCornerOffset = 20

        val icon: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)!!
        val background = ColorDrawable(Color.RED)


        //calculations?
        val iconMargin: Int = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop: Int = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom: Int = iconTop + icon.intrinsicHeight

        if (dX < 0) { //Swipe left
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(((itemView.height + dX - backgroundCornerOffset).toInt()),
                itemView.top, itemView.right, itemView.bottom)
        }

        background.draw(c)
        icon.draw(c)
    }

    interface OnSwipeInteraction {
        fun onSwipeLeft(position: Int)
    }

}