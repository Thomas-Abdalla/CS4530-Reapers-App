package com.example.cs4530_mobileapp

import androidx.room.*


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserTable)

    @Query("SELECT * FROM user_table")
    fun getAll(): List<UserData>

    @Query("SELECT * FROM user_table WHERE first_name == :firstName AND last_name == :lastName")
    fun findByName(firstName: String, lastName: String): UserData

    @Query("DELETE FROM user_table")
    fun deleteAll()
}
