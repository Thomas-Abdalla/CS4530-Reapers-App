package com.example.cs4530_mobileapp

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import kotlin.math.pow

//Implement View.onClickListener to listen to button clicks. This means we have to override onClick().
class MainActivity : AppCompatActivity(), View.OnClickListener {
    //Create variables to hold the three strings
    private var mFullName: String? = null
    private var mFirstName: String? = null
    private var mLastName: String? = null
    private var mAge: Int? = null
    private var mHeight: Int? = null
    private var mWeight: Int? = null
    private var mBMI: Float? = 0.0f
    private var mDailyCalories: Int? = 0
    private var mSex: Int? = null          // 0 = male; 1 = female;
    private var mActivityLvl: Int? = null // 0 = sedentary; 1 = moderate; 2 = very active;
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
    private var mRBMale: RadioButton? = null
    private var mRBFemale: RadioButton? = null
    private var mRBActLow: RadioButton? = null
    private var mRBActMed: RadioButton? = null
    private var mRBActHigh: RadioButton? = null
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

        //Get the Radio Buttons
        mRBMale = findViewById(R.id.rb_male)
        mRBFemale = findViewById(R.id.rb_female)
        mRBActLow = findViewById(R.id.rb_light)
        mRBActMed = findViewById(R.id.rb_moderate)
        mRBActHigh = findViewById(R.id.rb_heavy)

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

                //Next get the age from the age NumberPicker
                mEtAge = findViewById(R.id.et_age)
                if (!mEtAge?.text.isNullOrBlank()) {
                    mAge = Integer.parseInt(mEtAge?.text.toString())
                }
                if (mAge == 0 || mAge == null){ //throw warning if incorrect data
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Next get the height from the height NumberPicker
                mEtHeight = findViewById(R.id.et_height)
                if (!mEtHeight?.text.isNullOrBlank()) {
                    mHeight = Integer.parseInt(mEtHeight?.text.toString())
                }
                val splitByValues = Array(2){i->i.toString()}
                if (mHeight == 0 || mHeight == null) { //throw warning if no data
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Next get the weight from the weight EditTexts
                mEtWeight = findViewById(R.id.et_weight)
                if (!mEtWeight?.text.isNullOrBlank()) {
                    mWeight = Integer.parseInt(mEtWeight?.text.toString())
                }
                if (mWeight == 0 || mWeight == null) { //throw warning if bad data
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Next check Sex Radio Group inputs
                if (mRBMale!!.isActivated)
                    mSex = 0
                else if (mRBFemale!!.isActivated)
                    mSex = 1
                else if (!mRBMale!!.isActivated || !mRBFemale!!.isActivated)
                    Toast.makeText(
                        this@MainActivity,
                        "Please Select a sex for BMI and Calorie calculation",
                        Toast.LENGTH_SHORT
                    ).show()

                //Next check Activity Level Radio Group inputs
                if (mRBActLow!!.isActivated)
                    mActivityLvl = 0
                else if (mRBActMed!!.isActivated)
                    mActivityLvl = 1
                else if (mRBActHigh!!.isActivated)
                    mActivityLvl = 2
                else (!(mRBActLow!!.isActivated || mRBActMed!!.isActivated || mRBActHigh!!.isActivated))
                    Toast.makeText(
                        this@MainActivity,
                        "Please select an activity level for BMI and calorie calculation",
                        Toast.LENGTH_SHORT
                    ).show()
                if(mWeight != null && splitByValues[0].isNotBlank() && splitByValues[0].isNotBlank())
                mBMI = (703 * mWeight!!.toFloat()/(splitByValues[0].toInt().toFloat() * 12f + splitByValues[1].toInt().toFloat()).pow(2))

                //Harris Benedict Equation
                when(mActivityLvl){
                    0 -> { mDailyCalories = (mBMI!! * 1.2f).toInt() }
                    1 -> { mDailyCalories = (mBMI!! * 1.55f).toInt() }
                    2 -> { mDailyCalories = (mBMI!! * 1.725f).toInt() }
                }
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
            outState.putInt("Height", mHeight!!)
        if(mWeight != null)
            outState.putInt("Weight",mWeight!!)
        if(mBMI != null)
            outState.putFloat("BMI",mBMI!!)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState.getString("FullName") != null)
            mFullName = savedInstanceState.getString("FullName").toString()
        if(savedInstanceState.getString("FirstName") != null)
            mFirstName = savedInstanceState.getString("FirstName").toString()
        if(savedInstanceState.getString("LastName") != null)
            mLastName = savedInstanceState.getString("LastName").toString()
        if(savedInstanceState.getString("Height") != null)
            mHeight = savedInstanceState.getString("Height").toString().toInt()
        if(savedInstanceState.getString("Age") != null)
            mAge = savedInstanceState.getString("Age").toString().toInt()
        if(savedInstanceState.getString("Weight") != null)
            mWeight = savedInstanceState.getString("Weight").toString().toInt()
        if(savedInstanceState.getString("BMI") != null)
            mBMI = savedInstanceState.getString("BMI").toString().toFloat()
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