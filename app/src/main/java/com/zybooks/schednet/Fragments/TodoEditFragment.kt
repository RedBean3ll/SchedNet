package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.Model.TodoObject
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager

class TodoEditFragment(): Fragment() {

    companion object {
        const val EVENTKEY = "event.ky"
    }

    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton
    private lateinit var eventTitle: TextInputEditText
    private lateinit var eventTitleLayout: TextInputLayout
    private lateinit var eventDescription: TextInputEditText
    private lateinit var eventDescriptionLayout: TextInputLayout
    private lateinit var pinCheckBox: CheckBox

    private lateinit var event: TodoObject
    private var gEventId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        gEventId = args?.getInt(EVENTKEY) ?: -1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.todo_edit, container, false)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        eventTitle = rootView.findViewById(R.id.edit_todo_name_edit)
        eventTitleLayout = rootView.findViewById(R.id.edit_todo_name_layout)
        eventDescription = rootView.findViewById(R.id.edit_todo_description_edit)
        eventDescriptionLayout = rootView.findViewById(R.id.edit_todo_description_layout)
        pinCheckBox = rootView.findViewById(R.id.edit_todo_check_box)
        initializer()

        return rootView
    }

    fun initializer() {
        event = DatabaseManager(requireContext()).readIndividualTodo(gEventId)
        eventTitle.setText(event.eventName)
        eventDescription.setText(event.eventDescription)
        pinCheckBox.isChecked = event.isPinned

        //CLICK LISTENERS
        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        confirmButton.setOnClickListener {
            eventTitleLayout.error = ""
            if(eventTitle.text.toString().isEmpty()) {
                eventTitleLayout.error = "Name field is empty"
                return@setOnClickListener
            }

            event.eventId = gEventId //DO NOT REMOVE!!
            event.eventName = eventTitle.text.toString()
            event.eventDescription = eventDescription.text.toString()
            event.isPinned = pinCheckBox.isChecked
            DatabaseManager(requireContext()).editTodo(event)
            findNavController().popBackStack()
        }
    }

    init {
        gEventId = -1
    }
}