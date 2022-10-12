package com.zybooks.seniorproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.time.Instant

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //buttons
        val signUpBtn: Button = findViewById(R.id.SignUpBtn)
        val loginBtn: Button = findViewById(R.id.LoginBtn)




        // moving to different pages
        signUpBtn.setOnClickListener(View.OnClickListener {
         val intent = Intent(this@MainActivity, SignUpForm::class.java)
         startActivity(intent)
 })

       loginBtn.setOnClickListener(View.OnClickListener {
           val intent = Intent(this@MainActivity, LoginPage::class.java)
           startActivity(intent)
       })
    }
}
