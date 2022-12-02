package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerCalendarTouchHelper
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerViewTouchHelper
import com.zybooks.schednet.Adapter.CalendarEventAdapter
import com.zybooks.schednet.Model.CalendarEvent
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.Utils.DateConversions
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * To use for day view and total view
 *
 * returns list of altered events in current month
 */
class CalendarListFragment(): Fragment(), CalendarEventAdapter.OnRibbonClick {

    companion object {
        const val TYPEKEY = "type.ky"
    }

    private lateinit var listLabel: TextView
    private lateinit var recyclerview: RecyclerView
    private lateinit var cancelButton: ImageButton

    private lateinit var gAdapter: CalendarEventAdapter

    private var gId: Int
    private var gType: Long

    init {
        gId = -1
        gType = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val arguments = getArguments()
        gType = arguments?.getLong(TYPEKEY) ?: 0
        val activityArgs = requireActivity().intent.extras
        gId = activityArgs!!.getInt(StageActivity.MAGIC_NUMBER)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.calendar_list, container, false)
        recyclerview = rootView.findViewById(R.id.generic_calendar_recyclerview)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        listLabel = rootView.findViewById(R.id.calendar_list_label)

        val list: ArrayList<CalendarEvent>

        if(gType > 0) {
            list = DatabaseManager(requireContext()).readDayEvents(gId,gType)
            val time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(gType), ZoneId.systemDefault())
            listLabel.text = DateConversions.formatListDate(time)
        } else {
            list = DatabaseManager(requireContext()).readUpcomingCalendarEvents(gId)
            listLabel.text = resources.getString(R.string.calendar_list_upcoming_text)
        }

        recyclerview.layoutManager = LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)
        recyclerview.addItemDecoration( DividerItemDecoration(recyclerview.context, DividerItemDecoration.VERTICAL) )
        gAdapter = CalendarEventAdapter(requireContext(), this, list)
        recyclerview.adapter = gAdapter
        ItemTouchHelper(RecyclerCalendarTouchHelper(gAdapter)).attachToRecyclerView(recyclerview)
        recyclerview.addItemDecoration( DividerItemDecoration(recyclerview.context, DividerItemDecoration.VERTICAL))

        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onClickEvent(eventId: Int) {
        //Toast.makeText(requireContext(), "Event ID: $eventId", Toast.LENGTH_LONG).show() //TODO: DELETE ME
        val bundle = Bundle()
        bundle.putInt(CalendarEditFragment.EVENTKEY, eventId)
        findNavController().navigate(R.id.show_calendar_edit, bundle)
    }


}