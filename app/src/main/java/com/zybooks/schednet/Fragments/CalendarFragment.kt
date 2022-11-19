package com.zybooks.schednet.Fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Adapter.CalendarAdapter
import com.zybooks.schednet.Fragments.BottomFragments.TestBottomFragment
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DateConversions
import java.time.LocalDate

class CalendarFragment: Fragment(), CalendarAdapter.onItemListener, TestBottomFragment.onDismissInteraction {

    private lateinit var controlLabel: TextView
    private lateinit var recyclerview: RecyclerView
    private lateinit var previousButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private lateinit var testButton: ImageButton

    private lateinit var sharedAdapter: CalendarAdapter

    private var dialogueControl: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    private var timeKeeper: LocalDate = LocalDate.now()

    private var currentSelect = -1
    private var previousSelect = -1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.calendar, container, false)
        controlLabel = rootView.findViewById(R.id.control_pane_label)
        recyclerview = rootView.findViewById(R.id.calendar_recyclerview)
        previousButton = rootView.findViewById(R.id.control_pane_left)
        forwardButton = rootView.findViewById(R.id.control_pane_right)
        testButton = rootView.findViewById(R.id.temp_testy)
        initializer()

        previousButton.setOnClickListener {
            previousMonth()
        }

        forwardButton.setOnClickListener {
            forwardMonth()
        }

        testButton.setOnClickListener {
            val fragment = TestBottomFragment(this)
            fragment.show(childFragmentManager, "bottomSheet")
        }



        return rootView
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun initializer() {
        //Year Text
        controlLabel.text = DateConversions.formatMonthYear(timeKeeper)

        val adapter = CalendarAdapter(DateConversions.daysInMonth(LocalDate.now()), this, DateConversions.eventDaysInMonth(timeKeeper))
        sharedAdapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this.context, 7)
        recyclerview.adapter = adapter

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonth() {
        timeKeeper = timeKeeper.minusMonths(1)
        controlLabel.text = DateConversions.formatMonthYear(timeKeeper)
        sharedAdapter.updateList(DateConversions.daysInMonth(timeKeeper))
        //updateMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun forwardMonth() {
        timeKeeper = timeKeeper.plusMonths(1)
        controlLabel.text = DateConversions.formatMonthYear(timeKeeper)
        sharedAdapter.updateList(DateConversions.daysInMonth(timeKeeper))
        //updateMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, date: LocalDate?) {
        //Toast.makeText(requireContext(), "Postion: $position", Toast.LENGTH_SHORT).show()
        DateConversions.testLoc(position, timeKeeper)
        //fun setClicked (result > dropdown bar with events from that day)
    }

    override fun onItemLongClick(position: Int) {
        Toast.makeText(requireContext(), "onClickCell!!", Toast.LENGTH_LONG).show()
    }

    override fun onReturnAction(words: String) {
        Toast.makeText(requireContext(), "Action: $words", Toast.LENGTH_SHORT).show()

    }
}
