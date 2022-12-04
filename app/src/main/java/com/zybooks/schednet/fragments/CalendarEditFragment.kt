package com.zybooks.schednet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.zybooks.schednet.model.CalendarEvent
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.utils.DatabaseManager
import com.zybooks.schednet.utils.DateConversions
import java.time.*

/**
 *  File: CalendarEditFragment.kt
 *  @author Matthew Clark
 */

class CalendarEditFragment: Fragment() {

    companion object {
        const val EVENT_KEY = "event.ky"
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

    private var gId: Int
    private var gEventId: Int
    private var gObject: CalendarEvent
    private var gDateTime: ZonedDateTime?
    private var gCurrentZone: ZoneId

    init {
        gId = -1
        gEventId = -2
        gObject = CalendarEvent(-1,null,null,null)
        gDateTime = ZonedDateTime.now()
        gCurrentZone = ZoneId.systemDefault()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        gEventId = arguments?.getInt(EVENT_KEY) ?: 0
        val activityArgs = requireActivity().intent.extras
        gId = activityArgs!!.getInt(StageActivity.MAGIC_NUMBER)

        gObject = DatabaseManager(requireContext()).readCalendarEventSingle(gEventId)
        gDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(gObject.eventDateTime), gCurrentZone)
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
        timeEditText.setText(DateConversions.dateTimeConversion(gDateTime ?: ZonedDateTime.now()))
        timeButton.setImageResource(R.drawable.ic_cancel_24_ico)

        timeButton.setOnClickListener {
            timeButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_date_range_24))
            timeEditText.setText("")
            timeButton.setImageResource(R.drawable.ic_baseline_date_range_24)
            callMaterialDatePicker()
        }

        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        confirmButton.setOnClickListener {
            nameEditCasing.error = ""
            if(nameEditText.text.toString().isEmpty()) {
                nameEditCasing.error = "Name box is empty"
                return@setOnClickListener
            }

            gObject.eventName = nameEditText.text.toString()
            gObject.eventDescription = descriptionEditText.text.toString()
            gObject.eventDateTime = gDateTime?.toInstant()?.toEpochMilli() ?: 0
            DatabaseManager(requireContext()).updateCalendarEvent(gId, gObject)
            findNavController().popBackStack()
        }

        return rootView
    }

    private fun callMaterialDatePicker() {
        val materialDatePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTheme(R.style.ThemeOverlay_AppCompat_MaterialCalendar)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        materialDatePicker.addOnPositiveButtonClickListener {
            val result: Long = materialDatePicker.selection!!
            val zonedResult = ZonedDateTime.ofInstant(Instant.ofEpochMilli(result), ZoneOffset.UTC)

            timeButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_cancel_24_ico))
            val myZonedDateTime = ZonedDateTime.of(zonedResult.year, zonedResult.monthValue, zonedResult.dayOfMonth, 0,0,0,0, ZoneId.systemDefault())
            timeEditText.setText(DateConversions.dateTimeConversion(myZonedDateTime))
            gDateTime = myZonedDateTime

            callMaterialTimePicker()
        }

        materialDatePicker.show(childFragmentManager, materialDatePicker.tag)
    }

    private fun callMaterialTimePicker() {
        val tempTime = LocalTime.now()

        val materialTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(tempTime.hour)
            .setMinute(tempTime.minute)
            .setTheme(R.style.ThemeOverlay_AppCompat_MaterialTime)
            .build()
        materialTimePicker.addOnPositiveButtonClickListener {
            gDateTime = gDateTime?.plusHours(materialTimePicker.hour.toLong())
                ?.plusMinutes(materialTimePicker.minute.toLong())
            timeEditText.setText(DateConversions.dateTimeConversion(gDateTime ?: ZonedDateTime.now()))
        }

        materialTimePicker.show(childFragmentManager, materialTimePicker.tag)
    }
}