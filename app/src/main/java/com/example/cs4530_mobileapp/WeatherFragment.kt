package com.example.cs4530_mobileapp

import android.content.Context
import com.example.cs4530_mobileapp.NetworkUtils.buildURLFromString
import com.example.cs4530_mobileapp.NetworkUtils.getDataFromURL
import android.widget.EditText
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.core.os.HandlerCompat
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import android.app.Application
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils
import org.json.JSONException
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class WeatherFragment : Fragment(), View.OnClickListener {
    private var mEtLocation: EditText? = null
    private var mTvTemp: TextView? = null
    private var mTvPress: TextView? = null
    private var mTvHum: TextView? = null
    private var mWeatherData: WeatherData? = null
    private var mBtSubmit: Button? = null
    private var mButtonHome: Button? = null
    private var mRecyclerView: RecyclerView? = null
    var mDataPasser: DataPassingInterface? = null

    interface DataPassingInterface {
        fun passData(data: Array<String?>?)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDataPasser = try {
            context as WeatherFragment.DataPassingInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement UserInfoFragment.DataPassingInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        //Get the edit text and all the text views
        mEtLocation = view.findViewById<View>(R.id.et_location) as EditText
        mTvTemp = view.findViewById<View>(R.id.tv_temp) as TextView
        mTvPress = view.findViewById<View>(R.id.tv_pressure) as TextView
        mTvHum = view.findViewById<View>(R.id.tv_humidity) as TextView
        mButtonHome = view?.findViewById(R.id.button_home) as Button?
        if (savedInstanceState != null) {
            val temp = savedInstanceState.getString("tvTemp")
            val hum = savedInstanceState.getString("tvHum")
            val press = savedInstanceState.getString("tvPress")
            if (temp != null) mTvTemp!!.text = "" + temp
            if (hum != null) mTvHum!!.text = "" + hum
            if (press != null) mTvPress!!.text = "" + press
        }

        mFetchWeatherTask.setWeakReference(this) //make sure we're always pointing to current version of fragment
        mBtSubmit = view.findViewById<View>(R.id.button_submit) as Button
        mBtSubmit!!.setOnClickListener(this)
        mButtonHome!!.setOnClickListener(this)
        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {

                //Get the string from the edit text and sanitize the input
                val inputFromEt = mEtLocation!!.text.toString().replace(' ', '&')
                loadWeatherData(inputFromEt)
            }
            R.id.button_home -> {

                //Get the string from the edit text and sanitize the input
                var dataToPass: Array<String?>?  = arrayOf("frag change", "list")
                mDataPasser!!.passData(dataToPass)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("tvTemp", mTvTemp!!.text.toString())
        outState.putString("tvHum", mTvHum!!.text.toString())
        outState.putString("tvPress", mTvPress!!.text.toString())
    }

    private fun loadWeatherData(location: String) {
        mFetchWeatherTask.execute(location)
    }

    private class FetchWeatherTask {
        var weatherFragmentWeakReference: WeakReference<WeatherFragment>? = null
        private val executorService = Executors.newSingleThreadExecutor()
        private val mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
        fun setWeakReference(ref: WeatherFragment) {
            weatherFragmentWeakReference = WeakReference(ref)
        }

        fun execute(location: String?) {
            executorService.execute {
                val jsonWeatherData: String?
                val weatherDataURL = buildURLFromString(location!!)
                try {
                    jsonWeatherData = getDataFromURL(weatherDataURL!!)
                    postToMainThread(jsonWeatherData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun postToMainThread(jsonWeatherData: String?) {
            val localRef = weatherFragmentWeakReference!!.get()
            mainThreadHandler.post {
                if (jsonWeatherData != null) {
                    try {
                        localRef!!.mWeatherData = JSONWeatherUtils.getWeatherData(jsonWeatherData)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (localRef!!.mWeatherData != null) {
                        localRef.mTvTemp!!.text =
                            "" + Math.round(localRef.mWeatherData!!.temperature.temp - 273.15) + " C"
                        localRef.mTvHum!!.text =
                            "" + localRef.mWeatherData!!.currentCondition.humidity + "%"
                        localRef.mTvPress!!.text =
                            "" + localRef.mWeatherData!!.currentCondition.pressure + " hPa"
                    }
                }
            }
        }
    }

    companion object {
        private val mFetchWeatherTask = FetchWeatherTask()
    }
    private val liveDataObserver: Observer<WeatherData> =
        Observer { weatherData -> // Update the UI if this data variable changes
            if (weatherData != null) {
                mTvTemp!!.text = "" + (weatherData.temperature.temp - 273.15).roundToInt() + " C"
                mTvHum!!.text = "" + weatherData.currentCondition.humidity + "%"
                mTvPress!!.text = "" + weatherData.currentCondition.pressure + " hPa"
            }
        }

    // This observer is triggered when the Flow object in the repository
    // detects a change to the database (including at the start of the app)
    private val flowObserver: Observer<List<WeatherTable>> =
        Observer { weatherTableList ->
            if (weatherTableList != null) {
                // Pass the entire list to a RecyclerView
                mRecyclerView!!.adapter = WeatherRVAdapter(weatherTableList)
            }
        }
}