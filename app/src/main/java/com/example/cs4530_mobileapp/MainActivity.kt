package com.example.cs4530_mobileapp

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.os.Build
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import java.io.OutputStream
import kotlin.math.pow

//Implement View.onClickListener to listen to button clicks. This means we have to override onClick().
class MainActivity : AppCompatActivity(), View.OnClickListener {
    //Create variables to hold the three strings
    private var mFullName: String? = null
    private var mFirstName: String? = null
    private var mLastName: String? = null
    private var mAge: Int? = null
    private var mHeight: String? = null
    private var mWeight: Int? = null
    private var mBMI: Float? = 0.0f
    private var mDailyCalories: Int? = 0
    //Create variables for the UI elements that we need to control
    private var mTvFirstName: TextView? = null
    private var mTvLastName: TextView? = null
    private var mButtonSubmit: Button? = null
    private var mButtonCamera: Button? = null
    private var mButtonHome: Button? = null
    private var mEtFullName: EditText? = null
    private var mEtAge: EditText? = null
    private var mEtHeight: EditText? = null
    private var mEtWeight: EditText? = null
    //Create the variable for the ImageView that holds the profile pic
    private var mIvPic: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get the text views where we will display names
        mTvFirstName = findViewById(R.id.tv_fn_data)
        mTvLastName = findViewById(R.id.tv_ln_data)

        //Get the buttons
        mButtonSubmit = findViewById(R.id.button_submit)
        mButtonCamera = findViewById(R.id.button_pic)
        mButtonHome = findViewById(R.id.button_home)

        //Say that this class itself contains the listener.
        mButtonSubmit!!.setOnClickListener(this)
        mButtonCamera!!.setOnClickListener(this)
        mButtonHome!!.setOnClickListener(this)
    }

    //Handle clicks for ALL buttons here
    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {

                //First, get the names from the name EditText
                mEtFullName = findViewById(R.id.et_name)
                mFullName = mEtFullName!!.text.toString()

                //Check if the EditText string is empty
                if (mFullName.isNullOrBlank()) {
                    //Complain that there's no text
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //Reward them for submitting their names
                    Toast.makeText(
                        this@MainActivity,
                        "Welcome!",
                        Toast.LENGTH_SHORT
                    ).show()

                    //Remove any leading spaces or tabs
                    mFullName = mFullName!!.replace("^\\s+".toRegex(), "")

                    //Separate the string into first and last name using simple Java stuff
                    val splitStrings = mFullName!!.split("\\s+".toRegex()).toTypedArray()
                    when (splitStrings.size) {
                        1 -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Enter both first and last name, separated by a space",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        2 -> {
                            mFirstName = splitStrings[0]
                            mLastName = splitStrings[1]

                            //Set the text views
                            mTvFirstName!!.text = mFirstName
                            mTvLastName!!.text = mLastName
                        }
                        else -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Enter only first and last name!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                //Next get the age from the age EditTexts
                mEtAge = findViewById(R.id.et_age)
                mAge = mEtAge!!.text.toString().toIntOrNull()
                if (mAge == null){ //throw warning if incorrect data
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //Next get the height from the height EditTexts
                mEtHeight = findViewById(R.id.et_height)
                mHeight = mEtHeight!!.text.toString()
                val splitByValues = Array(2){i->i.toString()}
                if (mHeight.isNullOrBlank()) { //throw warning if no data
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //Remove any leading spaces or tabs
                    mHeight = mHeight!!.replace("^\\s+".toRegex(), "")
                    val splitStrings = mHeight!!.split("\\s+".toRegex()).toTypedArray()
                    if(splitStrings.size == 1) {
                        mHeight = splitStrings[0]
                        val splitByPieces = mHeight!!.split("'".toRegex()).toTypedArray()
                        splitByPieces[1] = splitByPieces[1].replace("\"","")
                        splitByValues[0] = splitByPieces[0]
                        splitByValues[1] = splitByPieces[1]
                    }
                    else //throw warning if improperly formatted
                        Toast.makeText(
                            this@MainActivity,
                            "Please enter height in proper format: *Feet*'*inches*\"",
                            Toast.LENGTH_SHORT
                        ).show()
                }
                //Next get the weight from the weight EditTexts
                mEtWeight = findViewById(R.id.et_weight)
                mWeight = mEtWeight!!.text.toString().toIntOrNull()
                if (mWeight == null) { //throw warning if bad data
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                mBMI = (703 * mWeight!!.toFloat()/(splitByValues[0].toInt().toFloat() * 12f + splitByValues[1].toInt().toFloat()).pow(2))
                mDailyCalories = mBMI!!.toInt()
            }

            R.id.button_home ->{
                //send intent for home page
                val homeActivityIntent = Intent(this, HomePageActivity::class.java)
                val intentBundle = Bundle()
                intentBundle.putFloat("BMI", mBMI!!)
                intentBundle.putInt("Calories", mDailyCalories!!)
                startActivity(homeActivityIntent)
            }

            R.id.button_pic -> {
                //The button press should open a camera
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try{
                    cameraActivity.launch(cameraIntent)
                }catch(ex:ActivityNotFoundException){
                    //Do error handling here
                }
            }
            //R.id.
        }
    }
    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK) {
            mIvPic = findViewById<View>(R.id.iv_pic) as ImageView
            //val extras = result.data!!.extras
            //val thumbnailImage = extras!!["data"] as Bitmap?

                val thumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
                mIvPic!!.setImageBitmap(thumbnailImage)


        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        if(mFullName != null)
         outState.putString("FullName",mFullName)
        if(mFirstName!=null)
         outState.putString("FirstName",mFirstName)
        if(mLastName!=null)
        outState.putString("LastName",mLastName)
        if(mAge != null)
        outState.putInt("Age", mAge!!)
        if(mHeight != null)
        outState.putString("Height",mHeight)
        if(mWeight != null)
        outState.putInt("Weight",mWeight!!)
        if(mBMI != null)
        outState.putFloat("BMI",mBMI!!)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState.get("FullName") != null)
            mFullName = savedInstanceState.get("FullName").toString()
        if(savedInstanceState.get("FirstName") != null)
            mFirstName = savedInstanceState.get("FirstName").toString()
        if(savedInstanceState.get("LastName") != null)
            mLastName = savedInstanceState.get("LastName").toString()
        if(savedInstanceState.get("Height") != null)
            mHeight = savedInstanceState.get("Height").toString()
        if(savedInstanceState.get("Age") != null)
            mAge = savedInstanceState.get("Age").toString().toInt()
        if(savedInstanceState.get("Weight") != null)
            mWeight = savedInstanceState.get("Weight").toString().toInt()
        if(savedInstanceState.get("BMI") != null)
            mBMI = savedInstanceState.get("BMI").toString().toFloat()
    }

    override fun onStop() {
        super.onStop()
        val filename:String?
        var fileContents:String?
        if(mFullName != null) {
            filename = mFullName!!
            fileContents = ""
            fileContents += mFullName!!.toString() + "\n"
            fileContents += mFirstName!!.toString() + "\n"
            fileContents += mLastName!!.toString() + "\n"
            fileContents += mAge!!.toString() + "\n"
            fileContents += mHeight!!.toString() + "\n"
            fileContents += mWeight!!.toString() + "\n"
            fileContents += mBMI!!.toString() + "\n"
            try {
                val output = openFileOutput(filename, MODE_PRIVATE)
                output.write(fileContents.toByteArray())
                output.close()
            } catch (e : Exception)
            {
                e.printStackTrace()
            }
        }

    }
}