package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerListTouchHelper
import com.zybooks.schednet.Adapter.MenuAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddListBottomFragment
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.databinding.ListBinding
import java.util.*

class ListFragment: Fragment() {

    private lateinit var binding: ListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO "WILL ADD PULL USERID AND SAVE TO BUNDLE FUNCTION"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ListBinding.inflate(layoutInflater)

        binding.listActionbarNewList.setOnClickListener {
            val fragment = AddListBottomFragment()
            fragment.show(childFragmentManager, "bottomSheet")
        }

        val lists = ArrayList<ListModel>()
        lists.add(ListModel())
        lists.add(ListModel())
        lists.add(ListModel())
        binding.listRecyclerview.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.listRecyclerview.addItemDecoration(
            DividerItemDecoration(binding.listRecyclerview.context, DividerItemDecoration.VERTICAL)
        )
        val mAdapter = MenuAdapter(binding.root.context, lists)
        binding.listRecyclerview.adapter = mAdapter
        val recyclerviewSwipeHelper= ItemTouchHelper(RecyclerListTouchHelper(mAdapter))
        recyclerviewSwipeHelper.attachToRecyclerView(binding.listRecyclerview)

        return binding.root
    }


}