package com.zybooks.schednet.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.adapter.CalendarAdapter
import com.zybooks.schednet.fragments.bottom_fragments.AddCalendarBottomFragment
import com.zybooks.schednet.model.CalendarObject
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.utils.DatabaseManager
import com.zybooks.schednet.utils.DateConversions
import java.time.*

/**
 *  File: CalendarFragment.kt
 *  @author Matthew Clark
 *  @author Amanda Thomas
 */

class CalendarFragment: Fragment(), CalendarAdapter.OnCellListener, AddCalendarBottomFragment.OnDismissInteraction {

    private lateinit var controlLabel: TextView
    private lateinit var recyclerview: RecyclerView
    private lateinit var previousButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private lateinit var listButton: ImageButton
    private lateinit var gAdapter: CalendarAdapter
    private lateinit var newEventButton: ImageButton
    private lateinit var resetDateButton: ImageButton

    private lateinit var calendarMonth: LocalDate
    private var gId: Int
    private var gControl: Boolean
    private lateinit var gList: MutableList<CalendarObject>

    init {
        gId = -1
        gControl = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityIntent = requireActivity().intent.extras
        gId = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)

        //If new session
        val temp: Int = DatabaseManager(requireContext()).readCalendarDate(gId).toInt()
        if(temp == 0) {
            val tempDate = LocalDate.now()
            val zoneDT = ZonedDateTime.of(tempDate.year, tempDate.monthValue, 1, 0, 0, 0,0, ZoneId.systemDefault()).toInstant().toEpochMilli()
            DatabaseManager(requireContext()).updateCalendarDate(gId, zoneDT)
        }

        val tempList = DatabaseManager(requireContext()).readGarbageCollectorValues(gId)
        DatabaseManager(requireContext()).cleanCalendar(gId, DateConversions.getDateStampCalendar(tempList[1]))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.calendar, container, false)
        controlLabel = rootView.findViewById(R.id.control_pane_label)
        recyclerview = rootView.findViewById(R.id.calendar_recyclerview)
        previousButton = rootView.findViewById(R.id.control_pane_left)
        forwardButton = rootView.findViewById(R.id.control_pane_right)
        listButton = rootView.findViewById(R.id.calendar_list)
        newEventButton = rootView.findViewById(R.id.new_calendar_event_button)
        resetDateButton = rootView.findViewById(R.id.calendar_reset_date)

        calendarMonth = ZonedDateTime.ofInstant(Instant.ofEpochMilli(DatabaseManager(requireContext()).readCalendarDate(gId)), ZoneId.systemDefault()).toLocalDate()
        initializer()

        return rootView
    }

    private fun initializer() {
        controlLabel.text = DateConversions.formatMonthYear(calendarMonth)

        recyclerview.apply {
            layoutManager = GridLayoutManager(this.context, 7)
            gAdapter = CalendarAdapter(this@CalendarFragment)
            adapter = gAdapter
            itemAnimator = null
        }

        //GET LIST
        gList = DateConversions.daysInMonth(requireContext(), gId, calendarMonth)
        gAdapter.submitNewList(gList.toList())

        listButton.setOnClickListener {
            findNavController().navigate(R.id.show_list_calendar)
        }

        resetDateButton.setOnClickListener {
            val date = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
            DatabaseManager(requireContext()).updateCalendarDate(gId, date.toInstant().toEpochMilli())
            calendarMonth = date.toLocalDate()

            gList = DateConversions.daysInMonth(requireContext(), gId, calendarMonth)
            gAdapter.submitNewList(gList.toList())

            controlLabel.text = DateConversions.formatMonthYear(calendarMonth)
        }

        newEventButton.setOnClickListener {
            if(gControl) {
                gControl = false
                val createDialog = AddCalendarBottomFragment()
                createDialog.setInterface(this)
                createDialog.id = gId
                createDialog.show(childFragmentManager, createDialog.tag)
            }
        }

        previousButton.setOnClickListener {
            previousMonth()
        }

        forwardButton.setOnClickListener {
            forwardMonth()
        }
    }

    private fun previousMonth() {
        calendarMonth = calendarMonth.minusMonths(1)
        DatabaseManager(requireContext()).updateCalendarDate(gId, ZonedDateTime.of(calendarMonth, LocalTime.of(0,0,0,0), ZoneId.systemDefault()).toInstant().toEpochMilli())
        controlLabel.text = DateConversions.formatMonthYear(calendarMonth)

        gList = DateConversions.daysInMonth(requireContext(), gId, calendarMonth)
        gAdapter.submitNewList(gList.toList())
    }

    private fun forwardMonth() {
        calendarMonth = calendarMonth.plusMonths(1)
        DatabaseManager(requireContext()).updateCalendarDate(gId, ZonedDateTime.of(calendarMonth, LocalTime.of(0,0,0,0), ZoneId.systemDefault()).toInstant().toEpochMilli())
        controlLabel.text = DateConversions.formatMonthYear(calendarMonth)

        gList = DateConversions.daysInMonth(requireContext(), gId, calendarMonth)
        gAdapter.submitNewList(gList.toList())
    }

    override fun onCellClick(position: Int, date: CalendarObject) {
        if (date.calendarDay != null) {
            val passover = ZonedDateTime.of(date.calendarDay, LocalTime.of(0,0,0,0), ZoneId.systemDefault())
            val bundle = Bundle()
            bundle.putLong(CalendarListFragment.TYPE_KEY, passover.toInstant().toEpochMilli())
            findNavController().navigate(R.id.show_list_calendar, bundle)
        }
    }

    override fun confirmChange(dateTime: ZonedDateTime) {
        val position = DateConversions.getNewEventPosition(dateTime, calendarMonth)
        Log.i("TEST", "Position: $position")
        gList[position] = CalendarObject(dateTime.toLocalDate(), true)
        gAdapter.submitNewList(gList.toList())
        gControl = true
    }

    override fun cancelChange() {
        gControl = true
    }

}
