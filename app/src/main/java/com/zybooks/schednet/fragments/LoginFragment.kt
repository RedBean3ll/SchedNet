package com.zybooks.schednet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import android.content.Intent
import android.widget.Button
import android.widget.CheckBox
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: LoginFragment.kt
 *  @author Matthew Clark
 */

class LoginFragment: Fragment() {

    private lateinit var usernameEdit: TextInputEditText
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var rememberCheckBox: CheckBox
    private lateinit var forgotButton: Button
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.login, container, false)
        usernameEdit = rootView.findViewById(R.id.login_username_edit)
        usernameLayout = rootView.findViewById(R.id.login_username_layout)
        passwordEdit = rootView.findViewById(R.id.login_password_edit)
        passwordLayout = rootView.findViewById(R.id.login_password_layout)
        rememberCheckBox = rootView.findViewById(R.id.login_check_box_remember)
        forgotButton = rootView.findViewById(R.id.login_forgot_button)
        registerButton = rootView.findViewById(R.id.login_register_button)
        loginButton = rootView.findViewById(R.id.login_button)
        initializer()

        return rootView
    }

    fun initializer() {
        registerButton.setOnClickListener { findNavController().navigate(R.id.show_signup) }
        forgotButton.setOnClickListener { findNavController().navigate(R.id.show_forgot) }

        loginButton.setOnClickListener {
            usernameLayout.error = ""
            passwordLayout.error = ""

            val usernameEmpty = usernameEdit.text.toString().length
            val passwordEmpty = passwordEdit.text.toString().length
            if(usernameEmpty == 0 || passwordEmpty == 0 ) {
                if(usernameEmpty == 0) usernameLayout.error = "Username field is empty"
                if(passwordEmpty == 0) passwordLayout.error = "Password field is empty"
                return@setOnClickListener
            }

            val attempt = DatabaseManager(requireContext()).readUser(usernameEdit.text.toString(), passwordEdit.text.toString())
            if(attempt > -1) {
                if(rememberCheckBox.isChecked) {
                    DatabaseManager(requireContext()).updatePreferredLogin(attempt, true)
                }

                val intent = Intent(activity, StageActivity::class.java)
                intent.putExtra(StageActivity.MAGIC_NUMBER, attempt)
                startActivity(intent)
            }
        }
    }
}


