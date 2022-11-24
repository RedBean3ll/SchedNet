package com.zybooks.schednet.Fragments.BottomFragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.Model.TodoEvent
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager

/**
 * If chip box filled, change color of chip on action of value updated
 */

class AddTodoBottomFragment(): BottomSheetDialogFragment() {
    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var nameEditCasing: TextInputLayout
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var descriptionEditCasing: TextInputLayout
    private lateinit var descriptionChip: Chip

    private lateinit var pinButton: ImageButton

    private var pinned: Boolean
    private var gId: Int
    private var gListId: Int
    private var onPageDismiss: onDismissInteraction?


    init {
        pinned = false
        gId = -1
        gListId = -1
        this.onPageDismiss = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.todo_new_ribbon_frame, container, false)

        if(dialog is BottomSheetDialog) {
            val behaviour = (dialog as BottomSheetDialog).behavior
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            behaviour.isDraggable = false
        }

        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        nameEditText = rootView.findViewById(R.id.new_todo_name_edit)
        nameEditCasing = rootView.findViewById(R.id.new_todo_name_layout)
        descriptionEditText = rootView.findViewById(R.id.new_todo_description_edit)
        descriptionEditCasing = rootView.findViewById(R.id.new_todo_description_layout)
        descriptionChip = rootView.findViewById(R.id.new_todo_chip_description)
        pinButton = rootView.findViewById(R.id.new_todo_pin)

        intitializeControls()
        return rootView
    }

    private fun intitializeControls() {
        cancelButton.setOnClickListener {
            onPageDismiss?.cancelChange()
            dismiss()
        }

        confirmButton.setOnClickListener {
            if(nameEditText.text.toString().isNotEmpty()) {
                val obj = TodoEvent()
                obj.eventName = nameEditText.text.toString()
                obj.eventDescription = descriptionEditText.text.toString()
                obj.isPinned = pinned
                DatabaseManager(requireContext()).insertTodo(gId, gListId, obj)

                obj.eventId = DatabaseManager(requireContext()).readNewTodoId(gId, gListId, obj.eventTimestamp)
                onPageDismiss?.confirmChange(obj)
                dismiss()
            } else {
                nameEditCasing.error = "Name field is empty"
            }
        }

        descriptionChip.setOnClickListener {
            toggleDescriptionVisible()
        }

        pinButton.setOnClickListener {
            pinned = !pinned
            if(pinned) {
                pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
            } else {
                pinButton.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onPageDismiss?.cancelChange()
    }

    //Guarantee flip
    private fun toggleDescriptionVisible() {
        descriptionEditCasing.isVisible = !descriptionEditCasing.isVisible
    }

    interface onDismissInteraction {
        fun confirmChange(todoEvent: TodoEvent)
        fun cancelChange()
    }

    fun setInterface(onPageDismiss: onDismissInteraction) {
        this.onPageDismiss = onPageDismiss
    }

    fun setId(id: Int) {
        gId = id
    }
    fun setListId(listId: Int) {
        gListId = listId
    }


}