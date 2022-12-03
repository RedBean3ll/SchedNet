package com.zybooks.schednet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.adapter.FavoriteListAdapter
import com.zybooks.schednet.fragments.bottom_fragments.AddFavoriteTodoBottomFragment
import com.zybooks.schednet.model.ListObject
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: FavoriteListSelectFragment.kt
 *  @author Matthew Clark
 */

class FavoriteListSelectFragment: Fragment(), FavoriteListAdapter.OnRibbonListener, AddFavoriteTodoBottomFragment.OnDismissInteraction {

    companion object {
        const val PROFILE_KEY = "favorite.ky"
    }

    private lateinit var cancelButton: ImageButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var gAdapter: FavoriteListAdapter

    private var gId: Int
    private var gFavoriteId: Int

    init {
        gFavoriteId = -1
        gId = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        gFavoriteId = args?.getInt(PROFILE_KEY) ?: 0

        val activityIntent = requireActivity().intent.extras
        gId = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)
        if(gId == -1) findNavController().popBackStack()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.favorite_list_select, container, false)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        recyclerView = rootView.findViewById(R.id.favorite_list_recyclerview)
        initializer()

        Toast.makeText(requireContext(), "ID is: $gFavoriteId", Toast.LENGTH_SHORT).show()

        return rootView
    }

    fun initializer() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            gAdapter = FavoriteListAdapter(this@FavoriteListSelectFragment)
            adapter = gAdapter
            addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        }

        gAdapter.submitList(DatabaseManager(requireContext()).readLists(gId))

        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onClick(position: Int, list: ListObject) {
        //Toast.makeText(requireContext(), "Position: $position and List: ${list.toString()}", Toast.LENGTH_LONG).show()
        val fragment = AddFavoriteTodoBottomFragment()
        fragment.setInterface(this@FavoriteListSelectFragment)
        fragment.id = gId
        fragment.setListId(list.listId)
        fragment.setFavoriteId(gFavoriteId)
        fragment.show(childFragmentManager, fragment.tag)
    }

    override fun dismissAction() {
        findNavController().popBackStack()
    }
}