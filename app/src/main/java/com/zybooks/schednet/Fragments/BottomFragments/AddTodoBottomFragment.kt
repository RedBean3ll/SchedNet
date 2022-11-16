package com.zybooks.schednet.Fragments.BottomFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zybooks.schednet.Model.StateViewModelTodo
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.databinding.TodoNewRibbonFrameBinding

/**
 * If chip box filled, change color of chip on action of value updated
 */

class AddTodoBottomFragment(MagicNumberInstance: Int, AltMagicNumberInstance: Int): BottomSheetDialogFragment() {
    private lateinit var binding: TodoNewRibbonFrameBinding
    private var pinned: Boolean
    private var magicNumber: Int
    private var altMagicNumber: Int
    private val viewModel: StateViewModelTodo by activityViewModels()

    init {
        pinned = false
        magicNumber = MagicNumberInstance
        altMagicNumber = AltMagicNumberInstance
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TodoNewRibbonFrameBinding.inflate(inflater, container, false)
        binding.genericSaveButton.setOnClickListener {
            if(binding.newTodoNameEdit.text.toString().length > 0) {
                saveAction(magicNumber, altMagicNumber, binding.newTodoNameEdit.text.toString(), binding.newTodoDescriptionEdit.text.toString(), pinned )
                viewModel.PageState.value = true
                dismiss()
            } else {
                binding.newTodoNameLayout.error = "Name field is empty"
            }
        }



        binding.newTodoChipDescription.setOnClickListener {
            toggleDescriptionVisible()
        }

        binding.newTodoPriority.setOnClickListener {
            pinned = !pinned
            if(pinned) {
                binding.newTodoPriority.setImageResource(R.drawable.ic_baseline_push_pin_24)
            } else {
                binding.newTodoPriority.setImageResource(R.drawable.ic_baseline_push_pin_alt_24)
            }
        }

        return binding.root
    }

    private fun saveAction(id: Int, listId: Int, eventName: String, description: String, isPinned: Boolean) {
        val obj = TodoModel()
        obj.TodoName = eventName
        obj.TodoDescription = description
        obj.TodoPinned = isPinned
        val dbm = DatabaseManager(requireContext())
        dbm.insertTodo(id, listId, obj)
    }

    //Guarantee flip
    private fun toggleDescriptionVisible() {
        binding.newTodoDescriptionCasing.isVisible = !binding.newTodoDescriptionCasing.isVisible
    }


}