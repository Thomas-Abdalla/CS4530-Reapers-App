package com.example.cs4530_mobileapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

class HomePageFragment : Fragment(), View.OnClickListener {
    //import parent VM
    private val mUserViewModel: UserViewModel by activityViewModels()

    //initialize UI variables
    private var mButtonProfile: Button? = null
    private var mButtonHikes: Button? = null
    private var mButtonWeather: Button? = null
    private var mTVBMI: TextView? = null
    private var mTVCalorie: TextView? = null

    //fun fragment function
    private var mDataPasser: DataPassingInterface? = null
    interface DataPassingInterface {
        fun passData(data: Array<String?>?)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        //Set VM Observer
        mUserViewModel.data.observe(this, userObserver)

        //link .kt to .xml
        mButtonProfile = view?.findViewById(R.id.button_profile) as Button?
        mButtonWeather = view?.findViewById(R.id.button_weather) as Button?
        mButtonHikes = view?.findViewById(R.id.button_hikes) as Button?
        mTVBMI = view?.findViewById(R.id.tv_bmi_value) as TextView?
        mTVCalorie = view?.findViewById(R.id.tv_calorie_value) as TextView?

        //link button to onClick
        mButtonProfile!!.setOnClickListener(this)
        mButtonHikes!!.setOnClickListener(this)
        mButtonWeather!!.setOnClickListener(this)

        return view
    }

    //prepping for sending to activity
    override fun onAttach(context: Context){
        super.onAttach(context)
        mDataPasser = try {
            context as DataPassingInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement UserInfoFragment.DataPassingInterface")
        }
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.button_profile ->{
                val buttonClicked: Array<String?>?
                buttonClicked = arrayOf("frag change", "list")
                mDataPasser!!.passData(buttonClicked)
            }
            R.id.button_hikes ->{
                mDataPasser!!.passData(arrayOf("frag change", "hikes"))
                }

            R.id.button_weather ->{
                mDataPasser!!.passData(arrayOf("frag change", "weather"))
            }
        }
    }

    private val userObserver: Observer<UserData> = Observer {
        userData ->
            userData.calcCals()
            userData.calcBMI()
            mTVBMI?.text = userData.bMI.toString()
            mTVCalorie?.text = userData.dailyCalories.toString()
    }
}