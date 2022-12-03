package com.zybooks.schednet.model

import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 *  File: TodoObject.kt
 *  @author Matthew Clark
 */

class TodoObject(@NonNull eventId: Int, @Nullable eventName: String?, @Nullable eventDescription: String?, @Nullable isChecked: Boolean?, @Nullable isPinned: Boolean?, @Nullable timestamp: Long?) {
    var eventId: Int
    var eventName: String
    var eventDescription: String
    var isChecked: Boolean
    var isPinned: Boolean
    var eventTimestamp: Long

    init {
        this.eventId = eventId
        this.eventName = eventName ?: "NAN"
        this.eventDescription = eventDescription ?: ""
        this.isChecked = isChecked ?: false
        this.isPinned = isPinned ?: false
        this.eventTimestamp = timestamp ?: System.currentTimeMillis()
    }

    override fun toString(): String {
        return "$eventId:$isPinned:$isChecked:$eventName:$eventDescription:$eventTimestamp"
    }
}
