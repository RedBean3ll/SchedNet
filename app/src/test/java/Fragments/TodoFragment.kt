package Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zybooks.schednet.Adapter.TodoAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddTodoBottomFragment
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.Model.TodoViewModel
import com.zybooks.schednet.R
import com.zybooks.schednet.databinding.TodoListBinding

class TodoFragment : Fragment() {

    private lateinit var binding: TodoListBinding
    private lateinit var mAdapter: TodoAdapter

    private val viewModel: TodoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TodoListBinding.inflate(inflater)

        //Setup Dialog Attach
        binding.todoActionbarNewTodo.setOnClickListener {
            val fragment = AddTodoBottomFragment()
            fragment.show(childFragmentManager, "bottomSheet")
        }

        val mList: ArrayList<TodoModel> = ArrayList()
        mList.add(TodoModel())
        mList.add(TodoModel())
        //DATABASE PULL
        //SORT

        //RecyclerView Setup
        binding.todoRecyclerview.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.todoRecyclerview.addItemDecoration(
            DividerItemDecoration(binding.todoRecyclerview.context, DividerItemDecoration.VERTICAL
        ))
        //RecyclerView Data Assignment
        mAdapter = TodoAdapter(binding.root.context, mList)
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
            }
        }

        binding.todoRecyclerview.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.show_todo_edit)
        }

        return binding.root
    }


}