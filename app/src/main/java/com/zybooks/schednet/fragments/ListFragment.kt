package com.zybooks.schednet.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.adapter.ListAdapter
import com.zybooks.schednet.fragments.bottom_fragments.AddListBottomFragment
import com.zybooks.schednet.model.ListObject
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.zybooks.schednet.adapter.RecyclerViewTouchHelper
import com.zybooks.schednet.fragments.bottom_fragments.EditListBottomFragment
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.utils.DatabaseManager
import com.zybooks.schednet.utils.DateConversions

/**
 *  File: ListFragment.kt
 *  @author Matthew Clark
 */

class ListFragment: Fragment(), AddListBottomFragment.OnDismissAddInteraction, ListAdapter.OnRibbonListener, EditListBottomFragment.OnDismissEditInteraction, RecyclerViewTouchHelper.OnSwipeInteraction {

    private lateinit var recyclerview: RecyclerView
    private lateinit var addListButton: ImageButton

    private lateinit var gList: MutableList<ListObject>
    private lateinit var gAdapter: ListAdapter
    private var gId: Int
    private var control: Boolean


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityIntent = requireActivity().intent.extras
        gId = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.list, container, false)
        recyclerview = rootView.findViewById(R.id.list_recyclerview)
        addListButton = rootView.findViewById(R.id.list_actionbar_new_list)

        val tempList = DatabaseManager(requireContext()).readGarbageCollectorValues(gId)
        DatabaseManager(requireContext()).cleanTodo(gId, DateConversions.getDateStampTodo(tempList[0]))
        initializer()

        return rootView
    }

    private fun initializer() {
        recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            gAdapter = ListAdapter(this@ListFragment)
            adapter = gAdapter
            ItemTouchHelper(RecyclerViewTouchHelper(requireContext(), this@ListFragment)).attachToRecyclerView(this)
            addItemDecoration(DividerItemDecoration(recyclerview.context, DividerItemDecoration.VERTICAL))
        }

        //SET FIRST LIST
        gList = DatabaseManager(requireContext()).readLists(gId)
        gAdapter.submitNewList(gList.toList())

        //CLICK LISTENER
        addListButton.setOnClickListener {
            if(control) {
                control = false
                val addFragment = AddListBottomFragment()
                addFragment.setInterface(this@ListFragment)
                addFragment.id = gId
                addFragment.show(childFragmentManager, addFragment.tag)
            }
        }
    }

    init {
        gId = -1
        control = true
    }

    //ADD LIST BOTTOM
    override fun confirmAddition(newList: ListObject) {
        gList.add(0, newList)
        gAdapter.submitNewList(gList.toList())
    }
    override fun cancelAddition() {
        control = true
    }

    //EDIT LIST BOTTOM
    override fun confirmName(position: Int, listObject: ListObject) {
        gList[position] = listObject
        gAdapter.submitNewList(gList.toList())
    }

    //ADAPTER
    override fun onClick(position: Int, list: ListObject) {
        Log.i("clickTest", "onClick w/ List: ${list.listName} @ Position: $position")
        val bundle = Bundle()
        bundle.putInt(TodoFragment.LISTKEY, list.listId)
        findNavController().navigate(R.id.show_todo_list, bundle)
    }
    override fun onLongClick(position: Int, list: ListObject) {
        val editFragment = EditListBottomFragment()
        editFragment.setInterface(this@ListFragment)
        editFragment.setPosition(position)
        editFragment.setObject(ListObject(list.listId, list.listName, list.isPinned))
        editFragment.show(childFragmentManager, editFragment.tag)
    }
    override fun onPin(position: Int, list: ListObject) {
        DatabaseManager(requireContext()).updateListPinValue(list.listId, list.isPinned)
        if(list.isPinned) {
            gList.removeAt(position)
            gList.add(0, list)
            gAdapter.submitNewList(gList.toList())
        }
    }

    //TOUCH HELPER
    override fun onSwipeLeft(position: Int) {
        DatabaseManager(requireContext()).deleteList(gList[position].listId)
        if(gList.size > 0) {
            gList.removeAt(position)
            gAdapter.submitNewList(gList.toList())
        }

    }

}