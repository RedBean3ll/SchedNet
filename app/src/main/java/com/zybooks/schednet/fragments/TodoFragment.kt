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
import com.zybooks.schednet.adapter.TodoAdapter
import com.zybooks.schednet.fragments.bottom_fragments.AddTodoBottomFragment
import com.zybooks.schednet.model.TodoObject
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: TodoFragment.kt
 *  @author Matthew Clark
 */

class TodoFragment : Fragment(), AddTodoBottomFragment.OnDismissInteraction, TodoAdapter.OnRibbonListener, RecyclerViewTouchHelper.OnSwipeInteraction {

    private lateinit var gAdapter: TodoAdapter

    private lateinit var listName: TextView
    private lateinit var recyclerview: RecyclerView
    private lateinit var newEventButton: ImageButton

    private var gId: Int
    private var gListId: Int

    private lateinit var gList: MutableList<TodoObject>
    private var control: Boolean

    init {
        gId = -1
        gListId = -1
        control = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        gListId = args?.getInt(LISTKEY) ?: -1
        val activityIntent = requireActivity().intent.extras
        gId = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.todo_list, container, false)
        listName = rootView.findViewById(R.id.todo_label)
        recyclerview = rootView.findViewById(R.id.todo_recyclerview)
        newEventButton = rootView.findViewById(R.id.todo_actionbar_new_todo)
        initializer()

        return rootView
    }

    private fun initializer() {
        listName.text = DatabaseManager(requireContext()).readListName(gListId)

        recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            gAdapter = TodoAdapter(this@TodoFragment)
            adapter = gAdapter
            ItemTouchHelper(RecyclerViewTouchHelper(requireContext(), this@TodoFragment)).attachToRecyclerView(this)
            addItemDecoration( DividerItemDecoration(recyclerview.context, DividerItemDecoration.VERTICAL))
        }

        gList = DatabaseManager(requireContext()).readTodos(gId, gListId)
        gAdapter.submitNewList(gList.toList())

        //Click events
        newEventButton.setOnClickListener {
            if(control) {
                control = false

                val fragment = AddTodoBottomFragment()
                fragment.setInterface(this)
                fragment.id = gId
                fragment.setListId(gListId)
                fragment.show(childFragmentManager, "bottomSheet")
            }
        }
    }

    override fun confirmChange(newEvent: TodoObject) {
        gList.add(0, newEvent)
        gAdapter.submitNewList(gList.toList())
    }

    override fun cancelChange() {
        control = true
    }

    override fun onClick(event: TodoObject) {
        val bundle = Bundle()
        bundle.putInt(TodoEditFragment.EVENTKEY, event.eventId)
        findNavController().navigate(R.id.show_todo_edit, bundle)
    }

    override fun onPin(position: Int, event: TodoObject) {
        event.eventTimestamp = DatabaseManager(requireContext()).updateTodoPinValue(event.eventId, event)
        if(event.isPinned && !event.isChecked) {
            gList.removeAt(position)
            gList.add(0, event)
            gAdapter.submitNewList(gList.toList())
        }
    }

    override fun onCheck(position: Int, event: TodoObject) {
        event.eventTimestamp = DatabaseManager(requireContext()).updateTodoCheckboxValue(event.eventId, event)
        if(event.isChecked) {
            gList.removeAt(position)
            gList.add(event)
            gAdapter.submitNewList(gList.toList())
        }
    }

    override fun onSwipeLeft(position: Int) {
        DatabaseManager(requireContext()).deleteTodo(gList[position].eventId)
        if(gList.size > 0) {
            gList.removeAt(position)
            gAdapter.submitNewList(gList.toList())
        }
    }

    companion object {
        const val LISTKEY = "list.ky"
    }

    //1669496
}
