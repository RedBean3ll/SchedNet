package com.zybooks.schednet.Adapter.AdapterTouchHelper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Adapter.MenuAdapter
import com.zybooks.schednet.R


class RecyclerListTouchHelper(adapterIn: MenuAdapter):
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private lateinit var adapter: MenuAdapter;

    init {
        adapter = adapterIn;
    }

    override fun onMove(@NonNull recyclerView: RecyclerView, @NonNull viewHolder: RecyclerView.ViewHolder,
                        @NonNull target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    //TODO MAY NEED SOME ADJUSTMENTS
    override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position: Int = viewHolder.bindingAdapterPosition
        if(direction == ItemTouchHelper.LEFT) {
            adapter.removeAt(position)
        }
        else {

            //EditItem?
        }
    }

    override fun onChildDraw(@NonNull c: Canvas, recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
            actionState: Int, isCurrentlyActive: Boolean) {

        super.onChildDraw( c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

         var icon: Drawable
         var background: ColorDrawable

         var itemView = viewHolder.itemView
         val backgroundCornerOffset: Int = 20

        //TODO: TINKER WITH COLOR LATER
        if(dX > 0) {
            icon = ContextCompat.getDrawable(adapter.getContext(),
                R.drawable.ic_menu_dot_settings_ico
            )!!
            background = ColorDrawable(Color.GREEN)
        }
        else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete_24)!!
            background = ColorDrawable(Color.RED)
        }

        //calculations?
        var iconMargin: Int = (itemView.height - icon.intrinsicHeight) / 2
        var iconTop: Int = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        var iconBottom: Int = iconTop + icon.intrinsicHeight

        if(dX > 0) { //Swipe right

        } else if (dX < 0) { //Swipe left
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(((itemView.height + dX - backgroundCornerOffset).toInt()),
                itemView.top, itemView.right, itemView.bottom)
        } else { //Unswiped
            //background.setBounds(0,0,0,0)
        }

        background.draw(c)
        icon.draw(c)

    }

}