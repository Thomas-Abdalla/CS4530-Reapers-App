package com.example.cs4530_mobileapp

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

// This does not need to be its own class. You could directly annotate the WeatherData class
// But since our Weather class is messy, it makes sense to strip out the parts
// that we want stored in the db (just for testing and maintainability).
// This does introduce the issue of drift between this class and the WeatherData class
@Entity(tableName = "user_table")
data class UserTable(
    @field:ColumnInfo(name = "userdata")
    @field:PrimaryKey(autoGenerate = true)
    var uid: Long = 0,
    @field:ColumnInfo(
        name = "first_name"
    ) val firstName: String?,
    @field:ColumnInfo(
        name = "last_name"
    ) val lastName: String?,
    @field:ColumnInfo(
        name = "age"
    ) val age: Int?,
    @field:ColumnInfo(
        name = "height"
    ) val height: Int?,
    @field:ColumnInfo(
        name = "weight"
    ) val weight: Int?,
    @field:ColumnInfo(
        name = "bMI"
    ) val bMI: Float?,
    @field:ColumnInfo(
        name = "dailyCalories"
    ) val dailyCalories: Int?,
    @field:ColumnInfo(
        name = "sex"
    ) val sex: Int?,
    @field:ColumnInfo(
        name = "activityLvl"
    ) val activityLvl: Int?
)