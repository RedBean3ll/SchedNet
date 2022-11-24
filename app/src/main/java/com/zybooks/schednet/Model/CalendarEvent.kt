package com.zybooks.schednet.Model

import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import java.time.*

class CalendarEvent(@Nullable idInput: Int, @Nullable nameInput: String?, @Nullable descriptionInput: String?, @Nullable timestamp: Long?) {
    /*companion object {
        var eventList: ArrayList<Event> = ArrayList()

        fun suitEventList(date: LocalDate) {
            val events: ArrayList<Event> = ArrayList()
            for(event: Event in events) {
                if(event.date == date) events.add(event)
            }
        }
    }*/

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