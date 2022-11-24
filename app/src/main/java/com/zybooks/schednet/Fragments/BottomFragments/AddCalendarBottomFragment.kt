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
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.util.Log
import android.widget.*
import com.zybooks.schednet.Model.CalendarEvent
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.Utils.DateConversions
import java.time.*

/**
 * If chip box filled, change color of chip on action of value updated
 */

class AddCalendarBottomFragment(): BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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
    private var gTime: LocalDateTime?
    private var onPageDismiss: onDismissInteraction?

    private var currentZone = ZoneId.systemDefault()


    init {
        gId = -1
        gTime = null
        onPageDismiss = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        intitializeControls()
        return rootView
    }

    private fun intitializeControls() {
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
            gTime?.atZone(currentZone)?.toInstant()?.toEpochMilli()

            val obj = CalendarEvent(0, nameEditText.text.toString(), descriptionEditText.text.toString(), gTime?.atZone(currentZone)?.toInstant()?.toEpochMilli())
            DatabaseManager(requireContext()).insertCalendarEvent(gId, obj)

            onPageDismiss?.confirmChange()
            dismiss()
        }

        descriptionChip.setOnClickListener {
            toggleDescriptionVisible()
        }
        
        dateChip.setOnClickListener {
            this.gTime = null
            dateChip.text = "Date"
            dateChip.isCloseIconVisible = false
            val tempDate = LocalDate.now()
            DatePickerDialog(requireContext(), this, tempDate.year, tempDate.monthValue-1, tempDate.dayOfMonth).show()
        }

    }

    //Guarantee flip
    private fun toggleDescriptionVisible() {
        descriptionEditCasing.isVisible = !descriptionEditCasing.isVisible
    }

    //Interface
    interface onDismissInteraction {
        fun confirmChange()
        fun cancelChange()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onPageDismiss?.cancelChange()
    }

    //Interface Callbacks
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val tempTime = LocalTime.now()
        this.gTime = LocalDateTime.of(year, month+1, dayOfMonth, tempTime.hour, tempTime.minute, 0, 0)

        val ldt = LocalDateTime.of(year, month+1, dayOfMonth, 0, 0, 0, 0). atZone(currentZone)

        dateChip.text = DateConversions.dateTimeConversion(ldt)
        dateChip.isCloseIconVisible = true
        TimePickerDialog(requireContext(), this, tempTime.hour, tempTime.minute, false).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //Toast.makeText(requireContext(), "callback time called", Toast.LENGTH_LONG).show()
        this.gTime = LocalDateTime.of(gTime!!.year, gTime!!.monthValue, gTime!!.dayOfMonth, hourOfDay, minute, 0, 0)
        val ldt = LocalDateTime.of(gTime!!.year, gTime!!.monthValue, gTime!!. dayOfMonth,gTime!!.hour, gTime!!.minute, 0, 0).atZone(currentZone)
        dateChip.text = DateConversions.dateTimeConversion(ldt)
        dateChip.isCloseIconVisible = true

        Log.i("TimeTest", "Timestamp: ${ldt.toInstant().toEpochMilli()}")

    }

    fun setInterface(onPageDismiss: onDismissInteraction) {
        this.onPageDismiss = onPageDismiss
    }

    fun setId(id: Int) {
        gId = id
    }


}