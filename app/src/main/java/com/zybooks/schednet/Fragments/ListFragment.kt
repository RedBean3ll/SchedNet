package com.zybooks.schednet.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerListTouchHelper
import com.zybooks.schednet.Adapter.MenuAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddListBottomFragment
import com.zybooks.schednet.Model.TodoList
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity

class ListFragment: Fragment(), AddListBottomFragment.OnDismissInteraction, MenuAdapter.OnRibbonListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addListButton: ImageButton

    private lateinit var gAdapter: MenuAdapter
    private var gId: Int
    private var control: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var activityIntent = requireActivity().intent.extras
        gId = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)
        if(gId == -1) findNavController().popBackStack()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.list, container, false)
        recyclerView = rootView.findViewById(R.id.list_recyclerview)
        addListButton = rootView.findViewById(R.id.list_actionbar_new_list)
        initializer(rootView.context)

        return rootView
    }

    fun initializer(context: Context) {
        //RecyclerView Setup
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration( DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL) )
        gAdapter = MenuAdapter(context, gId, this)
        recyclerView.adapter = gAdapter
        ItemTouchHelper(RecyclerListTouchHelper(gAdapter)).attachToRecyclerView(recyclerView)

        //Click listeners
        addListButton.setOnClickListener {
            if (control) {
                control = false
                callBottomDialogue()
            }
        }
    }

    private fun callBottomDialogue() {
        val fragment = AddListBottomFragment()
        fragment.setInterface(this)
        fragment.id = gId
        fragment.show(childFragmentManager, "bottomSheet")
    }

    //init
    init {
        gId = -1
        control = true
    }

    //interfaces
    override fun confirmChange(listModel: TodoList) {
        control = true
        gAdapter.add(listModel)
    }

    override fun cancelChange() {
        control = true
    }

    override fun onClick(eventId: Int, view: View) {
        val bundle = Bundle()
        bundle.putInt(TodoFragment.LISTKEY, eventId)
        findNavController(view).navigate(R.id.show_todo_list, bundle)

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}