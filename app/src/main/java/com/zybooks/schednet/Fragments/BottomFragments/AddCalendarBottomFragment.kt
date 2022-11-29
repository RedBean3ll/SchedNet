package com.zybooks.schednet.Fragments.BottomFragments

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
import com.zybooks.schednet.Model.CalendarDataObject
import com.zybooks.schednet.Model.CalendarEvent
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.Utils.DateConversions
import java.time.*

/**
 * If chip box filled, change color of chip on action of value updated
 */

class AddCalendarBottomFragment(): BottomSheetDialogFragment() {

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
    private var onPageDismiss: onDismissInteraction?

    private var currentZone = ZoneId.systemDefault()


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
        intitializer()

        return rootView
    }

    private fun intitializer() {
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
            gTime?.toInstant()?.toEpochMilli()

            val obj = CalendarDataObject(0, nameEditText.text.toString(), descriptionEditText.text.toString(), gTime?.toInstant()?.toEpochMilli())
            DatabaseManager(requireContext()).insertCalendarEvent(gId, obj)

            onPageDismiss?.confirmChange(gTime!!)
            dismiss()
        }

        descriptionChip.setOnClickListener {
            descriptionEditCasing.isVisible = !descriptionEditCasing.isVisible
        }
        
        dateChip.setOnClickListener {
            this.gTime = null
            dateChip.text = "Date"
            dateChip.isCloseIconVisible = false

            callMaterialDatePicker()
        }

    }

    fun callMaterialDatePicker() {
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

    fun callMaterialTimePicker() {
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
    interface onDismissInteraction {
        fun confirmChange(dateTime: ZonedDateTime)
        fun cancelChange()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onPageDismiss?.cancelChange()
    }

    fun setInterface(onPageDismiss: onDismissInteraction) {
        this.onPageDismiss = onPageDismiss
    }

    fun setId(id: Int) {
        gId = id
    }


}