package com.zybooks.schednet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.adapter.RecyclerViewTouchHelper
import com.zybooks.schednet.adapter.FavoriteAdapter
import com.zybooks.schednet.fragments.bottom_fragments.AddFavoriteBottomFragment
import com.zybooks.schednet.fragments.bottom_fragments.AddFavoriteCalendarBottomFragment
import com.zybooks.schednet.model.FavoriteObject
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: FavoriteFragment.kt
 *  @author Matthew Clark
 */

class FavoriteFragment: Fragment(), FavoriteAdapter.OnRibbonListener, RecyclerViewTouchHelper.OnSwipeInteraction, AddFavoriteBottomFragment.OnDismissAddInteraction {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pageLabel: TextView
    private lateinit var switchButton: ImageButton
    private lateinit var addFavoriteButton: ImageButton

    private lateinit var favoriteTodo: MutableList<FavoriteObject>
    private lateinit var favoriteCalendar: MutableList<FavoriteObject>
    private var gControl: Boolean
    private var gMode: Boolean //false = tdo, true = favorite
    private var gId: Int
    private lateinit var gAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get user ID from activity
        val activityIntent = requireActivity().intent.extras
        gId = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)

        //Pull mode from db
        gMode = DatabaseManager(requireContext()).readPreferredFavoritePage(gId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.favorite, container, false)
        pageLabel = rootView.findViewById(R.id.favorite_label)
        recyclerView = rootView.findViewById(R.id.favorite_recyclerview)
        switchButton = rootView.findViewById(R.id.favorite_actionbar_switch)
        addFavoriteButton = rootView.findViewById(R.id.favorite_actionbar_new_profile)
        initializer()

        return rootView
    }

    init {
        gMode = false
        gControl = true
        gId = -1
    }

    fun initializer() {
        gMode = DatabaseManager(requireContext()).readDefaultFavorite(gId)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            gAdapter = FavoriteAdapter(this@FavoriteFragment)
            adapter = gAdapter
            ItemTouchHelper(RecyclerViewTouchHelper(requireContext(), this@FavoriteFragment)).attachToRecyclerView(this)
            addItemDecoration( DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        }
        //LIST SETUP
        favoriteCalendar = DatabaseManager(requireContext()).readCalendarProfile(gId)
        favoriteTodo = DatabaseManager(requireContext()).readTodoProfile(gId)

        //STARTER MODE
        if(gMode) {
            pageLabel.text = resources.getString(R.string.favorite_calendar_name_text)
            gAdapter.submitNewList(favoriteCalendar.toList())
        } else {
            pageLabel.text = resources.getString(R.string.favorite_todo_name_text)
            gAdapter.submitNewList(favoriteTodo.toList())
        }

        //ACTIONS
        switchButton.setOnClickListener {
            gMode = !gMode
            if(gMode) {
                gAdapter.submitNewList(favoriteCalendar.toList())
                pageLabel.text = resources.getString(R.string.favorite_calendar_name_text)
                return@setOnClickListener
            }
            gAdapter.submitNewList(favoriteTodo.toList())
            pageLabel.text = resources.getString(R.string.favorite_todo_name_text)

        }

        addFavoriteButton.setOnClickListener {
            if(gControl) {
                gControl = false

                val fragment = AddFavoriteBottomFragment()
                fragment.setInterface(this@FavoriteFragment)
                fragment.id = gId
                fragment.setMode(gMode)
                fragment.show(childFragmentManager, fragment.tag)
            }
        }

    }

    override fun onClick(position: Int, favorite: FavoriteObject) {
        val bundle = Bundle()
        bundle.putInt(FavoriteEditFragment.PROFILE_KEY, favorite.favoriteId)
        bundle.putBoolean(FavoriteEditFragment.MODE_KEY, gMode)
        findNavController().navigate(R.id.show_favorite_edit, bundle)
    }

    override fun onAddClick(position: Int, favorite: FavoriteObject) {
        if(gMode) {
            val fragment = AddFavoriteCalendarBottomFragment()
            fragment.id = gId
            fragment.setProfileId(favorite.favoriteId)
            fragment.show(childFragmentManager, fragment.tag)
            return
        }
        val bundle = Bundle()
        bundle.putInt(FavoriteListSelectFragment.PROFILE_KEY, favorite.favoriteId)
        findNavController().navigate(R.id.show_favorite_list_select, bundle)
    }

    override fun onPin(position: Int, favorite: FavoriteObject) {
        DatabaseManager(requireContext()).updateFavoriteProfilePinValue(favorite.favoriteId, gMode, favorite.isPinned)
        if(favorite.isPinned) {
            if(gMode) {
                favoriteCalendar.removeAt(position)
                favoriteCalendar.add(0, favorite)
                gAdapter.submitNewList(favoriteCalendar.toList())
                return
            }
            //UPDATE PIN and STAMP in DB
            favoriteTodo.removeAt(position)
            favoriteTodo.add(0, favorite)
            gAdapter.submitNewList(favoriteTodo.toList())
        }
    }

    override fun onSwipeLeft(position: Int) {
        if(gMode) {
            DatabaseManager(requireContext()).deleteFavoriteProfile(favoriteCalendar[position].favoriteId, true)
            favoriteCalendar.removeAt(position)
            gAdapter.submitNewList(favoriteCalendar.toList())
            return
        }
        DatabaseManager(requireContext()).deleteFavoriteProfile(favoriteTodo[position].favoriteId, false)
        favoriteTodo.removeAt(position)
        gAdapter.submitNewList(favoriteTodo.toList())
    }

    override fun confirmAddition(favoriteProfile: FavoriteObject) {
        //Ensure added to correct list
        if(favoriteProfile.favoriteType) favoriteCalendar.add(0, favoriteProfile)
        else favoriteTodo.add(0, favoriteProfile)

        //Submit list on current mode
        if(gMode) gAdapter.submitNewList(favoriteCalendar.toList())
        else gAdapter.submitNewList(favoriteTodo.toList())
    }

    override fun cancelAddition() {
        gControl = true
    }
}