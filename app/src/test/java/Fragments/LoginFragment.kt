package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zybooks.schednet.R
import com.zybooks.schednet.StageActivity
import com.zybooks.schednet.databinding.LoginBinding
import android.content.Intent

class LoginFragment: Fragment() {

    private lateinit var binding: LoginBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LoginBinding.inflate(layoutInflater)
        val rootView: View = binding.root

        binding.loginRegisterButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.show_signup)
        }
        binding.loginForgotButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.show_forgot)
        }
        binding.loginLocalButton.setOnClickListener {
            val data = Intent(activity, StageActivity::class.java)
            startActivity(data)
        }



        return rootView
    }

    //binding.loginUsernameLayout.error = "TESTING"
    //binding.loginUsernameLayout.error = ""
}

