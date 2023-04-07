package com.example.cs4530_mobileapp

class UserData {
    var fullName: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var age: Int? = null
    var height: Int? = null
    var weight: Int? = null
    var bMI: Float? = 0.0f
    var dailyCalories: Int? = 0
    var sex: Int? = null          // 0 = male; 1 = female;
    var activityLvl: Int? = null  // 0 = sedentary; 1 = moderate; 2 = very active;

    fun calcBMI(){
        if(weight != null && height != null && age != null) {
            when(sex) {
                0 -> { bMI = (10 * weight!!.toFloat() + (height!!.toFloat() * 6.25f) - 5* age!!.toFloat() + 5.0f) }
                1 -> { (10 * weight!!.toFloat() + (height!!.toFloat() * 6.25f) - 5* age!!.toFloat() - 161.0f) }
                else -> { bMI = null}
            }
        }
    }

    fun calcCals(){
        if (bMI != null)
        //Harris Benedict Equation
        when(activityLvl) {
            0 -> { dailyCalories = (bMI!! * 1.2f).toInt() }
            1 -> { dailyCalories = (bMI!! * 1.55f).toInt() }
            2 -> { dailyCalories = (bMI!! * 1.725f).toInt() }
        }
        else
            dailyCalories = null
    }

}
