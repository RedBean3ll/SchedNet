package com.zybooks.schednet.Fragments

import android.media.Image
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
import com.zybooks.schednet.Adapter.CalendarAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddCalendarBottomFragment
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.Utils.DateConversions
import java.time.*

class CalendarFragment: Fragment(), CalendarAdapter.OnItemListener, AddCalendarBottomFragment.onDismissInteraction {

    private lateinit var controlLabel: TextView
    private lateinit var recyclerview: RecyclerView
    private lateinit var previousButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private lateinit var listButton: ImageButton
    private lateinit var gAdapter: CalendarAdapter
    private lateinit var newEventButton: ImageButton
    private lateinit var resetDateButton: ImageButton

    private var control: Boolean = true

    private lateinit var timeKeeper: LocalDate
    private var gId: Int

    init {
        gId = -1
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

        //TODO: ADD RESET DATE BUTTON

        timeKeeper = ZonedDateTime.ofInstant(Instant.ofEpochMilli(DatabaseManager(requireContext()).readCalendarDate(gId)), ZoneId.systemDefault()).toLocalDate()


        initializer()

        listButton.setOnClickListener {
            findNavController().navigate(R.id.show_list_calendar)
        }

        resetDateButton.setOnClickListener {
            val tempDate = LocalDate.now()
            val date = ZonedDateTime.of(tempDate.year, tempDate.monthValue, tempDate.dayOfMonth, 0, 0, 0, 0, ZoneId.systemDefault())
            DatabaseManager(requireContext()).updateCalendarDate(gId, date.toInstant().toEpochMilli())
            timeKeeper = date.toLocalDate()
            controlLabel.text = DateConversions.formatMonthYear(timeKeeper)
            gAdapter.updateDots(updateDataPoints())
            gAdapter.updateList(DateConversions.daysInMonth(timeKeeper))
        }

        newEventButton.setOnClickListener {
            if(control) {
                control = false
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

        return rootView
    }

    private fun initializer() {
        //Year Text
        controlLabel.text = DateConversions.formatMonthYear(timeKeeper)

        val dateTime = ZonedDateTime.of(timeKeeper.year, timeKeeper.monthValue, 1, 0, 0, 0, 0, ZoneId.systemDefault())
        val datePoints = DatabaseManager(requireContext()).readMonthlyEventTimestamp(gId, dateTime.toInstant().toEpochMilli(), YearMonth.of(dateTime.year, dateTime.monthValue).lengthOfMonth())
        val adapter = CalendarAdapter(DateConversions.daysInMonth(timeKeeper), this, DateConversions.eventDaysInMonth(datePoints, timeKeeper))
        gAdapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this.context, 7)
        recyclerview.adapter = adapter

    }

    fun previousMonth() {
        timeKeeper = timeKeeper.minusMonths(1)

        val tempZonedDateTime = ZonedDateTime.of(timeKeeper.year, timeKeeper.monthValue, timeKeeper.dayOfMonth, 0,0,0,0, ZoneId.systemDefault())
        DatabaseManager(requireContext()).updateCalendarDate(gId, tempZonedDateTime.toInstant().toEpochMilli())

        controlLabel.text = DateConversions.formatMonthYear(timeKeeper)
        gAdapter.updateDots(updateDataPoints())
        //TODO: ADD UPDATE TO PULL CURRENT EVENTS
        gAdapter.updateList(DateConversions.daysInMonth(timeKeeper))
        //updateMonthView()
    }

    fun forwardMonth() {
        timeKeeper = timeKeeper.plusMonths(1)

        val tempZonedDateTime = ZonedDateTime.of(timeKeeper.year, timeKeeper.monthValue, timeKeeper.dayOfMonth, 0,0,0,0, ZoneId.systemDefault())
        DatabaseManager(requireContext()).updateCalendarDate(gId, tempZonedDateTime.toInstant().toEpochMilli())

        controlLabel.text = DateConversions.formatMonthYear(timeKeeper)
        //TODO: ADD UPDATE TO PULL CURRENT EVENTS

        gAdapter.updateDots(updateDataPoints())
        gAdapter.updateList(DateConversions.daysInMonth(timeKeeper))
        //updateMonthView()
    }

    fun updateDataPoints(): ArrayList<Boolean> {
        val dateTimey = ZonedDateTime.of(timeKeeper.year, timeKeeper.monthValue, 1, 0, 0, 0, 0, ZoneId.systemDefault())
        val mult: Long = 86400000
        Log.i("CalendarFragment", "Day in month: ${YearMonth.of(dateTimey.year, dateTimey.dayOfMonth).lengthOfMonth()} and Start epoch: ${dateTimey.toInstant().toEpochMilli()} and End epoch: ${mult * YearMonth.of(dateTimey.year, dateTimey.dayOfMonth).lengthOfMonth()}")
        val datePointss = DatabaseManager(requireContext()).readMonthlyEventTimestamp(gId, dateTimey.toInstant().toEpochMilli(), YearMonth.of(dateTimey.year, dateTimey.dayOfMonth).lengthOfMonth())
        return DateConversions.eventDaysInMonth(datePointss, timeKeeper)
    }

    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            val passover = LocalDateTime.of(date.year, date.monthValue, date.dayOfMonth, 0, 0, 0, 0).atZone(ZoneId.systemDefault())

            val bundle = Bundle()
            bundle.putLong(CalendarListFragment.TYPEKEY, passover.toInstant().toEpochMilli())
            findNavController().navigate(R.id.show_list_calendar, bundle)
        }
    }

    override fun onItemLongClick(position: Int) {
    }

    override fun confirmChange(dateTime: ZonedDateTime) {
        val position = DateConversions.eventDayPosition(dateTime, timeKeeper)
        gAdapter.updateDotAt(position)

        control = true
    }

    override fun cancelChange() {
        control = true
    }

}
