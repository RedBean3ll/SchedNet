package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerFavoriteTouchHelper
import com.zybooks.schednet.Adapter.FavoriteAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddFavoriteCalendarBottomFragment
import com.zybooks.schednet.Fragments.BottomFragments.AddFavoriteTodoBottomFragment
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.databinding.FavoriteBinding

class FavoriteFragment: Fragment() {

    private lateinit var binding: FavoriteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FavoriteBinding.inflate(layoutInflater)

        //false = tdo mode, true = calendar mode
        val mode = false

        //GET LIST DATA
        val list = ArrayList<ListModel>()
        //TODO: ANY TEST LISTMODEL NEEDS ID FIRST!!

        //VIEW SETUP AND CLICK LISTENERS
        binding.apply {
            val adapter = FavoriteAdapter(root.context, list, mode)

            favoriteActionbarSwitch.setOnClickListener {
                mode != mode
                adapter.changeMode(mode)
            }


            favoriteActionbarNewList.setOnClickListener {
                val fragment =
                    if (mode) AddFavoriteCalendarBottomFragment() else AddFavoriteTodoBottomFragment()
                fragment.show(childFragmentManager, "bottomSheet")
            }
            favoriteRecyclerview.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            favoriteRecyclerview.addItemDecoration(
                DividerItemDecoration(
                    binding.favoriteRecyclerview.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            val recyclerviewSwipeHelper = ItemTouchHelper(RecyclerFavoriteTouchHelper(adapter))
            recyclerviewSwipeHelper.attachToRecyclerView(favoriteRecyclerview)

            favoriteRecyclerview.adapter = adapter

        }

        return binding.root
    }
}