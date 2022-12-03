package com.zybooks.schednet.fragments.bottom_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.zybooks.schednet.model.CalendarDataObject
import com.zybooks.schednet.model.FavoriteDataObject
import com.zybooks.schednet.R
import com.zybooks.schednet.utils.DatabaseManager
import com.zybooks.schednet.utils.DateConversions
import java.time.*

/**
 *  File: AddFavoriteCalendarBottomFragment.kt
 *  @author Matthew Clark
 */

class AddFavoriteCalendarBottomFragment: BottomSheetDialogFragment() {

    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var nameEditCasing: TextInputLayout
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var descriptionEditCasing: TextInputLayout
    private lateinit var descriptionChip: Chip
    private lateinit var dateChip: Chip
    private lateinit var errorText: TextView

    private var gId: Int
    private var gProfileId: Int
    private var currentZone: ZoneId
    private var gTime: ZonedDateTime?

    private lateinit var gProfile: FavoriteDataObject

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.calendar_new_ribbon_frame, container, false)

        if(dialog is BottomSheetDialog) {
            val behaviour = (dialog as BottomSheetDialog).behavior
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            behaviour.isDraggable = false
        }

        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        nameEditText = rootView.findViewById(R.id.new_calendar_name_edit)
        nameEditCasing = rootView.findViewById(R.id.new_calendar_name_layout)
        descriptionEditText = rootView.findViewById(R.id.new_calendar_description_edit)
        descriptionEditCasing = rootView.findViewById(R.id.new_calendar_description_layout)
        descriptionChip = rootView.findViewById(R.id.new_calendar_chip_description)
        dateChip = rootView.findViewById(R.id.new_calendar_chip_date)
        errorText = rootView.findViewById(R.id.new_calendar_error)
        initializer()

        return rootView
    }

    private fun initializer() {
        gProfile = DatabaseManager(requireContext()).readFavoriteCalendarSingle(gProfileId)

        nameEditText.setText(gProfile.eventName)
        descriptionEditText.setText(gProfile.eventDescription)

        descriptionChip.setOnClickListener {
            descriptionEditCasing.isVisible = !descriptionEditCasing.isVisible
        }

        dateChip.setOnClickListener {
            if(gTime != null) {
                gTime = null
                dateChip.apply {
                    text = resources.getString(R.string.new_chip_date)
                    isCloseIconVisible = false
                }
                return@setOnClickListener
            }

            callMaterialDatePicker()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        confirmButton.setOnClickListener {
            nameEditCasing.error = ""
            errorText.visibility = View.GONE

            val empty = nameEditText.text.toString().isEmpty()
            val unSet = gTime == null
            if(empty || unSet) {
                if(empty) nameEditCasing.error = "Name field is empty"
                if(unSet) errorText.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val calendarEvent = CalendarDataObject(0, nameEditText.text.toString(), descriptionEditText.text.toString(), gTime?.toInstant()?.toEpochMilli())
            DatabaseManager(requireContext()).insertCalendarEvent(gId, calendarEvent)

            dismiss()
        }

    }

    private fun callMaterialDatePicker() {
        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.ThemeOverlay_AppCompat_MaterialCalendar)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        materialDatePicker.addOnPositiveButtonClickListener {
            val result: Long = materialDatePicker.selection ?: 0
            val zonedResult = ZonedDateTime.ofInstant(Instant.ofEpochMilli(result), ZoneOffset.UTC)

            val myZonedDateTime = ZonedDateTime.of(zonedResult.year, zonedResult.monthValue, zonedResult.dayOfMonth, 0,0,0,0, currentZone)
            dateChip.apply {
                text = DateConversions.dateTimeConversion(myZonedDateTime)
                isCloseIconVisible = true
            }
            gTime = myZonedDateTime

            callMaterialTimePicker()
        }

        materialDatePicker.show(childFragmentManager, materialDatePicker.tag)
    }

    private fun callMaterialTimePicker() {
        val tempTime = LocalTime.now()
        val materialTimePicker = MaterialTimePicker.Builder().apply {
            setTimeFormat(TimeFormat.CLOCK_12H)
            setTheme(R.style.ThemeOverlay_AppCompat_MaterialTime)
            setHour(tempTime.hour)
            setMinute(tempTime.minute)
        }.build()

        materialTimePicker.addOnPositiveButtonClickListener {
            gTime = gTime?.plusHours(materialTimePicker.hour.toLong())
                ?.plusMinutes(materialTimePicker.minute.toLong())

            dateChip.text = DateConversions.dateTimeConversion(gTime ?: ZonedDateTime.now())
        }

        materialTimePicker.show(childFragmentManager, materialTimePicker.tag)
    }

    //INIT
    init {
        gId = -1
        gProfileId = -1
        gTime = null
        currentZone = ZoneId.systemDefault()
    }

    fun setId(id: Int) {
        gId = id
    }
    fun setProfileId(profileId: Int) {
        gProfileId = profileId
    }




}