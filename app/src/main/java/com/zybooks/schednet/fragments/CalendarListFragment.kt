package com.zybooks.schednet.fragments

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
import com.zybooks.schednet.adapter.RecyclerViewTouchHelper
import com.zybooks.schednet.adapter.CalendarEventAdapter
import com.zybooks.schednet.model.CalendarEvent
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.utils.DatabaseManager
import com.zybooks.schednet.utils.DateConversions
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 *  File: CalendarListFragment.kt
 *  @author Matthew Clark
 */

class CalendarListFragment: Fragment(), CalendarEventAdapter.OnRibbonClick, RecyclerViewTouchHelper.OnSwipeInteraction {

    companion object {
        const val TYPE_KEY = "type.ky"
    }

    private lateinit var listLabel: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var cancelButton: ImageButton

    private lateinit var gAdapter: CalendarEventAdapter

    private var gId: Int
    private var gType: Long
    private lateinit var gList: MutableList<CalendarEvent>

    init {
        gId = -1
        gType = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val arguments = arguments
        gType = arguments?.getLong(TYPE_KEY) ?: 0
        val activityArgs = requireActivity().intent.extras
        gId = activityArgs!!.getInt(StageActivity.MAGIC_NUMBER)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.calendar_list, container, false)
        recyclerView = rootView.findViewById(R.id.generic_calendar_recyclerview)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        listLabel = rootView.findViewById(R.id.calendar_list_label)
        initializer()



        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return rootView
    }

    fun initializer() {
        if(gType > 0) {
            gList = DatabaseManager(requireContext()).readDayEvents(gId,gType)
            listLabel.text = DateConversions.formatListDate(ZonedDateTime.ofInstant(Instant.ofEpochMilli(gType), ZoneId.systemDefault()))
        } else {
            gList = DatabaseManager(requireContext()).readUpcomingCalendarEvents(gId)
            listLabel.text = resources.getString(R.string.calendar_list_upcoming_text)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)
            gAdapter = CalendarEventAdapter(this@CalendarListFragment)
            recyclerView.adapter = gAdapter
            addItemDecoration( DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL) )
            ItemTouchHelper(RecyclerViewTouchHelper(requireContext(), this@CalendarListFragment)).attachToRecyclerView(this)
        }

        gAdapter.submitNewList(gList.toList())
    }

    override fun onClickEvent(position: Int, event: CalendarEvent) {
        val bundle = Bundle()
        bundle.putInt(CalendarEditFragment.EVENT_KEY, event.eventId)
        findNavController().navigate(R.id.show_calendar_edit, bundle)
    }

    override fun onSwipeLeft(position: Int) {
        DatabaseManager(requireContext()).deleteCalendarEvent(gList[position].eventId)
        gList.removeAt(position)
        gAdapter.submitNewList(gList.toList())
    }


}