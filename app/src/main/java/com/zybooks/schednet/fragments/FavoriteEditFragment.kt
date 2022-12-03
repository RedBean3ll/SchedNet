package com.zybooks.schednet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.model.FavoriteDataObject
import com.zybooks.schednet.R
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: FavoriteEditFragment.kt
 *  @author Matthew Clark
 */

class FavoriteEditFragment: Fragment() {

    companion object {
        const val PROFILE_KEY = "favorite.ky"
        const val MODE_KEY = "mode.ky"
    }

    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton
    private lateinit var profileEditText: TextInputEditText
    private lateinit var profileLayout: TextInputLayout
    private lateinit var nameEditText: TextInputEditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var pinSwitch: SwitchCompat

    private var gFavoriteId: Int
    private var gMode: Boolean

    private lateinit var gProfile: FavoriteDataObject

    init {
        gFavoriteId = -1
        gMode = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        gFavoriteId = args?.getInt(PROFILE_KEY) ?: 0
        gMode = args?.getBoolean(MODE_KEY) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.favorite_edit, container, false)
        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        profileEditText = rootView.findViewById(R.id.edit_favorite_profile_name_edit)
        profileLayout = rootView.findViewById(R.id.edit_favorite_profile_name_layout)
        nameEditText = rootView.findViewById(R.id.edit_favorite_name_edit)
        nameLayout = rootView.findViewById(R.id.edit_favorite_name_layout)
        descriptionEditText = rootView.findViewById(R.id.edit_favorite_description_edit)
        descriptionLayout = rootView.findViewById(R.id.edit_favorite_description_layout)
        pinSwitch = rootView.findViewById(R.id.edit_favorite_switch)
        initializer()

        return rootView
    }

    fun initializer() {
        gProfile = if(gMode) DatabaseManager(requireContext()).readFavoriteCalendarSingle(gFavoriteId) else DatabaseManager(requireContext()).readFavoriteTodoSingle(gFavoriteId)

        if(gMode) profileLayout.hint = "Calendar Profile"
        else profileLayout.hint = "Todo Profile"

        profileEditText.setText(gProfile.favoriteName)
        nameEditText.setText(gProfile.eventName)
        descriptionEditText.setText(gProfile.eventDescription)
        pinSwitch.isChecked = gProfile.isPinned

        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        confirmButton.setOnClickListener {
            profileLayout.error = ""
            nameLayout.error = ""
            if(profileEditText.text.toString().isEmpty() || nameEditText.text.toString().isEmpty()) {
                if(profileEditText.text.toString().isEmpty()) profileLayout.error = "Profile name field is empty"
                if(nameEditText.text.toString().isEmpty()) nameLayout.error = "Event name field is empty"
                return@setOnClickListener
            }

            //EDIT DB
            gProfile.favoriteName = profileEditText.text.toString()
            gProfile.eventName = nameEditText.text.toString()
            gProfile.eventDescription = descriptionEditText.text.toString()
            gProfile.isPinned = pinSwitch.isChecked
            DatabaseManager(requireContext()).updateFavoriteProfile(gMode, gProfile)
            //check → db → pop
            findNavController().popBackStack()
        }
    }
}