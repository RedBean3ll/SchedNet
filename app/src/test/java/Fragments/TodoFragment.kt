package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zybooks.schednet.Adapter.TodoAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddTodoBottomFragment
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.Model.TodoViewModel
import com.zybooks.schednet.R
import com.zybooks.schednet.databinding.TodoListBinding


class TodoFragment : Fragment() {

    companion object {
        const val ARG_LIST_ID = "list_id"
    }

    private lateinit var binding: TodoListBinding
    private lateinit var todoViewModel: TodoViewModel

    private lateinit var mAdapter: TodoAdapter
    //DEBUG

    private val viewModel: TodoViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TodoListBinding.inflate(inflater)
        val rootView: View = binding.root

        //Setup Dialog Attach
        binding.todoActionbarNewTodo.setOnClickListener {
            val fragment: AddTodoBottomFragment = AddTodoBottomFragment()
            fragment.show(childFragmentManager, "bottomSheet")
        }

        var mList: MutableList<TodoModel> = mutableListOf<TodoModel>()
        mList.add(TodoModel())
        mList.add(TodoModel())
        //DATABASE PULL
        //SORT

        //RecyclerView Setup
        binding.todoRecyclerview.layoutManager = LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)
        binding.todoRecyclerview.addItemDecoration(
            DividerItemDecoration(binding.todoRecyclerview.getContext(), DividerItemDecoration.VERTICAL
        ))
        //RecyclerView Data Assignment
        mAdapter = TodoAdapter(mList)
        binding.todoRecyclerview.adapter = mAdapter


        var shell = TodoModel()
        viewModel.TodoTitle.observe(viewLifecycleOwner) {
                shell = TodoModel()
                shell.TodoName = String.format(it)
        }
        viewModel.TodoDescription.observe(viewLifecycleOwner) {
            shell.TodoDescription = String.format(it)
            if (shell.TodoName != "") {
                mList.add(shell)
                viewModel.TodoDescription.value = ""
                viewModel.TodoTitle.value = ""
                updater(mList)
            }
        }

        binding.todoRecyclerview.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.show_todo_edit)
        }

        return rootView
    }

    fun updater(list: MutableList<TodoModel>) {
        mAdapter.setTasks(list)
    }


}