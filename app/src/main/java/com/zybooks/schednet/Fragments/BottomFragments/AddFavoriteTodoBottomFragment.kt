package com.zybooks.schednet.Fragments.BottomFragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zybooks.schednet.Model.TodoViewModel

import com.zybooks.schednet.databinding.FavoriteTodoNewRibbonFrameBinding


class AddFavoriteTodoBottomFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FavoriteTodoNewRibbonFrameBinding
    private var pinned: Boolean = false

    private val viewModel: TodoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FavoriteTodoNewRibbonFrameBinding.inflate(inflater, container, false)

        /*binding.newribbonSave.setOnClickListener {
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
         */

        return binding.root
    }

    private fun saveAction() {
        viewModel.TodoTitle.value = binding.newFavoriteTodoEdit.text.toString()
        viewModel.TodoStatus.value = pinned
    }



}