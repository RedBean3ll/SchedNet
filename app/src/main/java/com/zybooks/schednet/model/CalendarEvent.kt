package com.zybooks.schednet.model

import androidx.annotation.Nullable

/**
 *  File: CalendarEvent.kt
 *  @author Matthew Clark
 *  @author Amanda Thomas
 */

class CalendarEvent(@Nullable idInput: Int, @Nullable nameInput: String?, @Nullable descriptionInput: String?, @Nullable timestamp: Long?) {

    var eventId: Int
    var eventName: String
    var eventDescription: String
    var eventDateTime: Long

    init {
        eventId = idInput
        eventName = nameInput ?: "NAN"
        eventDescription = descriptionInput ?: ""
        eventDateTime = timestamp ?: 0
    }
}