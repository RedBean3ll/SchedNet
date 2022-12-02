package com.zybooks.schednet.Fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.zybooks.schednet.MainActivity
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.databinding.SettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding: SettingsBinding

    private var gId: Int

    init {
        gId = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityIntent = requireActivity().intent.extras
        gId = activityIntent!!.getInt(StageActivity.MAGIC_NUMBER)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SettingsBinding.inflate(inflater)

        binding.apply {
            binding.settingsLabel.text = DatabaseManager(requireContext()).readAccountName(gId)

            settingsLogoutButton.setOnClickListener {
                DatabaseManager(requireContext()).revokeAutoLogin(gId)
                endSession()
            }

            settingsDeleteButton.setOnClickListener {
                deleteButtonDialog()
            }
        }

        return binding.root
    }

    fun deleteButtonDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder
            .setTitle("Are you sure?")
            .setMessage("Once confirmed, everything will be deleted")
            .setPositiveButton("Confirm") { dialog, which ->
                DatabaseManager(requireContext()).deleteUser(gId)
                endSession()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
        dialogBuilder.show()
    }

    fun endSession() {
        val intent = Intent(requireContext(), MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        requireActivity().finish()
        startActivity(intent)
    }
}