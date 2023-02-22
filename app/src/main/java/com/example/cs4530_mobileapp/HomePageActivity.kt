package com.example.cs4530_mobileapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class HomePageActivity : AppCompatActivity(), View.OnClickListener {
    //initialize members
    private var mBMI: Float? = null
    private var mDailyCalories: Int? = null
    private var mActivityLvl: Int? = null

    //initialize UI variables
    private var mButtonProfile: Button? = null
    private var mTVBMI: TextView? = null
    private var mTVCalorie: TextView? = null

    //override onCreate
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        //link .kt to .xml
        mButtonProfile = findViewById(R.id.button_profile)
        mTVBMI = findViewById(R.id.tv_bmi_value)
        mTVCalorie = findViewById(R.id.tv_calorie_value)

        //link button to onClick
        mButtonProfile!!.setOnClickListener(this)

        //receive intent bundle
        val receivedIntent = intent
        mBMI = receivedIntent.getFloatExtra("BMI",0.0f)
        mDailyCalories = receivedIntent.getIntExtra("Calories",0)

        mTVBMI!!.text = mBMI.toString()
        mTVCalorie!!.text = mDailyCalories.toString()
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.button_profile ->{
                //send intent for MainActivity (user info page)
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(mainActivityIntent)
            }
        }
    }
}

