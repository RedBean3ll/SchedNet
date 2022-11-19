package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerListTouchHelper
import com.zybooks.schednet.Adapter.MenuAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddListBottomFragment
import com.zybooks.schednet.Model.StateViewModel
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.databinding.ListBinding

class ListFragment: Fragment() {

    private val state: StateViewModel by activityViewModels()
    private lateinit var binding: ListBinding
    private lateinit var Adapter: MenuAdapter
    var MagicNumber: Int
    var control: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var activityIntent = requireActivity().intent.extras
        MagicNumber = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ListBinding.inflate(layoutInflater)

        binding.listActionbarNewList.setOnClickListener {
            if (control) {
                control = false
                callBottomDialogue()
            }
        }

        //RecyclerView Setup
        binding.listRecyclerview.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.listRecyclerview.addItemDecoration( DividerItemDecoration(binding.listRecyclerview.context, DividerItemDecoration.VERTICAL) )
        Adapter = MenuAdapter(binding.root.context, MagicNumber)
        binding.listRecyclerview.adapter = Adapter
        ItemTouchHelper(RecyclerListTouchHelper(Adapter)).attachToRecyclerView(binding.listRecyclerview)

        return binding.root
    }

    private fun callBottomDialogue() {
        callObserver()
        val fragment = AddListBottomFragment(MagicNumber)
        fragment.show(childFragmentManager, "bottomSheet")
    }

    private fun callObserver() {
        //Should update?
        state.PageState.observe(viewLifecycleOwner) {
            if(state.PageState.value == true) {
                Adapter.callUpdateSpinner()
                state.PageState.value = false
            }
        }

        //Should allow bottom view again?
        state.AccessState.observe(viewLifecycleOwner) {
            if(state.AccessState.value == true) {
                control = true
                state.AccessState.value = false
            }
        }
    }

    init {
        MagicNumber = -1
        control = true
    }
}