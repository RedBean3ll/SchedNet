package com.zybooks.schednet.Fragments.BottomFragments

import android.os.Bundle
import android.view.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.zybooks.schednet.databinding.FavoriteTodoNewRibbonFrameBinding


class AddFavoriteTodoBottomFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FavoriteTodoNewRibbonFrameBinding
    private var pinned: Boolean = false

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
    }



}