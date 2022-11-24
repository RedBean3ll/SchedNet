package com.zybooks.schednet.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerTodoTouchHelper
import com.zybooks.schednet.Adapter.TodoAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddTodoBottomFragment
import com.zybooks.schednet.Model.TodoEvent
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.Utils.DatabaseManager

class TodoFragment : Fragment(), AddTodoBottomFragment.onDismissInteraction, TodoAdapter.OnRibbonListener {

    private lateinit var gAdapter: TodoAdapter

    private lateinit var listName: TextView
    private lateinit var recyclerview: RecyclerView
    private lateinit var newEventButton: ImageButton

    private var gId: Int
    private var gListId: Int
    private var control: Boolean

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
        initializer(rootView.context)

        return rootView
    }

    private fun initializer(context: Context) {
        //List name
        listName.text = DatabaseManager(requireContext()).readListName(gListId)

        //Recyclerview
        recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerview.addItemDecoration( DividerItemDecoration(recyclerview.context, DividerItemDecoration.VERTICAL))
        gAdapter = TodoAdapter(context, gId, gListId, this)
        recyclerview.adapter = gAdapter
        ItemTouchHelper(RecyclerTodoTouchHelper(gAdapter)).attachToRecyclerView(recyclerview)

        //Click events
        newEventButton.setOnClickListener {
            if(control) {
                control = false
                callBottomDialogue()
            }
        }
    }

    private fun callBottomDialogue() {
        val fragment = AddTodoBottomFragment()
        fragment.setInterface(this)
        fragment.id = gId
        fragment.setListId(gListId)
        fragment.show(childFragmentManager, "bottomSheet")
    }

    init {
        gId = -1
        gListId = -1
        control = true
    }

    companion object {
        const val LISTKEY = "list.ky"
    }

    override fun confirmChange(todoEvent: TodoEvent) {
        control = true
        gAdapter.add(todoEvent)
    }

    override fun cancelChange() {
        control = true
    }

    override fun onClick(view: View) {

        Navigation.findNavController(view).navigate(R.id.show_todo_edit)
    }
}
