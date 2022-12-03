package com.zybooks.schednet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.R
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: ForgotFragment.kt
 *  @author Matthew Clark
 */

class ForgotFragment: Fragment() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var confirmButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.forgot, container, false)
        usernameEditText = rootView.findViewById(R.id.forgot_signup_edit)
        usernameLayout = rootView.findViewById(R.id.forgot_signup_layout)
        confirmButton = rootView.findViewById(R.id.forgot_continue_button)
        initializer()

        return rootView
    }

    private fun initializer() {
        confirmButton.setOnClickListener {
            usernameLayout.error = ""
            if(usernameEditText.text.toString().isEmpty()) {
                usernameLayout.error = "Username field is empty"
                return@setOnClickListener
            }

            val exists = DatabaseManager(requireContext()).readUserByName(usernameEditText.text.toString())
            if(exists < 1) {
                usernameLayout.error = "Invalid login"
                return@setOnClickListener
            }

            val bundle = Bundle()
            bundle.putInt(ForgotQuestionFragment.USE_KEY, exists)
            findNavController().navigate(R.id.show_forgot_question, bundle)
        }
    }
}