package com.zybooks.schednet.Model

class TodoModel {

    var mId : Int? = null
        get() = field
        set(value) {
            field = value
        }

    var mStatus : Int? = null
        get() = field
        set(value) {
            field = value
        }

    var mTaskNam : String? = null
        get() = field
        set(value) {
            field = value
        }

    init {
        mId = 0
        mStatus = 1
        mTaskNam = "NA"
    }

}
