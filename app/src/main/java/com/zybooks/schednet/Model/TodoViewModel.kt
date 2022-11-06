package com.zybooks.schednet.Model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoViewModel: ViewModel() {
    var TodoTitle = MutableLiveData<String>()
    var TodoDescription = MutableLiveData<String>()
    var TodoId = MutableLiveData<Int>()
    var TodoStatus = MutableLiveData<Int>()
    var TodoPriorityStatus = MutableLiveData<Boolean>()
    var TodoCreateStamp = MutableLiveData<Long>()
    var TodoStatusTimestamp = MutableLiveData<String>()
}