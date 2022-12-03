package com.zybooks.schednet.fragments.bottom_fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
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
 *  File: AddListBottomFragment.kt
 *  @author Matthew Clark
 */

class AddListBottomFragment: BottomSheetDialogFragment() {

    private lateinit var listEditText: TextInputEditText
    private lateinit var listEditLayout: TextInputLayout
    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton
    private lateinit var pinButton: ImageButton
    private var gId: Int
    private var pinned: Boolean
    private var onDismissInteraction: OnDismissAddInteraction?

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

    //RESET CONTROL
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissInteraction?.cancelAddition()
    }

    //INIT
    init {
        gId = -1
        pinned = false
        this.onDismissInteraction = null
    }

    fun setInterface(onDismissInteraction: OnDismissAddInteraction) {
        this.onDismissInteraction = onDismissInteraction
    }
    fun setId(id: Int) {
        gId = id
    }

    private fun initializer() {
        cancelButton.setOnClickListener {
            dismiss()
        }

        confirmButton.setOnClickListener {
            if(listEditText.text.toString().isNotEmpty()) {
                val creationStamp: Long
                val obj = ListObject(0, listEditText.text.toString(), pinned)

                creationStamp = DatabaseManager(requireContext()).insertList(obj, gId)
                obj.listId = DatabaseManager(requireContext()).readNewListId(gId, creationStamp)

                onDismissInteraction?.confirmAddition(obj)
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

    interface OnDismissAddInteraction {
        fun confirmAddition(newList: ListObject)
        fun cancelAddition()
    }
}

