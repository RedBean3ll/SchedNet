package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zybooks.schednet.R
import com.zybooks.schednet.databinding.TodoEditBinding

class TodoEditFragment(): Fragment() {

    companion object {
        const val EVENTKEY = "event.ky"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.todo_edit, container, false)
        return rootView
    }


}