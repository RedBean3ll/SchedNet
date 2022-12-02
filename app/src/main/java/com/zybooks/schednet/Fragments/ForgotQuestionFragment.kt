package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.Fragments.BottomFragments.NewPasswordBottomFragment
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager

class ForgotQuestionFragment: Fragment(), NewPasswordBottomFragment.OnDismissAction {

    private lateinit var continueButton: Button
    private lateinit var questionOneLabel: TextView
    private lateinit var questionTwoLabel: TextView
    private lateinit var answerOneEdit: TextInputEditText
    private lateinit var answerOneLayout: TextInputLayout
    private lateinit var answerTwoEdit: TextInputEditText
    private lateinit var answerTwoLayout: TextInputLayout
    private lateinit var inputFailError: TextView

    private var gId: Int
    private var control: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        gId = args?.getInt(USE_KEY) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.forgot_question, container, false)
        continueButton = rootView.findViewById(R.id.forgot_continue_button)
        questionOneLabel = rootView.findViewById(R.id.forgot_question)
        questionTwoLabel = rootView.findViewById(R.id.forgot_question_two)
        answerOneEdit = rootView.findViewById(R.id.forgot_answer_edit)
        answerOneLayout = rootView.findViewById(R.id.forgot_answer_layout)
        answerTwoEdit = rootView.findViewById(R.id.forgot_answer_two_edit)
        answerTwoLayout = rootView.findViewById(R.id.forgot_answer_two_layout)
        inputFailError = rootView.findViewById(R.id.forgot_incorrect_error)
        initializer()

        return rootView
    }

    init {
        gId = -1
        control = true
    }

    fun initializer() {
        val questions = DatabaseManager(requireContext()).readSecurityQuestion(gId)
        questionOneLabel.text = String.format(resources.getString(R.string.forgot_question_format), 1, questions[0])
        questionTwoLabel.text = String.format(resources.getString(R.string.forgot_question_format), 2, questions[1])

        continueButton.setOnClickListener {
            answerOneLayout.error = ""
            answerTwoLayout.error = ""
            inputFailError.visibility = View.GONE

            val oneEmpty = answerOneEdit.text.toString().isEmpty()
            val twoEmpty = answerTwoEdit.text.toString().isEmpty()
            if(oneEmpty || twoEmpty) {
                if(oneEmpty) answerOneLayout.error = "First answer field is empty"
                if(twoEmpty) answerTwoLayout.error = "Second answer field is empty"
                return@setOnClickListener
            }

            if(!DatabaseManager(requireContext()).checkQuestions(gId, answerOneEdit.text.toString(), answerTwoEdit.text.toString())) {
                inputFailError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if(control) {
                val bottomDialog = NewPasswordBottomFragment()
                bottomDialog.id = gId
                bottomDialog.setInterface(this@ForgotQuestionFragment)
                bottomDialog.show(childFragmentManager, bottomDialog.tag)
            }
        }
    }


    override fun dismissAction() {
        control = true
    }

    override fun cancelAction() {
        findNavController().popBackStack()
    }

    override fun confirmAction() {
        findNavController().popBackStack(R.id.login_fragment, false)
    }

    companion object {
        const val USE_KEY = "user.ky"
    }
}