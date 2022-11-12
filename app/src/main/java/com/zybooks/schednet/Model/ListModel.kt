package com.zybooks.schednet.Model

class ListModel {
    var ListId: Int;
    var ListName: String;
    var isPinned: Boolean;

    init {
        ListId = 0
        ListName = "NAN"
        isPinned = false
    }

    override fun toString(): String {
        return ListId.toString()+ListName+isPinned
    }
}