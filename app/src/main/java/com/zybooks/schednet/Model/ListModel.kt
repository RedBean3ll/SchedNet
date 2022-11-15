package com.zybooks.schednet.Model

class ListModel {
    var ListId: Int
    var ListName: String
    var isPinned: Boolean
    var timeStamp: Long

    init {
        ListId = 0
        ListName = "NAN"
        isPinned = false
        timeStamp = System.currentTimeMillis()
    }

    override fun toString(): String {
        return ListId.toString()+ListName+isPinned+timeStamp
    }
}