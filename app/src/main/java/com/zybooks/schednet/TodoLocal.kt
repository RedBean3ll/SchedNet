package com.zybooks.schednet

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zybooks.schednet.Adapter.TodoAdapter
import com.zybooks.schednet.Model.TodoModel

class TodoLocal : AppCompatActivity() {

    //private var mLayoutMgr: RecyclerView.LayoutManager? = null
    //private var mRecAdaper: RecyclerView.Adapter<TodoAdapter.ViewHolder>? = null
    private var mRecycleViewMe: RecyclerView? = null
    private var mAdapt: TodoAdapter? = null
    private var addRibbonBtn: FloatingActionButton? = null
    private var mTdoList: MutableList<TodoModel>? = null

    //DEBUG
    val TAG = "TodoLocal"
    var CNT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_panel)

        mRecycleViewMe = findViewById(R.id.tdo_task_space)
        mAdapt = TodoAdapter(this)
        mRecycleViewMe!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mTdoList = mutableListOf()
        //var task: TodoModel = TodoModel()
        //task.mId = 1
        //task.mStatus = 1
        //task.mTaskNam = "Tdo Task #N"
        //mTdoList!!.add(task)

        mAdapt!!.setTasks(mTdoList!!)
        mRecycleViewMe!!.adapter = mAdapt

        addRibbonBtn = findViewById(R.id.tdo_new_todo_fab)
        addRibbonBtn?.setOnClickListener() { v -> addNewRibbon() }

    }

    fun addNewRibbon() {
        Log.i(TAG, "Add Ribbon!!")
        var blank: TodoModel = TodoModel()
        blank.mId = CNT
        blank.mStatus = 0
        blank.mTaskNam = "TEST NAME"
        mTdoList!!.add(blank)
        mAdapt!!.setTasks(mTdoList!!)
        mRecycleViewMe!!.adapter = mAdapt

    }
}