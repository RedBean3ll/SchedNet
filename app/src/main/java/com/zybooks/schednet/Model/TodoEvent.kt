package com.zybooks.schednet.Model

class TodoEvent {
    var eventId: Int = 0
    var eventName: String = "NAN"
    var eventDescription: String = ""
    var isSelected: Boolean = false
    var isPinned: Boolean = false
    var eventTimestamp: Long = System.currentTimeMillis()

    override fun toString(): String {
        return "$eventId:$isPinned:$eventName:$eventDescription:$eventTimestamp"
    }

}
