package com.zybooks.schednet.Fragments.BottomFragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.Model.TodoList
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager

class AddListBottomFragment(): BottomSheetDialogFragment() {

    private lateinit var listEditText: TextInputEditText
    private lateinit var listEditLayout: TextInputLayout
    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton
    private lateinit var pinButton: ImageButton
    private var gId: Int
    private var pinned: Boolean
    private var onDismissInteraction: OnDismissInteraction?

    init {
        gId = -1
        pinned = false
        this.onDismissInteraction = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.list_new_ribbon_frame, container, false)

        if(dialog is BottomSheetDialog) {
            val behaviour = (dialog as BottomSheetDialog).behavior
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            behaviour.isDraggable = false
        }

        listEditText = rootView.findViewById(R.id.new_list_edit)
        listEditLayout = rootView.findViewById(R.id.new_list_layout)
        pinButton = rootView.findViewById(R.id.new_list_pin)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        initializer()

        return rootView
    }

    private fun initializer() {
        cancelButton.setOnClickListener {
            dismiss()
        }

        confirmButton.setOnClickListener {
            if(listEditText.text.toString().isNotEmpty()) {
                val obj = TodoList(0, listEditText.text.toString(), pinned, null)
                DatabaseManager(requireContext()).insertList(obj, gId)

                obj.listId = DatabaseManager(requireContext()).readNewListId(gId, obj.timestamp)
                onDismissInteraction?.confirmChange(obj)
                dismiss()
            } else {
                listEditLayout.error = "Name field is empty"
            }
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
        onDismissInteraction?.cancelChange()
    }

    interface OnDismissInteraction {
        fun confirmChange(listModel: TodoList)
        fun cancelChange()
    }

    fun setInterface(onDismissInteraction: OnDismissInteraction) {
        this.onDismissInteraction = onDismissInteraction
    }

    fun setId(id: Int) {
        gId = id
    }



}

