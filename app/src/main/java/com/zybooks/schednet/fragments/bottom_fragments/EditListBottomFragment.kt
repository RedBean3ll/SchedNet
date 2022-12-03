package com.zybooks.schednet.fragments.bottom_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.model.ListObject
import com.zybooks.schednet.R
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: EditListBottomFragment.kt
 *  @author Matthew Clark
 */

class EditListBottomFragment: BottomSheetDialogFragment() {

    private lateinit var listEditText: TextInputEditText
    private lateinit var listEditLayout: TextInputLayout
    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton

    private lateinit var gOnUpdateCompleted: OnDismissEditInteraction
    private lateinit var gList: ListObject
    private var gPosition: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.list_edit_ribbon_frame, container, false)

        if(dialog is BottomSheetDialog) {
            val behaviour = (dialog as BottomSheetDialog).behavior
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            behaviour.isDraggable = false
        }

        listEditText = rootView.findViewById(R.id.new_list_edit)
        listEditLayout = rootView.findViewById(R.id.new_list_layout)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        initializer()

        return rootView
    }

    init {
        gPosition = -1
    }

    fun setInterface(onUpdateCompleted: OnDismissEditInteraction) {
        gOnUpdateCompleted = onUpdateCompleted
    }
    fun setPosition(position: Int) {
        gPosition = position
    }
    fun setObject(listObject: ListObject) {
        gList = listObject
    }

    private fun initializer() {
        listEditText.setText(gList.listName)

        cancelButton.setOnClickListener {
            dismiss()
        }

        confirmButton.setOnClickListener {
            listEditLayout.error = ""
            if(listEditText.text.toString().isEmpty()) {
                listEditLayout.error = "Name field is empty"
                return@setOnClickListener
            }
            gList.listName = listEditText.text.toString()
            DatabaseManager(requireContext()).updateListName(gList.listId, gList.listName)
            gOnUpdateCompleted.confirmName(gPosition, gList)
            dismiss()
        }
    }

    interface OnDismissEditInteraction {
        fun confirmName(position: Int, listObject: ListObject)
    }



}