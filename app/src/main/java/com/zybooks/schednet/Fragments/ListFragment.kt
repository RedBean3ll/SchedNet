package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerListTouchHelper
import com.zybooks.schednet.Adapter.MenuAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddListBottomFragment
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.Model.StateViewModel
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.databinding.ListBinding
import java.util.*

class ListFragment: Fragment() {

    private lateinit var binding: ListBinding
    private val state: StateViewModel by activityViewModels()

    var mMagicNumber: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var activityIntent = requireActivity().intent.extras
        mMagicNumber = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ListBinding.inflate(layoutInflater)

        binding.listActionbarNewList.setOnClickListener {
            val fragment = AddListBottomFragment(mMagicNumber)
            fragment.show(childFragmentManager, "bottomSheet")
        }

        binding.listRecyclerview.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.listRecyclerview.addItemDecoration(
            DividerItemDecoration(binding.listRecyclerview.context, DividerItemDecoration.VERTICAL)
        )
        val mAdapter = MenuAdapter(binding.root.context, mMagicNumber)
        binding.listRecyclerview.adapter = mAdapter
        val recyclerviewSwipeHelper= ItemTouchHelper(RecyclerListTouchHelper(mAdapter))
        recyclerviewSwipeHelper.attachToRecyclerView(binding.listRecyclerview)

        //BottomDialogFragment completion observer
        state.PageState.observe(viewLifecycleOwner) {
            if(state.PageState.value == true) {
                mAdapter.callUpdateSpinner()
                Toast.makeText(requireContext(), "Returned value: ",Toast.LENGTH_LONG).show()
                state.PageState.value = false
            }
        }

        return binding.root
    }




}