package com.zybooks.schednet.Fragments.BottomFragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zybooks.schednet.Model.ListModel
import com.zybooks.schednet.Model.StateViewModel
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.databinding.ListNewRibbonFrameBinding

class AddListBottomFragment(MagicNumberInstance: Int): BottomSheetDialogFragment() {

    private lateinit var binding: ListNewRibbonFrameBinding
    private var pinned: Boolean
    private var magicNumber: Int
    private val state: StateViewModel by activityViewModels()

    init {
        pinned = false
        magicNumber = MagicNumberInstance
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ListNewRibbonFrameBinding.inflate(inflater, container, false)

        binding.genericSaveButton.setOnClickListener {
            if(binding.newListEdit.text.toString().length > 0) {
                saveAction(binding.newListEdit.text.toString(), pinned, magicNumber)
                state.PageState.value = true
                dismiss()
            } else {
                binding.newListLayout.error = "Name field is empty"
            }
        }

        binding.newListPriority.setOnClickListener {
            pinned = !pinned
            if(pinned) {
                binding.newListPriority.setImageResource(R.drawable.ic_baseline_push_pin_24)
            } else {
                binding.newListPriority.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)
            }
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        state.AccessState.value = true
    }

    private fun saveAction(listName: String, isPinned: Boolean, magicNumber: Int) {
        val obj = ListModel(0, listName, isPinned, null)
        val dbm = DatabaseManager(requireContext())
        dbm.insertList(obj, magicNumber)
    }



}

