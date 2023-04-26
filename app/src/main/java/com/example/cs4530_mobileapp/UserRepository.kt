package com.example.cs4530_mobileapp

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.jvm.Synchronized

class UserRepository private constructor(userDao: UserDao) {
    // This LiveData object that is notified when we've fetched the weather
    val data = MutableLiveData<UserData>()
    private var mUserDao: UserDao = userDao

    private var mUid: Long? = null
    private var mfirstName = null
    private var mLastName = null
    private var mAge = null
    private var mHeight = null
    private var mWeight = null
    private var mbMI = null
    private var mDailyCalories = null
    private var mSex = null
    private var mActivityLvl = null

    fun setData(userdata: UserData) {
        userdata.calcBMI()
        userdata.calcCals()
        mScope.launch {
            data.postValue(userdata)
            insert()
        }
    }
    @WorkerThread
    suspend fun insert() {
        if (mUid != null) {
            mUserDao.insert(UserTable(mUid!!, mfirstName!!, mLastName!!, mAge!!, mHeight!!, mWeight!!, mbMI!!, mDailyCalories!!, mSex!!, mActivityLvl!!))
        }
    }
    // Make the repository singleton. Could in theory
    // make this an object class, but the companion object approach
    // is nicer (imo)
    companion object {
        private var mInstance: UserRepository? = null
        private lateinit var mScope: CoroutineScope
        @Synchronized
        fun getInstance(userDao: UserDao,
                        scope: CoroutineScope
        ): UserRepository {
            mScope = scope
            return mInstance?: synchronized(this){
                val instance = UserRepository(userDao)
                mInstance = instance
                instance
            }
        }
    }
}