package com.example.cs4530_mobileapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UserViewModel (application: Application?) : AndroidViewModel(application!!) {
        private val userData = MutableLiveData<UserData>()

    fun setInfo(firstName: String, lastName: String,age: Int, height: Int, weight: Int, sex: Int, actLvl: Int) {
        userData.value?.firstName = firstName
        userData.value?.lastName = lastName
        userData.value?.age = age
        userData.value?.height = height
        userData.value?.weight = weight
        userData.value?.sex = sex
        userData.value?.activityLvl = actLvl
        userData.value?.calcBMI()
        userData.value?.calcCals()
    }

    fun setName(firstName: String, lastName: String){
        userData.value?.firstName = firstName
        userData.value?.lastName = lastName
    }

    fun setAge(age: Int){
        userData.value?.age = age
    }

    fun setHeight(height: Int){
        userData.value?.height = height
    }

    fun setWeight(weight: Int){
        userData.value?.weight = weight
    }

    fun setSex(sex: Int){
        userData.value?.sex = sex
    }

    fun setActLvl(actLvl: Int){
        userData.value?.activityLvl = actLvl
    }

    fun calcBmi(){
        userData.value?.calcBMI()
    }

    fun calcCals(){
        userData.value?.calcCals()
    }

    val data: LiveData<UserData>
    get() = userData

}
