package com.zybooks.schednet.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StateViewModelTodo: ViewModel() {
    var PageState = MutableLiveData<Boolean>()
}