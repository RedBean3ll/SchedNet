package com.zybooks.schednet

class TodoRibbonObj {
    var Tdo_RibbonId = "0"
        get() = field
        set(value) {
            field = value
        }

    var Tdo_Name = "todo_nam"
        get() = field
        set(value) {
            field = value
        }

    var Tdo_Description = "empty"
        get() = field
        set(value) {
            field = value
        }

    var Tdo_isAllDay = false
        get() = field
        set(value) {
            field = value
        }

    var Tdo_DateA = "datestr:timestr"
        get() = field
        set(value) {
            field = value
        }

    var Tdo_DateB = "datestr:timestr"
        get() = field
        set(value) {
            field = value
        }

    var Tdo_isRepeating = false
        get() = field
        set(value) {
            field = value
        }

    var Tdo_isRemind = false
        get() = field
        set(value) {
            field = value
        }

    var Tdo_isRepeatType = "type"
        get() = field
        set(value) {
            field = value
        }

    var Tdo_numRemind = 0
        get() = field
        set(value) {
            field = value
        }

    //Struct ==> "datestr:timestr"
    var Tdo_listRepeatRemindTime: List<String> = listOf("datestr:timestr", "startReminds") //Index 0 = repeat elem, Index 1+ = remind elem
        get() = field
        set(value) {
            field = value
        }
}