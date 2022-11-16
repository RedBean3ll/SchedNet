package com.zybooks.schednet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.zybooks.schednet.R
import com.zybooks.schednet.Utils.DatabaseManager
import com.zybooks.schednet.databinding.SignupBinding

class SignupFragment: Fragment() {


    private lateinit var binding: SignupBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SignupBinding.inflate(inflater)

        binding.signupLocalButton.setOnClickListener {
            binding.signupUsernameLayout.error = ""
            binding.signupPasswordLayout.error = ""
            //Set error state if empty
            val lengthA = binding.signupUsernameEdit.text.toString().length
            val lengthB = binding.signupPasswordEdit.text.toString().length
            if(lengthA == 0 || lengthB == 0 ) {
                if(lengthA == 0) {
                    binding.signupUsernameLayout.error = "Username field is empty"
                }
                if(lengthB == 0) {
                    binding.signupPasswordLayout.error = "Password field is empty"
                }
                return@setOnClickListener
            }

            val dbm = DatabaseManager(requireContext())
            if(dbm.insertUser(binding.signupUsernameEdit.text.toString(), binding.signupPasswordEdit.text.toString())) {
                //Animation?
                Toast.makeText(requireContext(), "Welcome to SchedNet!", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStackImmediate()
            }

        }



        return binding.root
    }
}
