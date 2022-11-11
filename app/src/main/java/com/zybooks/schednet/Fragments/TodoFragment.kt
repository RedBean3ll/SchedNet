package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zybooks.schednet.Adapter.TodoAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddTodoBottomFragment
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.Model.TodoViewModel
import com.zybooks.schednet.databinding.TodoListBinding

class TodoFragment : Fragment() {
    private lateinit var binding: TodoListBinding
    private lateinit var todoViewModel: TodoViewModel

    private lateinit var mList: MutableList<TodoModel>
    private lateinit var mAdapter: TodoAdapter
    //DEBUG
    val TAG = "TodoLocal"
    var CNT = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TodoListBinding.inflate(inflater)
        val rootView: View = binding.root
        /*
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        binding.tdoNewTodoFab.setOnClickListener {
            AddTodoBottomFragment().show(childFragmentManager, "newTaskTag")
        }

        mList = mutableListOf()
        mList.add(TodoModel()) //TEST

        //DATABASE PULL
        //SORT

        mAdapter = TodoAdapter(mList)
        binding.tdoTaskSpace.layoutManager = LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)
        binding.tdoTaskSpace.adapter = mAdapter

        var shell = TodoModel()
        todoViewModel.TodoTitle.observe(this.viewLifecycleOwner) {
            shell = TodoModel()
            shell.TodoName = String.format(it)
            //Toast.makeText(rootView.context, "LIST SIZE NOW: "+mList.size, Toast.LENGTH_LONG)
        }
        todoViewModel.TodoDescription.observe(this.viewLifecycleOwner) {
            shell.TodoDescription = String.format(it)
            mList.add(shell)
            //Toast.makeText(rootView.context, "LIST SIZE NOW: "+mList.size, Toast.LENGTH_LONG)
            updater()
            //mAdapt!!.setTasks(mTdoList)
            //mRecycleViewMe!!.adapter = mAdapt
        }*/

        return rootView
    }

    fun updater() {
        mAdapter.setTasks(mList)
    }
}