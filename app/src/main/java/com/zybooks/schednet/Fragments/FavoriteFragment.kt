package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zybooks.schednet.Adapter.AdapterTouchHelper.RecyclerViewTouchHelper
import com.zybooks.schednet.Adapter.FavoriteAdapter
import com.zybooks.schednet.Fragments.BottomFragments.AddFavoriteBottomFagment
import com.zybooks.schednet.Fragments.BottomFragments.AddFavoriteCalendarBottomFragment
import com.zybooks.schednet.Model.FavoriteObject
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.Utils.DatabaseManager

class FavoriteFragment: Fragment(), FavoriteAdapter.OnRibbonListener, RecyclerViewTouchHelper.onSwipeInterraction, AddFavoriteBottomFagment.OnDismissAddInteraction {

    private val TAG = "FavoriteFragment"

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

    var counter: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get user ID from activity
        val activityIntent = requireActivity().intent.extras
        gId = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)

        //Pull mode from db
        gMode = DatabaseManager(requireContext()).readPreferredFavorite(gId)
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
        counter = 0
    }

    fun initializer() {
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
            pageLabel.text = "Favorite Calendar"
            gAdapter.submitNewList(favoriteCalendar.toList())
        } else {
            pageLabel.text = "Favorite Todo"
            gAdapter.submitNewList(favoriteTodo.toList())
        }

        //ACTIONS
        switchButton.setOnClickListener {
            gMode = !gMode
            Toast.makeText(requireContext(), "SWAP CLICKED w/ GMODE: $gMode", Toast.LENGTH_SHORT).show()
            if(gMode) {
                gAdapter.submitNewList(favoriteCalendar.toList())
                pageLabel.text = "Favorite Calendar"
                return@setOnClickListener
            }
            gAdapter.submitNewList(favoriteTodo.toList())
            pageLabel.text = "Favorite Todo"

        }

        addFavoriteButton.setOnClickListener {
            if(gControl) {
                gControl = false

                val fragment = AddFavoriteBottomFagment()
                fragment.setInterface(this@FavoriteFragment)
                fragment.id = gId
                fragment.setMode(gMode)
                fragment.show(childFragmentManager, fragment.tag)
            }
        }

    }

    override fun onClick(position: Int, favorite: FavoriteObject) {
        val bundle = Bundle()
        bundle.putInt(FavoriteEditFragment.PROFILEKEY, favorite.favoriteId)
        bundle.putBoolean(FavoriteEditFragment.MODEKEY, gMode)
        findNavController().navigate(R.id.show_favorite_edit, bundle)
        //if(mode)

        //Toast.makeText(requireContext(), "Clicked: ${favorite.favoriteId} w/ name ${favorite.favoriteName}", Toast.LENGTH_LONG).show()
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
        bundle.putInt(FavoriteListSelectFragment.PROFILEKEY, favorite.favoriteId)
        findNavController().navigate(R.id.show_favorite_list_select, bundle)
    }

    override fun onPin(position: Int, favorite: FavoriteObject) {
        DatabaseManager(requireContext()).updateFavoriteProfilePinValue(favorite.favoriteId, gMode, favorite.isPinned)
        if(favorite.isPinned) {
            if(gMode) {
                favoriteCalendar.removeAt(position)
                favoriteCalendar.add(0, favorite)
                gAdapter.submitNewList(favoriteCalendar.toList())
                Log.i(TAG, "PINNED CAL")
                return
            }
            //UPDATE PIN and STAMP in DB
            favoriteTodo.removeAt(position)
            favoriteTodo.add(0, favorite)
            gAdapter.submitNewList(favoriteTodo.toList())
        }
    }

    override fun onSwipeLeft(position: Int) {
        val temp: FavoriteObject
        if(gMode) {
            DatabaseManager(requireContext()).deleteFavoriteProfile(favoriteCalendar[position].favoriteId, true)
            favoriteCalendar.removeAt(position)
            gAdapter.submitNewList(favoriteCalendar.toList())
            Log.i(TAG, "DELETED FROM CAL")
            return
        }
        DatabaseManager(requireContext()).deleteFavoriteProfile(favoriteTodo[position].favoriteId, false)
        favoriteTodo.removeAt(position)
        gAdapter.submitNewList(favoriteTodo.toList())
        Log.i(TAG, "DELETED FROM TDO")

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