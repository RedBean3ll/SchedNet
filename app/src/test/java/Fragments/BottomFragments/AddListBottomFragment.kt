package com.zybooks.schednet.Fragments.BottomFragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zybooks.schednet.Model.TodoViewModel
import com.zybooks.schednet.R
import com.zybooks.schednet.databinding.ListNewRibbonFrameBinding
import com.zybooks.schednet.databinding.TodoNewRibbonFrameBinding
import kotlin.ClassCastException

class AddListBottomFragment: BottomSheetDialogFragment() {
    private lateinit var binding: ListNewRibbonFrameBinding
    private var pinned: Boolean

    init {
        pinned = false
    }

    private val viewModel: TodoViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ListNewRibbonFrameBinding.inflate(inflater, container, false)

        binding.newribbonSave.setOnClickListener {
            saveAction()
            dismiss()
        }

        binding.newribbonPriority.setOnClickListener {
            pinned = !pinned
            if(pinned) {
                binding.newribbonPriority.setImageResource(R.drawable.ic_baseline_push_pin_24)
            } else {
                binding.newribbonPriority.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveAction() {
        viewModel.TodoTitle.value = binding.newribbonName.text.toString()
        viewModel.TodoStatus.value = pinned
    }



}