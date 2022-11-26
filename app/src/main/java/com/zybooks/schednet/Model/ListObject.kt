package com.zybooks.schednet.Model

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class ListObject(@NonNull idInput: Int, @Nullable listNameInput: String?, @Nullable pinStatusInput: Boolean?, @Nullable timestampInput: Long?) {
    var listId: Int
    var listName: String
    var isPinned: Boolean
    var timestamp: Long

    init {
        listId = idInput
        listName = listNameInput ?: "NAN"
        isPinned = pinStatusInput ?: false
        timestamp = timestampInput ?: System.currentTimeMillis()
    }

    override fun toString(): String {
        return listId.toString()+listName+isPinned+timestamp
    }
}