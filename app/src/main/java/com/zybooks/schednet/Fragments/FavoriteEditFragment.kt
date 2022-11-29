package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.Model.FavoriteDataObject
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager

class FavoriteEditFragment: Fragment() {

    companion object {
        const val PROFILEKEY = "favorite.ky"
        const val MODEKEY = "mode.ky"
    }

    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton
    private lateinit var profileEditText: TextInputEditText
    private lateinit var profileLayout: TextInputLayout
    private lateinit var nameEditText: TextInputEditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var pinCheckBox: CheckBox

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
        gFavoriteId = args?.getInt(PROFILEKEY) ?: 0
        gMode = args?.getBoolean(MODEKEY) ?: false
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
        pinCheckBox = rootView.findViewById(R.id.edit_favorite_check_box)
        initializer()

        Toast.makeText(requireContext(), "ID is: $gFavoriteId AND Mode is: $gMode", Toast.LENGTH_SHORT).show()

        return rootView
    }

    fun initializer() {
        gProfile = if(gMode) DatabaseManager(requireContext()).readFavoriteCalendarSingle(gFavoriteId) else DatabaseManager(requireContext()).readFavoriteTodoSingle(gFavoriteId)

        if(gMode) profileLayout.setHint("Calendar Profile")
        else profileLayout.setHint("Todo Profile")

        profileEditText.setText(gProfile.favoriteName)
        nameEditText.setText(gProfile.eventName)
        descriptionEditText.setText(gProfile.eventDescription)
        pinCheckBox.isChecked = gProfile.isPinned

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
            gProfile.isPinned = pinCheckBox.isChecked
            DatabaseManager(requireContext()).updateFavoriteProfile(gMode, gProfile)
            //check → db → pop
            findNavController().popBackStack()
        }
    }
}