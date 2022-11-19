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

    private val viewModel: StateViewModelTodo by activityViewModels()
    private lateinit var binding: TodoListBinding
    private lateinit var Adapter: TodoAdapter
    private var MagicNumber: Int
    private var AltMagicNumber: Int
    private var control: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        AltMagicNumber = args!!.getInt(LISTKEY)
        val activityIntent = requireActivity().intent.extras
        MagicNumber = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TodoListBinding.inflate(inflater)

        binding.todoLabel.text = DatabaseManager(requireContext()).readListName(AltMagicNumber)
        binding.todoActionbarNewTodo.setOnClickListener {
            if(control) {
                control = false
                callBottomDialogue()
            }
        }

        //RecyclerView Setup
        binding.todoRecyclerview.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.todoRecyclerview.addItemDecoration( DividerItemDecoration(binding.todoRecyclerview.context, DividerItemDecoration.VERTICAL))
        Adapter = TodoAdapter(binding.root.context, MagicNumber, AltMagicNumber)
        binding.todoRecyclerview.adapter = Adapter
        ItemTouchHelper(RecyclerTodoTouchHelper(Adapter)).attachToRecyclerView(binding.todoRecyclerview)

        return binding.root
    }

    private fun callBottomDialogue() {

        callObserver()
        val fragment = AddTodoBottomFragment(MagicNumber, AltMagicNumber)
        fragment.show(childFragmentManager, "bottomSheet")
    }

    private fun callObserver() {
        //Should update?
        viewModel.PageState.observe(viewLifecycleOwner) {
            if(viewModel.PageState.value == true) {
                Adapter.callUpdateSpinner()
                viewModel.PageState.value = false
            }
        }

        //Should allow bottom view again?
        viewModel.AccessState.observe(viewLifecycleOwner) {
            if(viewModel.AccessState.value == true) {
                control = true
                viewModel.AccessState.value = false
            }
        }
    }

    init {
        MagicNumber = -1
        AltMagicNumber = -1
        control = true
    }

    companion object {
        const val LISTKEY = "list.ky"
    }
}
