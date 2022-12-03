package com.zybooks.schednet.model

import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 *  File: ListObject.kt
 *  @author Matthew Clark
 */

class ListObject(@NonNull idInput: Int, @Nullable listNameInput: String?, @Nullable pinStatusInput: Boolean?) {
    var listId: Int
    var listName: String
    var isPinned: Boolean

    init {
        listId = idInput
        listName = listNameInput ?: "NAN"
        isPinned = pinStatusInput ?: false
    }

    override fun toString(): String {
        return "$listId:$listName:$isPinned"
    }
}