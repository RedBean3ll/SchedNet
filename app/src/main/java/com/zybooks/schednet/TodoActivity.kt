package com.zybooks.schednet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zybooks.schednet.Adapter.TodoAdapter
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.Model.TodoViewModel
import com.zybooks.schednet.databinding.ActivityTodoPageBinding

class TodoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoPageBinding
    private lateinit var todoViewModel: TodoViewModel

    private lateinit var mList: MutableList<TodoModel>
    private lateinit var mAdapter: TodoAdapter
    //DEBUG
    val TAG = "TodoLocal"
    var CNT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        binding.tdoNewTodoFab.setOnClickListener {
            AddNewTodo().show(supportFragmentManager, "newTaskTag")
        }

        mList = mutableListOf()
        mList.add(TodoModel())

        //DATABASE PULL
        //SORT
        mAdapter = TodoAdapter(mList) //
        binding.tdoTaskSpace.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.tdoTaskSpace.adapter = mAdapter


        var shell = TodoModel()
        todoViewModel.TodoTitle.observe(this) {
            shell = TodoModel()
            shell.TodoName = String.format(it)
        }
        todoViewModel.TodoDescription.observe(this) {
            shell.TodoDescription = String.format(it)
            mList.add(shell)
            updater()
            //mAdapt!!.setTasks(mTdoList)
            //mRecycleViewMe!!.adapter = mAdapt
        }




        //var task: TodoModel = TodoModel()
        //task.TodoId = 1
        //task.TodoStatus = 1
        //task.TodoName = "Tdo Task #N"
        //mTdoList!!.add(task)

        //mAdapt!!.setTasks(mTdoList!!)
        //mRecycleViewMe!!.adapter = mAdapt

        //addRibbonBtn = findViewById(R.id.tdo_new_todo_fab)
        //addRibbonBtn?.setOnClickListener { v -> addNewRibbon() }

    }


    fun updater() {
        mAdapter.setTasks(mList)

    }

    override fun onBackPressed() {
        //nothing
    }


}