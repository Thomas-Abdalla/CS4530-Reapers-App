package com.example.cs4530_mobileapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UserViewModel (application: Application?) : AndroidViewModel(application!!) {
        private val userData = MutableLiveData<UserData>()

        fun setInfo(name: String, age: Int, height: Int, weight: Int, sex: Int, actLvl: Int) {
            userData.value?.fullName = name
            userData.value?.age = age
            userData.value?.height = height
            userData.value?.weight = weight
            userData.value?.sex = sex
            userData.value?.activityLvl = actLvl
            userData.value?.calcBMI()
            userData.value?.calcCals()

        }

        val data: LiveData<UserData>
            get() = userData

}
