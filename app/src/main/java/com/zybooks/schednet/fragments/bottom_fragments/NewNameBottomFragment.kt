package com.zybooks.schednet.fragments.bottom_fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.R
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: NewNameBottomFragment.kt
 *  @author Matthew Clark
 */

class NewNameBottomFragment: BottomSheetDialogFragment() {

    private lateinit var nameEdit: TextInputEditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton

    private var gOnDismissName: OnDismissName?
    private var gId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.name_new_ribbon_frame, container, false)
        nameEdit = rootView.findViewById(R.id.new_name_edit)
        nameLayout = rootView.findViewById(R.id.new_name_layout)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        initializer()

        return rootView
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        gOnDismissName?.dismissName()
    }

    init {
        gOnDismissName = null
        gId = -1
    }

    fun setInterface(onDismissName: OnDismissName) {
        gOnDismissName = onDismissName
    }
    fun setId(id: Int) {
        gId = id
    }

    private fun initializer() {
        cancelButton.setOnClickListener {
            dismiss()
        }

        confirmButton.setOnClickListener {
            if(nameEdit.text.toString().isEmpty()) return@setOnClickListener

            DatabaseManager(requireContext()).updateAccountName(gId, nameEdit.text.toString())
            gOnDismissName?.confirmName(nameEdit.text.toString())
            dismiss()
        }
    }

    interface OnDismissName {
        fun dismissName()
        fun confirmName(newName: String)
    }
}