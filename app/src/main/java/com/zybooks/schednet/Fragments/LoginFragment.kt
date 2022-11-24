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
import android.widget.Toast
import com.zybooks.schednet.Utils.DatabaseManager

class LoginFragment: Fragment() {

    private lateinit var binding: LoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LoginBinding.inflate(layoutInflater)

        binding.loginRegisterButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.show_signup)
        }
        binding.loginForgotButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.show_forgot)
        }
        binding.loginLocalButton.setOnClickListener {
            //Return default error state
            binding.loginUsernameLayout.error = ""
            binding.loginPasswordLayout.error = ""
            //Set error state if empty
            val lengthA = binding.loginUsernameEdit.text.toString().length
            val lengthB = binding.loginPasswordEdit.text.toString().length
            if(lengthA == 0 || lengthB == 0 ) {
                if(lengthA == 0) {
                    binding.loginUsernameLayout.error = "Username field is empty"
                }
                if(lengthB == 0) {
                    binding.loginPasswordLayout.error = "Password field is empty"
                }
                return@setOnClickListener
            }

            val dbm = DatabaseManager(requireContext())
            val attempt = dbm.readUser(binding.loginUsernameEdit.text.toString(), binding.loginPasswordEdit.text.toString())
            if(attempt > -1) {
                //Toast.makeText(requireContext(), "SUCCESS!! with ID: $attempt", Toast.LENGTH_LONG).show()
                if(binding.loginCheckBoxRemember.isChecked) {
                    dbm.updatePreferredLogin(attempt, true)
                }
                val intent = Intent(activity, StageActivity::class.java)
                intent.putExtra(StageActivity.MAGIC_NUMBER, attempt)
                startActivity(intent)
            }
        }

        return binding.root
    }

    //binding.loginUsernameLayout.error = "TESTING"
    //binding.loginUsernameLayout.error = ""
}


