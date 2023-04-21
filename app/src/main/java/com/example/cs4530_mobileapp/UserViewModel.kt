package com.example.cs4530_mobileapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UserViewModel (application: Application?) : AndroidViewModel(application!!) {
    private var userData = MutableLiveData<UserData>()

    val data: LiveData<UserData>
        get() = userData

    fun setData(userData : UserData?){
        this.userData.value = userData
        this.userData.value!!.calcCals()
        this.userData.value!!.calcBMI()
    }
}
