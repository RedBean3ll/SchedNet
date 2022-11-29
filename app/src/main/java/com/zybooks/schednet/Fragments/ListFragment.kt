package com.zybooks.schednet.Fragments

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
import com.zybooks.schednet.Adapter.ListAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddListBottomFragment
import com.zybooks.schednet.Model.ListObject
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerViewTouchHelper
import com.zybooks.schednet.Fragments.BottomFragments.EditListBottomFragment
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.Utils.DatabaseManager

class ListFragment: Fragment(), AddListBottomFragment.OnDismissAddInteraction, ListAdapter.OnRibbonListener, EditListBottomFragment.OnDismissEditInteraction, RecyclerViewTouchHelper.onSwipeInterraction {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addListButton: ImageButton

    private lateinit var gList: MutableList<ListObject>
    private lateinit var gAdapter: ListAdapter
    private var gId: Int
    private var control: Boolean


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityIntent = requireActivity().intent.extras
        gId = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)
        if(gId == -1) findNavController().popBackStack()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.list, container, false)
        recyclerView = rootView.findViewById(R.id.list_recyclerview)
        addListButton = rootView.findViewById(R.id.list_actionbar_new_list)
        initializer()

        return rootView
    }

    private fun initializer() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            gAdapter = ListAdapter(this@ListFragment)
            adapter = gAdapter
            ItemTouchHelper(RecyclerViewTouchHelper(requireContext(), this@ListFragment)).attachToRecyclerView(this)
            addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
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