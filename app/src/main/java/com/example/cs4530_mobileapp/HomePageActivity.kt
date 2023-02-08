package com.example.cs4530_mobileapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomePageActivity : AppCompatActivity(), View.OnClickListener {
    //initialize members
    private var mFirstName: String? = null
    private var mLastName: String? = null
    private var mAge: Int? = null
    private var mHeight: Int? = null
    private var mWeight: Int? = null
    private var mSex: Boolean? = null
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