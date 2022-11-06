package com.zybooks.schednet.Model

//FOR RIBBON
class CalModel {

    var CalId: Int //TASK BY USER ID WILL BE HANDLED BY DB HANDLER
    var CalEventName: String
    var CalEventStart : Int //DATETIME IN ONE STRING <<DATETIME IS DATE AND TIME TOGETHER>>
    var CalEventEnd : Int
    var CalIsRepeat : Int //MAYBE PLACE STAR AT END OF RIBBON IF REPEATING?? DELETE IF SCRAPPED..

    init { //DEFAULT VALUES IF NOT UPDATED OR DB PULL FAILS
        CalId = 0
        CalEventName = "COULD NOT BE LOADED"
        CalEventStart = 0
        CalEventEnd = 0
        CalIsRepeat = 0

    } //NOTE: DONT DISPLAY START OR END IF NULL <<.isNullOrEmpty() exists>>

}
