package com.zybooks.schednet.Model

//FOR RIBBON

class TodoModel {
    var TodoId: Int
    var TodoStatus: Boolean
    var TodoName: String
    var TodoDescription: String
    var TodoPriorityStatus: Boolean
    var TodoCreateStamp: Long
    var TodoStatusTimestamp: String

    init {
        TodoId = 0
        TodoStatus = false
        TodoName = "NAN"
        TodoDescription = "NAN"
        TodoPriorityStatus = false
        TodoCreateStamp = System.currentTimeMillis()
        TodoStatusTimestamp = "NAN"
    }

    override fun toString(): String {
        return TodoId.toString()+":"+TodoStatus+":"+TodoName+":"+TodoDescription+":"+TodoPriorityStatus+":"+TodoCreateStamp+":"+TodoStatusTimestamp
    }

    fun updateCreateStamp() {
        TodoCreateStamp = System.currentTimeMillis()
    }

}
