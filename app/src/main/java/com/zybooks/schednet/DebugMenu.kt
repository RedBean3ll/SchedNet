package com.zybooks.schednet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class DebugMenu : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_menu)

        //toTdoLocal.setOnClickListener { this.transitionTdo() };


    }

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
        val intend: Intent = Intent(this, TodoActivity::class.java)
        startActivity(intend)
    }

    fun callFavorite(view: View) {

    }

    fun callTrash(view: View) {

    }



}