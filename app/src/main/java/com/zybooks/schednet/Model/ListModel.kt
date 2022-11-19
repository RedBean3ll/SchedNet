package com.zybooks.schednet.Model

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class ListModel(@NonNull id: Int, @Nullable name: String?, @Nullable toggle: Boolean?, @Nullable mili: Long?) {
    var ListId: Int
    var ListName: String
    var isPinned: Boolean
    var timeStamp: Long

    init {
        ListId = id
        ListName = name ?: "NAN"
        isPinned = toggle ?: false
        timeStamp = mili ?: System.currentTimeMillis()
    }

    override fun toString(): String {
        return ListId.toString()+ListName+isPinned+timeStamp
    }
}