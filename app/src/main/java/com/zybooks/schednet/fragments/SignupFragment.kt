package com.zybooks.schednet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.R
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: SignupFragment.kt
 *  @author Matthew Clark
 */

class SignupFragment: Fragment() {
    private lateinit var progressChip: TextView
    private lateinit var nameEdit: TextInputEditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var usernameEdit: TextInputEditText
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var errorLabel: TextView

    private lateinit var questionEdit: TextInputEditText
    private lateinit var questionLayout: TextInputLayout
    private lateinit var questionTwoEdit: TextInputEditText
    private lateinit var questionTwoLayout: TextInputLayout
    private lateinit var answerEdit: TextInputEditText
    private lateinit var answerLayout: TextInputLayout
    private lateinit var answerTwoEdit: TextInputEditText
    private lateinit var answerTwoLayout: TextInputLayout

    private lateinit var backButton: ImageButton
    private lateinit var forwardButton: Button
    private lateinit var pageOne: LinearLayout
    private lateinit var pageTwo: LinearLayout

    private var pageNo: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.signup, container, false)
        progressChip = rootView.findViewById(R.id.signup_progress_label)
        nameEdit = rootView.findViewById(R.id.signup_account_name_edit)
        nameLayout = rootView.findViewById(R.id.signup_account_name_layout)
        usernameEdit = rootView.findViewById(R.id.signup_username_edit)
        usernameLayout = rootView.findViewById(R.id.signup_username_layout)
        passwordEdit = rootView.findViewById(R.id.signup_password_edit)
        passwordLayout = rootView.findViewById(R.id.signup_password_layout)
        errorLabel = rootView.findViewById(R.id.signup_already_exist_error)

        questionEdit = rootView.findViewById(R.id.signup_question_edit)
        questionLayout = rootView.findViewById(R.id.signup_question_layout)
        questionTwoEdit = rootView.findViewById(R.id.signup_question_two_edit)
        questionTwoLayout = rootView.findViewById(R.id.signup_question_two_layout)
        answerEdit = rootView.findViewById(R.id.signup_answer_edit)
        answerLayout = rootView.findViewById(R.id.signup_answer_layout)
        answerTwoEdit = rootView.findViewById(R.id.signup_answer_two_edit)
        answerTwoLayout = rootView.findViewById(R.id.signup_answer_two_layout)

        backButton = rootView.findViewById(R.id.signup_back_button)
        forwardButton = rootView.findViewById(R.id.signup_register_button)
        pageOne = rootView.findViewById(R.id.signup_user_box)
        pageTwo = rootView.findViewById(R.id.signup_question_box)
        initializer()

        return rootView
    }

    private fun initializer() {
        backButton.setOnClickListener {
            backButton.visibility = View.GONE
            progressChip.text = String.format(resources.getString(R.string.signup_progress_format), 1, stackSize)
            forwardButton.text = resources.getString(R.string.signup_forward_text)
            pageTwo.visibility = View.GONE
            pageOne.visibility = View.VISIBLE
            pageNo = 1
        }

        forwardButton.setOnClickListener {
            when(pageNo) {
                1 -> toPageTwo()
                2 -> confirm()
                else -> findNavController().popBackStack()
            }
        }

    }

    private fun confirm() {
        questionLayout.error = ""
        questionTwoLayout.error = ""
        answerLayout.error = ""
        answerTwoLayout.error = ""

        val questionEmpty = questionEdit.text.toString().isEmpty()
        val questionTwoEmpty = questionTwoEdit.text.toString().isEmpty()
        val answerEmpty = answerEdit.text.toString().isEmpty()
        val answerTwoEmpty = answerTwoEdit.text.toString().isEmpty()
        if(questionEmpty || questionTwoEmpty || answerEmpty || answerTwoEmpty) {
            if(questionEmpty) questionLayout.error = "Question one field is empty"
            if(questionTwoEmpty) questionTwoLayout.error = "Question two field is empty"
            if(answerEmpty) answerLayout.error = "Answer one field is empty"
            if(answerTwoEmpty) answerTwoLayout.error = "Answer two field is empty"
            return
        }

        DatabaseManager(requireContext()).insertNewUser(nameEdit.text.toString(), usernameEdit.text.toString(), passwordEdit.text.toString(), questionEdit.text.toString(), questionTwoEdit.text.toString(), answerEdit.text.toString(), answerTwoEdit.text.toString())
        findNavController().popBackStack()
    }

    private fun toPageTwo() {
        nameLayout.error = ""
        usernameLayout.error = ""
        passwordLayout.error = ""
        errorLabel.visibility = View.GONE

        val nameEmpty = nameEdit.text.toString().isEmpty()
        val userEmpty = usernameEdit.text.toString().isEmpty()
        val passwordEmpty = passwordEdit.text.toString().isEmpty()
        if(nameEmpty || userEmpty || passwordEmpty) {
            if(nameEmpty) nameLayout.error = "Name field is empty"
            if(userEmpty) usernameLayout.error = "Username field is empty"
            if(passwordEmpty) passwordLayout.error = "Password field is empty"
            return
        }

        if(DatabaseManager(requireContext()).checkUserExists(usernameEdit.text.toString())) {
            errorLabel.visibility = View.VISIBLE
            return
        }

        progressChip.text = String.format(resources.getString(R.string.signup_progress_format), 2, stackSize)
        forwardButton.text = resources.getString(R.string.signup_confirm_text)
        backButton.visibility = View.VISIBLE
        pageOne.visibility = View.GONE
        pageTwo.visibility = View.VISIBLE
        pageNo = 2
    }


    init {
        pageNo = 1
    }

    companion object {
        private const val stackSize = 2
    }
}
