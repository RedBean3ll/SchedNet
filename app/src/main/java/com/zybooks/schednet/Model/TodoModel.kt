package com.zybooks.schednet.Model

//FOR RIBBON
class TodoModel {

    var TodoId: Int? = null
    var TodoStatus: Int? = null
    var TodoName: String? = null
    var TodoPriorityStatus: Int? = null
    var TodoCreateStamp: String? = null
    var TodoStatusTimestamp: String? = null

    init {
        TodoId = 0
        TodoStatus = 1
        TodoName = "NOT UPDATED: POTENTIAL ERROR"
        TodoPriorityStatus = 0
        TodoCreateStamp = "NA"
        TodoStatusTimestamp = "NA"
    }

}
