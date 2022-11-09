package com.zybooks.schednet

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.zybooks.schednet.R.*

class DebugMenu : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_debug_menu)

        //toTdoLocal.setOnClickListener { this.transitionTdo() };
        val loginBtn: Button=findViewById(id.LoginBtn)
        val signUpBtn: Button=findViewById(id.SignUpBtn)

        loginBtn.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        signUpBtn.setOnClickListener{
            val intent = Intent(this, Sign_up::class.java)
            startActivity(intent)
        }



    }
/*
    fun callLogin(view: View) {

    }

    fun callSignUP(view: View) {

    }

    fun callMainHub(view: View) {

    }

    fun callGroupHub(view: View) {

    }

    fun callLocalHub(view: View) {

    }

    fun callLocalTDOPannel(view: View) {
        val intend: Intent = Intent(this, TodoLocal::class.java)
        startActivity(intend)
    }

    fun callFavorite(view: View) {

    }

    fun callTrash(view: View) {

    }*/



}