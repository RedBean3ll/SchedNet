package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerTodoTouchHelper
import com.zybooks.schednet.Adapter.TodoAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddTodoBottomFragment
import com.zybooks.schednet.Model.StateViewModelTodo
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.databinding.TodoListBinding

class TodoFragment : Fragment() {

    private lateinit var binding: TodoListBinding
    private lateinit var mAdapter: TodoAdapter
    private val viewModel: StateViewModelTodo by activityViewModels()


    companion object {
        const val LISTKEY = "list.ky"
    }

    private var MagicNumber = -1
    private var AltMagicNumber = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        AltMagicNumber = args!!.getInt(LISTKEY)
        //Toast.makeText(requireActivity(), "Number: "+args?.getInt(LISTKEY), Toast.LENGTH_LONG).show()

        val activityIntent = requireActivity().intent.extras
        MagicNumber = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TodoListBinding.inflate(inflater)

        binding.todoLabel.text = DatabaseManager(requireContext()).readListName(AltMagicNumber)

        //Setup Dialog Attach
        binding.todoActionbarNewTodo.setOnClickListener {
            val fragment = AddTodoBottomFragment(MagicNumber, AltMagicNumber)
            fragment.show(childFragmentManager, "bottomSheet")
        }

        //RecyclerView Setup
        binding.todoRecyclerview.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.todoRecyclerview.addItemDecoration(
            DividerItemDecoration(binding.todoRecyclerview.context, DividerItemDecoration.VERTICAL
        ))
        //RecyclerView Data Assignment
        mAdapter = TodoAdapter(binding.root.context, MagicNumber, AltMagicNumber)
        binding.todoRecyclerview.adapter = mAdapter
        val recyclerviewSwipeHelper = ItemTouchHelper(RecyclerTodoTouchHelper(mAdapter))
        recyclerviewSwipeHelper.attachToRecyclerView(binding.todoRecyclerview)

        //RETURN DATA
        viewModel.PageState.observe(viewLifecycleOwner) {
            //updater
            if(viewModel.PageState.value == true) {
                mAdapter.callUpdateSpinner()
                viewModel.PageState.value = false
            }
        }

        return binding.root
    }


}