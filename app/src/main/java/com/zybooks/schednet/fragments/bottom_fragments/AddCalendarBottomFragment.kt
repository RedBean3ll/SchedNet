package com.zybooks.schednet.fragments.bottom_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.R
import android.content.DialogInterface
import android.widget.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.zybooks.schednet.model.CalendarDataObject
import com.zybooks.schednet.utils.DatabaseManager
import com.zybooks.schednet.utils.DateConversions
import java.time.*

/**
 *  File: AddCalendarBottomFragment.kt
 *  @author Matthew Clark
 */

class AddCalendarBottomFragment: BottomSheetDialogFragment() {

    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var nameEditCasing: TextInputLayout
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var descriptionEditCasing: TextInputLayout
    private lateinit var descriptionChip: Chip
    private lateinit var dateChip: Chip
    private lateinit var errorText: TextView

    private var gTime: ZonedDateTime?
    private var gId: Int
    private var onPageDismiss: OnDismissInteraction?

    init {
        gId = -1
        onPageDismiss = null
        gTime = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        cancelButton.setOnClickListener {
            onPageDismiss?.cancelChange()
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

            val obj = CalendarDataObject(0, nameEditText.text.toString(), descriptionEditText.text.toString(), gTime?.toInstant()?.toEpochMilli())
            DatabaseManager(requireContext()).insertCalendarEvent(gId, obj)

            onPageDismiss?.confirmChange(gTime!!)
            dismiss()
        }

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

            val myZonedDateTime = ZonedDateTime.of(zonedResult.year, zonedResult.monthValue, zonedResult.dayOfMonth, 0,0,0,0, ZoneId.systemDefault())
            dateChip.text = DateConversions.dateTimeConversion(myZonedDateTime)
            dateChip.isCloseIconVisible = true
            gTime = myZonedDateTime

            callMaterialTimePicker()
        }

        materialDatePicker.show(childFragmentManager, "TAG")
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
            gTime = gTime?.plusHours(materialTimePicker.hour.toLong())
                ?.plusMinutes(materialTimePicker.minute.toLong())
            dateChip.text = DateConversions.dateTimeConversion(gTime ?: ZonedDateTime.now())
        }

        materialTimePicker.show(childFragmentManager, "AG")
    }

    //Interface
    interface OnDismissInteraction {
        fun confirmChange(dateTime: ZonedDateTime)
        fun cancelChange()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onPageDismiss?.cancelChange()
    }

    fun setInterface(onPageDismiss: OnDismissInteraction) {
        this.onPageDismiss = onPageDismiss
    }

    fun setId(id: Int) {
        gId = id
    }


}