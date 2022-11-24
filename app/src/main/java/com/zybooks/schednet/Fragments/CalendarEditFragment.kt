package com.zybooks.schednet.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.Model.CalendarEvent
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.Utils.DateConversions
import java.time.*

class CalendarEditFragment: Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    companion object {
        const val EVENTKEY = "event.ky"
    }

    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var nameEditCasing: TextInputLayout
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var descriptionEditCasing: TextInputLayout
    private lateinit var timeEditText: TextInputEditText
    private lateinit var timeEditCasing: TextInputLayout
    private lateinit var timeButton: ImageButton

    //lateinit var dateButton: ImageButton
    //lateinit var timeButton: ImageButton

    var gId: Int
    var gEventId: Int
    var gObject: CalendarEvent
    var gDateTime: ZonedDateTime
    var gCurrentZone: ZoneId

    init {
        gId = -1
        gEventId = -2
        gObject = CalendarEvent(-1,null,null,null)
        gDateTime = ZonedDateTime.now()
        gCurrentZone = ZoneId.systemDefault()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = getArguments()
        gEventId = arguments?.getInt(EVENTKEY) ?: 0
        val activityArgs = requireActivity().intent.extras
        gId = activityArgs!!.getInt(StageActivity.MAGIC_NUMBER)

        gObject = DatabaseManager(requireContext()).readCalendarEventSingle(gEventId)
        gDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(gObject.eventDateTime), gCurrentZone)

        Toast.makeText(requireContext(), "Current ID: $gEventId", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.calendar_edit, container, false)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        nameEditText = rootView.findViewById(R.id.ribbon_name)
        nameEditCasing = rootView.findViewById(R.id.ribbon_name_casing)
        descriptionEditText = rootView.findViewById(R.id.ribbon_description)
        descriptionEditCasing = rootView.findViewById(R.id.ribbon_description_casing)
        timeEditText = rootView.findViewById(R.id.ribbon_date)
        timeEditCasing = rootView.findViewById(R.id.ribbon_date_casing)
        timeButton = rootView.findViewById(R.id.date_button)


        nameEditText.setText(gObject.eventName)
        descriptionEditText.setText(gObject.eventDescription)
        timeEditText.setText(DateConversions.dateTimeConversion(gDateTime))

        timeButton.setOnClickListener {
            timeButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_date_range_24))
            val tempDate = LocalDate.of(gDateTime.year, gDateTime.monthValue, gDateTime.dayOfMonth)
            try {
                DatePickerDialog(requireContext(), this, tempDate.year, tempDate.monthValue-1, tempDate.dayOfMonth).show()
            } catch(e: Exception) {
                Log.i("text", "DONT YOU DARE!")
            }
        }

        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        confirmButton.setOnClickListener {
            //check if name or date are valie
            nameEditCasing.error = ""
            if(nameEditText.text.toString().isEmpty()) {
                nameEditCasing.error = "Name box is empty"
                return@setOnClickListener
            }

            gObject.eventName = nameEditText.text.toString()
            gObject.eventDescription = descriptionEditText.text.toString()
            gObject.eventDateTime = gDateTime.toInstant().toEpochMilli()
            DatabaseManager(requireContext()).updateCalendarEvent(gObject)
            findNavController().popBackStack()
        }

        return rootView
    }

    override fun onPause() {
        findNavController().popBackStack()
        super.onPause()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val tempTime = LocalTime.of(gDateTime.hour, gDateTime.minute, 0,0)
        this.gDateTime = LocalDateTime.of(year, month+1, dayOfMonth, tempTime.hour, tempTime.minute, 0, 0).atZone(gCurrentZone)

        //val ldt = LocalDateTime.of(year, month+1, dayOfMonth, 0, 0, 0, 0).atZone(gCurrentZone)

        timeEditText.setText(DateConversions.dateTimeConversion(gDateTime))
        timeButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_cancel_24_ico))

        TimePickerDialog(requireContext(), this, tempTime.hour, tempTime.minute, false).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.gDateTime = LocalDateTime.of(gDateTime.year, gDateTime.monthValue, gDateTime.dayOfMonth, hourOfDay, minute, 0, 0).atZone(gCurrentZone)
        //val ldt = LocalDateTime.of(gDateTime!!.year, gDateTime!!.monthValue, gDateTime!!. dayOfMonth,gDateTime!!.hour, gDateTime!!.minute, 0, 0).atZone(gCurrentZone)
        timeEditText.setText(DateConversions.dateTimeConversion(gDateTime))
    }
}