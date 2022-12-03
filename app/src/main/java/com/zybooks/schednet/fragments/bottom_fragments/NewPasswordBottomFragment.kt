package com.zybooks.schednet.fragments.bottom_fragments

import android.content.DialogInterface
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
import com.zybooks.schednet.R
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: NewPasswordBottomFragment.kt
 *  @author Matthew Clark
 */

class NewPasswordBottomFragment: BottomSheetDialogFragment() {

    private lateinit var passwordEdit: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton

    private var gOnDismissAction: OnDismissAction?
    private var gId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.new_password_frame, container, false)

        if(dialog is BottomSheetDialog) {
            val behaviour = (dialog as BottomSheetDialog).behavior
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            behaviour.isDraggable = false
        }

        passwordEdit = rootView.findViewById(R.id.signup_password_edit)
        passwordLayout = rootView.findViewById(R.id.signup_password_layout)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        initializer()

        return rootView
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        gOnDismissAction?.dismissAction()
    }

    private fun initializer() {
        cancelButton.setOnClickListener {
            gOnDismissAction?.cancelAction()
            dismiss()
        }

        confirmButton.setOnClickListener {
            passwordLayout.error = ""
            if(passwordEdit.text.toString().isEmpty()) {
                passwordLayout.error = "Password field is empty"
                return@setOnClickListener
            }

            DatabaseManager(requireContext()).updatePassword(gId, passwordEdit.text.toString())
            gOnDismissAction?.confirmAction()
            dismiss()
        }
    }

    init {
        gId = -1
        gOnDismissAction = null
    }

    fun setId(id: Int) {
        this.gId = id
    }
    fun setInterface(onDismissAction: OnDismissAction) {
        gOnDismissAction = onDismissAction
    }

    interface OnDismissAction {
        fun dismissAction()
        fun cancelAction()
        fun confirmAction()
    }
}