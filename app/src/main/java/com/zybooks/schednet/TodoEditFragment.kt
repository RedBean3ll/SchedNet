package com.zybooks.schednet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zybooks.schednet.databinding.TodoEditRibbonPageBinding

class TodoEditFragment(bundle: Bundle, contentLayoutId: Int): Fragment() {

    private lateinit var binding: TodoEditRibbonPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.todo_edit_ribbon_page, container, false)

        return rootView
    }

}