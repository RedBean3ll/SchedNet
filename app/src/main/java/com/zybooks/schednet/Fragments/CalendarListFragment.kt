package com.zybooks.schednet.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerCalendarTouchHelper
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerListTouchHelper
import com.zybooks.schednet.Adapter.CalendarEventAdapter
import com.zybooks.schednet.Model.CalendarEvent
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.Utils.DatabaseManager

/**
 * To use for day view and total view
 *
 * returns list of altered events in current month
 */
class CalendarListFragment(): Fragment(), CalendarEventAdapter.OnRibbonClick {

    companion object {
        const val TYPEKEY = "type.ky"
    }

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

        //Toast.makeText(requireContext(), "gType is $gType and gId is $gId", Toast.LENGTH_LONG).show()

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.calendar_list, container, false)
        recyclerview = rootView.findViewById(R.id.generic_calendar_recyclerview)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)


        val list: ArrayList<CalendarEvent>

        if(gType > 0) {
            list = DatabaseManager(requireContext()).readDayEvents(gId,gType)
        } else {
            list = DatabaseManager(requireContext()).readUpcomingCalendarEvents(gId)
        }


        recyclerview.layoutManager = LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)
        gAdapter = CalendarEventAdapter(requireContext(), this, list)
        recyclerview.adapter = gAdapter
        ItemTouchHelper(RecyclerCalendarTouchHelper(gAdapter)).attachToRecyclerView(recyclerview)

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