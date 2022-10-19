package com.zybooks.schednet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val backBtn: Button = findViewById(R.id.backBtn)
        val loginBtn: Button = findViewById(R.id.LoginBtn2)
    var emailTxt: EditText = findViewById(R.id.editTextTextEmailAddress)


        backBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginPage, MainActivity::class.java)
            startActivity(intent)
        })

        loginBtn.setOnClickListener{
          val intent = Intent(this@LoginPage, HomePage::class.java)
            startActivity(intent)
        }
        }
    }
