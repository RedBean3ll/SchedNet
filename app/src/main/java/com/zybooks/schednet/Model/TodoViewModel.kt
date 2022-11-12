package com.zybooks.schednet.Model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoViewModel: ViewModel() {
    var TodoTitle = MutableLiveData<String>()
    var TodoDescription = MutableLiveData<String>()
    var TodoStatus = MutableLiveData<Boolean>()

    fun reset() {
        TodoTitle.value = ""
        TodoDescription.value = ""
        TodoStatus.value = false
    }
}