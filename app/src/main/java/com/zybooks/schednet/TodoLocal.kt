package com.zybooks.schednet

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ScrollView

class TodoLocal : Activity() {

    //var scrollVw: ScrollView = findViewById(R.id.tdo_task_space)
    //var addRibbonBtn: Button = findViewById(R.id.tdo_new_todo_fab)

    val TAG = "TodoLocal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_panel)

        //addRibbonBtn.setOnClickListener { this.addNewRibbon() }

    }

    fun addNewRibbon() {
        Log.i(TAG, "Add Ribbon!!")
    }
}