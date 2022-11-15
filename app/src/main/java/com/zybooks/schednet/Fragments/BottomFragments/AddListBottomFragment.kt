package com.zybooks.schednet.Fragments.BottomFragments

import android.content.Context
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
import com.zybooks.schednet.Model.TodoViewModel
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.databinding.ListNewRibbonFrameBinding
import com.zybooks.schednet.databinding.TodoNewRibbonFrameBinding
import kotlin.ClassCastException

class AddListBottomFragment(MagicNumberInstance: Int): BottomSheetDialogFragment() {

    private lateinit var binding: ListNewRibbonFrameBinding
    private var pinned: Boolean
    private var magicNumber: Int
    private val state: StateViewModel by activityViewModels()

    init {
        pinned = false
        magicNumber = MagicNumberInstance
    }

    private val viewModel: TodoViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ListNewRibbonFrameBinding.inflate(inflater, container, false)

        binding.genericSaveButton.setOnClickListener {
            saveAction(binding.newListEdit.text.toString(), pinned, magicNumber)
            state.PageState.value = true
            dismiss()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveAction(listName: String, isPinned: Boolean, magicNumber: Int) {
        val obj = ListModel()
        obj.ListName = listName
        obj.isPinned = isPinned
        val dbm = DatabaseManager(requireContext())
        dbm.insertList(obj, magicNumber)
    }



}