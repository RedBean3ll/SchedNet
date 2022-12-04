package com.zybooks.schednet.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.fragment.findNavController
import com.zybooks.schednet.fragments.bottom_fragments.NewNameBottomFragment
import com.zybooks.schednet.fragments.bottom_fragments.NewPasswordBottomFragment
import com.zybooks.schednet.MainActivity
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: SettingsFragment.kt
 *  @author Matthew Clark
 */

class SettingsFragment : Fragment(), AlertFragment.OnDismissDialog, NewNameBottomFragment.OnDismissName, NewPasswordBottomFragment.OnDismissAction{

    private lateinit var nameLabel: TextView
    private lateinit var updateNameButton: Button
    private lateinit var updateQuestionButton: Button
    private lateinit var updatePasswordButton: Button
    private lateinit var todoExpirationSpinner: Spinner
    private lateinit var calendarExpirationSpinner: Spinner
    private lateinit var defaultFavoriteListSwitch: SwitchCompat
    private lateinit var logoutButton: Button
    private lateinit var deleteAccountButton: Button

    private var gId: Int
    private var gButtonControl: Boolean
    private var gSpinnerControl: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityIntent = requireActivity().intent.extras
        gId = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.settings, container, false)
        nameLabel = rootView.findViewById(R.id.settings_label)
        updateNameButton = rootView.findViewById(R.id.settings_update_name_button)
        updateQuestionButton = rootView.findViewById(R.id.settings_update_question_button)
        updatePasswordButton = rootView.findViewById(R.id.settings_update_password_button)
        todoExpirationSpinner = rootView.findViewById(R.id.settings_todo_expiration_spinner)
        calendarExpirationSpinner = rootView.findViewById(R.id.settings_calendar_expiration_spinner)
        defaultFavoriteListSwitch = rootView.findViewById(R.id.settings_favorite_mode_switch)
        logoutButton = rootView.findViewById(R.id.settings_logout_button)
        deleteAccountButton = rootView.findViewById(R.id.settings_delete_button)
        initializer()

        return rootView
    }

    private fun initializer() {
        //0 = tdo, 1 = cal
        val tempVal = DatabaseManager(requireContext()).readGarbageCollectorValues(gId)

        nameLabel.text = DatabaseManager(requireContext()).readAccountName(gId)
        defaultFavoriteListSwitch.isChecked = DatabaseManager(requireContext()).readDefaultFavorite(gId)
        todoExpirationSpinner.setSelection(tempVal[0])
        calendarExpirationSpinner.setSelection(tempVal[1])

        updateNameButton.setOnClickListener {
            if(!gButtonControl) return@setOnClickListener
            gButtonControl = false

            val fragmentName = NewNameBottomFragment()
            fragmentName.setInterface(this@SettingsFragment)
            fragmentName.id = gId
            fragmentName.show(childFragmentManager, fragmentName.tag)
        }

        updateQuestionButton.setOnClickListener {
            findNavController().navigate(R.id.show_question_edit)
        }

        updatePasswordButton.setOnClickListener {
            if(!gButtonControl) return@setOnClickListener
            gButtonControl = false

            val fragmentPassword = NewPasswordBottomFragment()
            fragmentPassword.setInterface(this@SettingsFragment)
            fragmentPassword.id = gId
            fragmentPassword.show(childFragmentManager, fragmentPassword.tag)
        }

        logoutButton.setOnClickListener {
            if(!gButtonControl) return@setOnClickListener
            gButtonControl = false

            DatabaseManager(requireContext()).revokeAutoLogin(gId)
            endSession()
        }

        deleteAccountButton.setOnClickListener {
            if(!gButtonControl) return@setOnClickListener
            gButtonControl = false

            val alert = AlertFragment()
            alert.setInterface(this@SettingsFragment)
            alert.show(childFragmentManager, alert.tag)
        }

        defaultFavoriteListSwitch.setOnCheckedChangeListener { view, value -> DatabaseManager(requireContext()).updateDefaultFavorite(gId, value) }

        todoExpirationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(gSpinnerControl) {
                    DatabaseManager(requireContext()).updateTodoCollectorValue(gId, position)
                    //Toast.makeText(requireContext(), "${todoExpirationSpinner.selectedItem} Selected @ Position $position", Toast.LENGTH_LONG).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        calendarExpirationSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(gSpinnerControl) {
                    DatabaseManager(requireContext()).updateCalendarCollectorValue(gId, position)
                    //Toast.makeText(requireContext(), "${calendarExpirationSpinner.selectedItem} Selected @ Position $position", Toast.LENGTH_LONG).show()
                } else gSpinnerControl = true
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    init {
        gId = -1
        gButtonControl = true
        gSpinnerControl = false
    }

    fun endSession() {
        val intent = Intent(requireContext(), MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun cancelAction() { } //UNUSED
    override fun confirmAction() { } //UNUSED

    override fun dismissDelete() { gButtonControl = true }
    override fun dismissName() { gButtonControl = true }
    override fun dismissAction() { gButtonControl = true }

    override fun confirmDelete() {
        DatabaseManager(requireContext()).deleteUser(gId)
        endSession()
    }

    override fun confirmName(newName: String) {
        nameLabel.text = newName
    }








}