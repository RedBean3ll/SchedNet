package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.zybooks.schednet.Adapter.MenuAdapter
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.R
import com.zybooks.schednet.databinding.ListBinding

class ListFragment: Fragment() {

    private lateinit var binding: ListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ListBinding.inflate(layoutInflater)

        binding.listActionbarNewList.setOnClickListener {
            findNavController(it).navigate(R.id.show_todo_list)
        }

        val recyclerView = binding.listRecyclerview
        val lists = mutableListOf<ListModel>()
        lists.add(ListModel())
        lists.add(ListModel())
        lists.add(ListModel())
        binding.listRecyclerview.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.listRecyclerview.addItemDecoration(
            DividerItemDecoration(binding.listRecyclerview.getContext(), DividerItemDecoration.VERTICAL)
        )

        recyclerView.adapter = MenuAdapter(lists)


        val rootView: View = binding.root
        return rootView
    }


}