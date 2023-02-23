package com.example.cs4530_mobileapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomePageFragment : Fragment(), View.OnClickListener {
    //initialize members
    private var mBMI: Float? = null
    private var mDailyCalories: Int? = null

    //initialize UI variables
    private var mButtonProfile: Button? = null
    private var mTVBMI: TextView? = null
    private var mTVCalorie: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        //receive arguments bundle
        val inBundle = arguments
        mBMI = inBundle!!.getFloat("BMI")
        mDailyCalories = inBundle.getInt("Calories")
        //link .kt to .xml
        mButtonProfile = view?.findViewById(R.id.button_profile) as Button?
        mTVBMI = view?.findViewById(R.id.tv_bmi_value) as TextView?
        mTVCalorie = view?.findViewById(R.id.tv_calorie_value) as TextView?
        //link button to onClick
        mButtonProfile!!.setOnClickListener(this)

        mTVBMI!!.text = mBMI.toString()
        mTVCalorie!!.text = mDailyCalories.toString()

        return view
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.button_profile ->{
                //TODO--> Set up sending intent data to Activity
                //send intent for MainActivity (user info page)
                //val mainActivityIntent = Intent(this, MainActivity::class.java)
                //startActivity(mainActivityIntent)
            }
        }
    }
}