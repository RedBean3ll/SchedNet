package com.zybooks.schednet.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StateViewModel: ViewModel() {
    var PageState = MutableLiveData<Boolean>()
    var AccessState = MutableLiveData<Boolean>()
}