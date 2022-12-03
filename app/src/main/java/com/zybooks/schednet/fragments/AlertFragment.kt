package com.zybooks.schednet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.zybooks.schednet.R

/**
 *  File: AlertFragment.kt
 *  @author Matthew Clark
 */

class AlertFragment: DialogFragment() {

    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton

    private var gOnDismissDialog: OnDismissDialog?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.warning_dialog, container, false)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)

        cancelButton.setOnClickListener {
            dismiss()
        }
        confirmButton.setOnClickListener {
            dismiss()
            gOnDismissDialog?.confirmDelete()
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun dismiss() {
        super.dismiss()
        gOnDismissDialog?.dismissDelete()
    }

    //INIT
    init {
        gOnDismissDialog = null
    }

    fun setInterface(onDismissDialog: OnDismissDialog) {
        gOnDismissDialog = onDismissDialog
    }

    interface OnDismissDialog {
        fun dismissDelete()
        fun confirmDelete()
    }
}