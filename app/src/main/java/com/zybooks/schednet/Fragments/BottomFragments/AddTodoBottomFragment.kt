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
import com.zybooks.schednet.Model.TodoObject
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager

/**
 * If chip box filled, change color of chip on action of value updated
 */

class AddTodoBottomFragment: BottomSheetDialogFragment() {
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
    private var onPageDismiss: OnDismissInteraction?

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
        initializer()

        return rootView
    }

    //Guarantee flip
    private fun toggleDescriptionVisible() {
        descriptionEditCasing.isVisible = !descriptionEditCasing.isVisible
    }

    //RESET CONTROL
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onPageDismiss?.cancelChange()
    }

    //INIT
    init {
        pinned = false
        gId = -1
        gListId = -1
        this.onPageDismiss = null
    }

    fun setInterface(onPageDismiss: OnDismissInteraction) {
        this.onPageDismiss = onPageDismiss
    }
    fun setId(id: Int) {
        gId = id
    }
    fun setListId(listId: Int) {
        gListId = listId
    }

    private fun initializer() {
        cancelButton.setOnClickListener {
            onPageDismiss?.cancelChange()
            dismiss()
        }

        confirmButton.setOnClickListener {
            nameEditCasing.error = ""
            if(nameEditText.text.toString().isEmpty()) {
                nameEditCasing.error = "Name field is empty"
                return@setOnClickListener
            }

            val event = TodoObject(-1, nameEditText.text.toString(), descriptionEditText.text.toString(), false, pinned, null)
            DatabaseManager(requireContext()).insertTodo(gId, gListId, event)

            event.eventId = DatabaseManager(requireContext()).readNewTodoId(gId, gListId, event.eventTimestamp)
            onPageDismiss?.confirmChange(event)
            dismiss()

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

    interface OnDismissInteraction {
        fun confirmChange(newEvent: TodoObject)
        fun cancelChange()
    }
}