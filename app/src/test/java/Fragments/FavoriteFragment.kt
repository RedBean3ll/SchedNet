package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zybooks.schednet.Adapter.FavoriteAdapter
import com.zybooks.schednet.Adapter.MenuAdapter
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.R
import com.zybooks.schednet.databinding.FavoriteBinding

class FavoriteFragment: Fragment() {

    private lateinit var binding: FavoriteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FavoriteBinding.inflate(layoutInflater)

        //GET LIST DATA
        val list = ArrayList<ListModel>()
        list.add(ListModel())
        list.add(ListModel())
        list.add(ListModel())
        list.add(ListModel())


        //VIEW SETUP AND CLICK LISTENERS
        binding.apply {
            val adapter = FavoriteAdapter(root.context, list)

            var cnt: Int = 0
            favoriteActionbarNewList.setOnClickListener {
                cnt++
                var shl: ListModel = ListModel()
                shl.ListName = "Item No."+cnt

                adapter.add(shl)
            }
            favoriteRecyclerview.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            favoriteRecyclerview.addItemDecoration(
                DividerItemDecoration(binding.favoriteRecyclerview.getContext(), DividerItemDecoration.VERTICAL)
            )

            favoriteRecyclerview.adapter = adapter

        }

        val rootView = binding.root
        return rootView
    }
}