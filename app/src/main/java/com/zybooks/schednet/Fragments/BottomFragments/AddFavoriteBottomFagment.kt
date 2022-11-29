package com.zybooks.schednet.Fragments.BottomFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zybooks.schednet.Model.FavoriteObject
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager

class AddFavoriteBottomFagment: BottomSheetDialogFragment() {

    private lateinit var cancelButton: ImageButton
    private lateinit var confirmButton: ImageButton
    private lateinit var profileEditText: TextInputEditText
    private lateinit var profileLayout: TextInputLayout
    private lateinit var pinButton: ImageButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var nameChip: Chip
    private lateinit var descriptionChip: Chip
    private lateinit var nameErrorText: TextView

    private var gId: Int
    private var gMode: Boolean
    private var pinned: Boolean
    private var onDismissAddInteraction: OnDismissAddInteraction?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.favorite_new_ribbon_frame, container, false)

        if(dialog is BottomSheetDialog) {
            val behaviour = (dialog as BottomSheetDialog).behavior
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            behaviour.isDraggable = false
        }

        cancelButton = rootView.findViewById(R.id.generic_cancel_button)
        confirmButton = rootView.findViewById(R.id.generic_confirm_button)
        profileEditText = rootView.findViewById(R.id.new_favorite_profile_name_edit)
        profileLayout = rootView.findViewById(R.id.new_favorite_profile_name_layout)
        pinButton = rootView.findViewById(R.id.new_favorite_pin)
        nameEditText = rootView.findViewById(R.id.new_favorite_name_edit)
        nameLayout = rootView.findViewById(R.id.new_favorite_name_layout)
        descriptionEditText = rootView.findViewById(R.id.new_favorite_description_edit)
        descriptionLayout = rootView.findViewById(R.id.new_favorite_description_layout)
        nameChip = rootView.findViewById(R.id.new_favorite_chip_name)
        descriptionChip = rootView.findViewById(R.id.new_favorite_chip_description)
        nameErrorText = rootView.findViewById(R.id.new_favorite_event_name_missing)
        initializer()

        return  rootView
    }

    fun initializer() {
        if(gMode) profileLayout.setHint("Calendar Profile")
        else profileLayout.setHint("Todo Profile")

        cancelButton.setOnClickListener {
            dismiss()
        }

        confirmButton.setOnClickListener {
            profileLayout.error = ""
            nameErrorText.isVisible = false
            if(profileEditText.text.toString().isEmpty() || nameEditText.text.toString().isEmpty()) {
                if(profileEditText.text.toString().isEmpty()) {
                    profileLayout.error = "Profile name field is empty"
                }
                if(nameEditText.text.toString().isEmpty()) {
                    nameErrorText.isVisible = true
                }
                return@setOnClickListener
            }

            val creationStamp: Long
            val favoriteObject: FavoriteObject
            if(gMode) {
                creationStamp = DatabaseManager(requireContext()).insertCalendarProfile(gId, profileEditText.text.toString(), nameEditText.text.toString(), descriptionEditText.text.toString(), pinned)
                favoriteObject = FavoriteObject(DatabaseManager(requireContext()).readNewCalendarProfileId(creationStamp),profileEditText.text.toString(), pinned, true)
                onDismissAddInteraction?.confirmAddition(favoriteObject)
                dismiss()
                return@setOnClickListener
            }
            creationStamp = DatabaseManager(requireContext()).insertTodoProfile(gId, profileEditText.text.toString(), nameEditText.text.toString(), descriptionEditText.text.toString(), pinned)
            favoriteObject = FavoriteObject(DatabaseManager(requireContext()).readNewTodoProfileId(creationStamp), profileEditText.text.toString(), pinned, false)
            onDismissAddInteraction?.confirmAddition(favoriteObject)
            dismiss()
        }

        pinButton.setOnClickListener {
            pinned = !pinned
            if(pinned) {
                pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
                return@setOnClickListener
            }
            pinButton.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)
        }

        nameChip.setOnClickListener {
            nameLayout.isVisible = !nameLayout.isVisible
            descriptionLayout.isVisible = false
        }

        descriptionChip.setOnClickListener {
            descriptionLayout.isVisible = !descriptionLayout.isVisible
            nameLayout.isVisible = false
        }
    }

    //RESET CONTROL
    override fun dismiss() {
        super.dismiss()
        onDismissAddInteraction?.cancelAddition()
    }

    //INIT
    init {
        gId = -1
        gMode = false
        pinned = false
        this.onDismissAddInteraction = null
    }

    fun setInterface(onDismissAddInteraction: OnDismissAddInteraction) {
        this.onDismissAddInteraction = onDismissAddInteraction
    }
    fun setId(id: Int) {
        gId = id
    }
    fun setMode(mode: Boolean) {
        gMode = mode
    }

    //INITIALIZER FUNC

    interface OnDismissAddInteraction {
        fun confirmAddition(favoriteProfile: FavoriteObject)
        fun cancelAddition()
    }
}