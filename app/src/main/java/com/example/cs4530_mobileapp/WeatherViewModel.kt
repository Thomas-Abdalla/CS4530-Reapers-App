package com.example.cs4530_mobileapp

import android.app.Application
import android.os.Looper
import androidx.core.os.HandlerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cs4530_mobileapp.JSONWeatherUtils.getWeatherData
import com.example.cs4530_mobileapp.NetworkUtils.buildURLFromString
import com.example.cs4530_mobileapp.NetworkUtils.getDataFromURL
import java.util.concurrent.Executors

class WeatherViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val jsonData = MutableLiveData<WeatherData>()
    private var mLocation: String? = null

    fun setLocation(location: String?){
        mLocation = location
        loadData()
    }

    val data: LiveData<WeatherData>
        get() = jsonData

    private fun loadData(){
        if (mLocation != null)
            LoadTask().execute(mLocation)
    }

    private inner class LoadTask { //TODO -> Replace BG Thread with Coroutine
        val executorService = Executors.newSingleThreadExecutor()!!
        val mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())

        fun execute(location: String?){
            executorService.execute{
                var jsonWeatherData: String?
                val weatherDataURL = buildURLFromString(location!!)
                jsonWeatherData = null
                try{
                    jsonWeatherData = getDataFromURL(weatherDataURL!!)
                    jsonWeatherData?.let { postToMainThread(it) }
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        private fun postToMainThread(retrievedJsonData: String){
            mainThreadHandler.post {
                try{
                    jsonData.setValue(getWeatherData(retrievedJsonData))
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}