package com.zybooks.schednet.Fragments.BottomFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zybooks.schednet.databinding.CalendarListBottomBinding
import com.zybooks.schednet.databinding.TestBottomBinding

class TestBottomFragment(onDismissInterface: onDismissInteraction): BottomSheetDialogFragment() {

    private lateinit var binding: CalendarListBottomBinding
    private val inter = onDismissInterface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = CalendarListBottomBinding.inflate(inflater)
        //cancelAction
        /*
        binding.genericCancelButton.setOnClickListener {
            inter.onReturnAction("Canceled")
            dismiss()
        }

        binding.genericConfirmButton.setOnClickListener {
            inter.onReturnAction("Confirmed")
            dismiss()
        }
        */


        binding

        return binding.root
    }

    interface onDismissInteraction {
        fun onReturnAction(words: String)
    }
}