package com.zybooks.schednet.Model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoViewModel: ViewModel() {
    var TodoTitle = MutableLiveData<String>()
    var TodoDescription = MutableLiveData<String>()
}