package com.zybooks.schednet.Model

//FOR CREATE AND UPDATE EVENT MENU
class CalModelFull {

    var CalId: Int //TASK BY USER ID WILL BE HANDLED BY DB HANDLER
    var CalEventName: String
    var CalEventDescription: String
    var CalEventStart: Int //DATETIME IN ONE STRING <<DATETIME IS DATE AND TIME TOGETHER>>
    var CalEventEnd: Int
    var CalIsRepeat: Int
    var CalIsAllDay: Int
    var CalHasReminder: Int
    var CalRepeatType: Int //OPTIONS WILL DISPLAY IN SPINNER <<DAILY, WEEKLY, MONTHLY>>

    init { //DEFAULT VALUES IF NOT UPDATED OR DB PULL FAILS
        CalId = 0
        CalEventName = "COULD NOT BE LOADED"
        CalEventDescription = "COULD NOT BE LOADED"
        CalEventStart = 0
        CalEventEnd = 0
        CalIsRepeat = 0
        CalIsAllDay = 0
        CalHasReminder = 0
        CalRepeatType = 0
    } //NOTE: DONT DISPLAY START OR END IF NULL <<.isNullOrEmpty() exists>>

}
