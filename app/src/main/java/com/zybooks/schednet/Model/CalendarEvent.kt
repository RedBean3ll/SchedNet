package com.zybooks.schednet.Model

import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class CalendarEvent(@Nullable idInput: Int, @Nullable nameInput: String?, @Nullable dateInput: LocalDate?, @Nullable timeInput: LocalTime?) {
    /*companion object {
        var eventList: ArrayList<Event> = ArrayList()

        fun suitEventList(date: LocalDate) {
            val events: ArrayList<Event> = ArrayList()
            for(event: Event in events) {
                if(event.date == date) events.add(event)
            }
        }
    }*/

    var id: Int
    var name: String
    var date: LocalDate
    var time: LocalTime

    init {

        id = idInput ?: -1
        name = nameInput ?: "NAN"
        date = dateInput ?: LocalDate.of(1500, 1, 1)
        time = timeInput ?: LocalTime.now()
    }
}