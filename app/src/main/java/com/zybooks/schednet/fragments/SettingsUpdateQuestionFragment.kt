package com.zybooks.schednet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: SettingsUpdateQuestionFragment.kt
 *  @author Matthew Clark
 */

class SettingsUpdateQuestionFragment: Fragment() {
    private lateinit var questionEdit: TextInputEditText
    private lateinit var questionLayout: TextInputLayout
    private lateinit var questionTwoEdit: TextInputEditText
    private lateinit var questionTwoLayout: TextInputLayout
    private lateinit var answerEdit: TextInputEditText
    private lateinit var answerLayout: TextInputLayout
    private lateinit var answerTwoEdit: TextInputEditText
    private lateinit var answerTwoLayout: TextInputLayout
    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton

    private var gId: Int

    init {
        gId = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityIntent = requireActivity().intent.extras
        gId = activityIntent?.getInt(StageActivity.MAGIC_NUMBER) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.settings_questions_edit, container, false)
        questionEdit = rootView.findViewById(R.id.signup_question_edit)
        questionLayout = rootView.findViewById(R.id.signup_question_layout)
        questionTwoEdit = rootView.findViewById(R.id.signup_question_two_edit)
        questionTwoLayout = rootView.findViewById(R.id.signup_question_two_layout)
        answerEdit = rootView.findViewById(R.id.signup_answer_edit)
        answerLayout = rootView.findViewById(R.id.signup_answer_layout)
        answerTwoEdit = rootView.findViewById(R.id.signup_answer_two_edit)
        answerTwoLayout = rootView.findViewById(R.id.signup_answer_two_layout)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        initializer()

        return rootView
    }

    private fun initializer() {
        cancelButton.setOnClickListener { findNavController().popBackStack() }
        confirmButton.setOnClickListener {
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
                return@setOnClickListener
            }

            DatabaseManager(requireContext()).updateQuestions(gId, listOf(questionEdit.text.toString(), answerEdit.text.toString(), questionTwoEdit.text.toString(), answerTwoEdit.text.toString()))
            findNavController().popBackStack()
        }
    }
}