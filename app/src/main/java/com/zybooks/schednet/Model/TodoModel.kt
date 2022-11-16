package com.zybooks.schednet.Model

//FOR RIBBON

class TodoModel {
    var TodoId: Int
    var TodoName: String
    var TodoDescription: String
    var TodoSelected: Boolean
    var TodoPinned: Boolean
    var TodoTimestamp: Long

    init {
        TodoId = 0
        TodoPinned = false
        TodoSelected = false
        TodoName = "NAN"
        TodoDescription = ""
        TodoTimestamp = System.currentTimeMillis()
    }

    override fun toString(): String {
        return TodoId.toString()+":"+TodoPinned+":"+TodoName+":"+TodoDescription+":"+":"+":"+TodoTimestamp
    }

    fun updateCreateStamp() {
        TodoTimestamp = System.currentTimeMillis()
    }

}
