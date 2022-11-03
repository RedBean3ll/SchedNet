package com.zybooks.schednet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.zybooks.schednet.Utils.DatabaseHandler

class AddNewTodo : BottomSheetDialogFragment() {

    val TAG = "ActionBottomDialog"

    private var nameEdit: EditText? = null
    //private var chipSet: ChipGroup? = null
    private var priorityChippet: Chip? = null
    private var dueChippet: Chip? = null
    private var repeatChippet: Chip? = null
    private var remindChippet: Chip? = null
    private var db: DatabaseHandler? = null

    fun AddNewTodo() : AddNewTodo {
        return AddNewTodo()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.todo_new_ribbon_frame, container, false)
        //API CHECK
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEdit = getView()?.findViewById(R.id.newribbon_name)

    }

}