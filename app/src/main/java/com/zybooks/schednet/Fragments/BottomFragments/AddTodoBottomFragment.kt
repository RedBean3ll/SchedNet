package com.zybooks.schednet.Fragments.BottomFragments

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zybooks.schednet.Model.TodoViewModel
import com.zybooks.schednet.databinding.TodoNewRibbonFrameBinding

class AddTodoBottomFragment: BottomSheetDialogFragment() {
    private lateinit var binding: TodoNewRibbonFrameBinding
    private lateinit var tViewModel: TodoViewModel

    private val viewModel: TodoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TodoNewRibbonFrameBinding.inflate(inflater, container, false)

        val activity = activity
        tViewModel = ViewModelProvider(activity!!)[TodoViewModel::class.java]
        binding.newTodoChipDescription.setOnClickListener {
            toggleDescriptionVisible()
        }
        binding.genericSaveButton.setOnClickListener {
            saveAction()
            dismiss()
        }

        return binding.root
    }

    private fun saveAction() {
        viewModel.TodoTitle.value = binding.newTodoEdit.text.toString()
        viewModel.TodoDescription.value = if(binding.newTodoDescriptionCasing .isVisible) binding.newTodoDescriptionEdit.text.toString() else ""
    }

    //Guarantee flip
    private fun toggleDescriptionVisible() {
        binding.newTodoDescriptionCasing.isVisible = !binding.newTodoDescriptionCasing.isVisible
    }


}