package com.zybooks.schednet.Fragments.BottomFragments

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zybooks.schednet.Model.TodoViewModel
import com.zybooks.schednet.databinding.TodoNewRibbonFrameBinding

class AddTodoBottomFragment: BottomSheetDialogFragment() {
    //DEBUG
    val TAG = "ActionBottomDialog"
    val test = ""

    private lateinit var binding: TodoNewRibbonFrameBinding
    private lateinit var tViewModel: TodoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        tViewModel = ViewModelProvider(activity).get(TodoViewModel::class.java)
        binding.newribbonChipDescription.setOnClickListener {
            toggleDescriptionVisible()
        }
        binding.newribbonSave.setOnClickListener {
            saveAction()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TodoNewRibbonFrameBinding.inflate(inflater, container, false)
        //NEEDS CODE TO USE DIFFERENT METHOD ON LATER VERSIONS FIXME
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        return binding.root
    }

    private fun saveAction() {
        tViewModel.TodoTitle.value = binding.newribbonName.text.toString()
        tViewModel.TodoDescription.value = if(binding.newribbonDescriptionCasing.isVisible) binding.newribbonDescription.text.toString() else ""
        binding.newribbonName.setText("")
        binding.newribbonDescription.setText("")
        dismiss()
    }

    //Guarantee flip
    private fun toggleDescriptionVisible() {
        binding.newribbonDescriptionCasing.isVisible = !binding.newribbonDescriptionCasing.isVisible
    }

}