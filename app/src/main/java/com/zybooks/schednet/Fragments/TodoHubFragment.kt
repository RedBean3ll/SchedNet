package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zybooks.schednet.R
import com.zybooks.schednet.databinding.TodoHubBinding

class TodoHubFragment: Fragment() {

    private lateinit var binding: TodoHubBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TodoHubBinding.inflate(layoutInflater)
        val rootView: View = binding.root

        binding.thnDeleteFavorite.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.show_todo_list)
        }

        return rootView
    }
}