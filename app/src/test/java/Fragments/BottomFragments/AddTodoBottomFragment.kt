package com.zybooks.schednet.Fragments.BottomFragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zybooks.schednet.Model.TodoViewModel
import com.zybooks.schednet.databinding.TodoNewRibbonFrameBinding
import kotlin.ClassCastException

class AddTodoBottomFragment: BottomSheetDialogFragment() {
    private lateinit var binding: TodoNewRibbonFrameBinding
    private lateinit var tViewModel: TodoViewModel

    private val viewModel: TodoViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TodoNewRibbonFrameBinding.inflate(inflater, container, false)



        val activity = getActivity()
        tViewModel = ViewModelProvider(activity!!).get(TodoViewModel::class.java)
        binding.newribbonChipDescription.setOnClickListener {
            toggleDescriptionVisible()
        }
        binding.newribbonSave.setOnClickListener {
            saveAction()
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveAction() {
        viewModel.TodoTitle.value = binding.newribbonName.text.toString()
        viewModel.TodoDescription.value = if(binding.newribbonDescriptionCasing.isVisible) binding.newribbonDescription.text.toString() else ""

    }


    //Guarantee flip
    private fun toggleDescriptionVisible() {
        binding.newribbonDescriptionCasing.isVisible = !binding.newribbonDescriptionCasing.isVisible
    }


}