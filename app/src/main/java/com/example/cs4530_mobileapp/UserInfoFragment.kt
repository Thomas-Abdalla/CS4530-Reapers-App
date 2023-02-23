package com.example.cs4530_mobileapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlin.math.pow

class UserInfoFragment : Fragment(), View.OnClickListener {
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

    //fragment stuff
    var inBundle = arguments
    var mDataPasser: DataPassingInterface? = null
    interface DataPassingInterface {
        fun passData(data: Array<String?>?)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

        //Get the buttons
        mButtonSubmit = view?.findViewById(R.id.button_submit) as Button?
        mButtonCamera = view?.findViewById(R.id.button_pic) as Button?
        mButtonHome = view?.findViewById(R.id.button_home) as Button?

        //Get the Radio Buttons
        mRBMale = view?.findViewById(R.id.rb_male) as RadioButton?
        mRBFemale = view?.findViewById(R.id.rb_female) as RadioButton?
        mRBActLow = view?.findViewById(R.id.rb_light) as RadioButton?
        mRBActMed = view?.findViewById(R.id.rb_moderate) as RadioButton?
        mRBActHigh = view?.findViewById(R.id.rb_heavy) as RadioButton?

        //Say that this class itself contains the listener.
        mButtonSubmit!!.setOnClickListener(this)
        mButtonCamera!!.setOnClickListener(this)
        mButtonHome!!.setOnClickListener(this)

        return view
    }

    //Handle clicks for ALL buttons here
    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {

                //First, get the names from the name EditText
                mEtFullName = view.findViewById(R.id.et_name) as EditText?
                mFullName = mEtFullName!!.text.toString()

                //Check if the EditText string is empty
                if (mFullName.isNullOrBlank()) {
//                    //Complain that there's no text
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Please enter data in all fields",
//                        Toast.LENGTH_SHORT
//                    ).show()
                } else {
//                    //Reward them for submitting their names
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Welcome!",
//                        Toast.LENGTH_SHORT
//                    ).show()

                    //Remove any leading spaces or tabs
                    mFullName = mFullName!!.replace("^\\s+".toRegex(), "")

                    //Separate the string into first and last name using simple Java stuff
                    val splitStrings = mFullName!!.split("\\s+".toRegex()).toTypedArray()
                    when (splitStrings.size) {
                        1 -> {
//                            Toast.makeText(
//                                this@MainActivity,
//                                "Enter both first and last name, separated by a space",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                        2 -> {
                            mFirstName = splitStrings[0]
                            mLastName = splitStrings[1]

                        }
                        else -> {
//                            Toast.makeText(
//                                this@MainActivity,
//                                "Enter only first and last name!",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }
                }

                //Next get the age from the age NumberPicker
                mEtAge = view?.findViewById(R.id.et_age) as EditText?
                if (!mEtAge?.text.isNullOrBlank()) {
                    mAge = Integer.parseInt(mEtAge?.text.toString())
                }
                if (mAge == 0 || mAge == null){ //throw warning if incorrect data
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Please enter data in all fields",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                //Next get the height from the height NumberPicker
                mEtHeight = view?.findViewById(R.id.et_height) as EditText?
                if (!mEtHeight?.text.isNullOrBlank()) {
                    mHeight = Integer.parseInt(mEtHeight?.text.toString())
                }
                val splitByValues = Array(2){i->i.toString()}
                if (mHeight == 0 || mHeight == null) { //throw warning if no data
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Please enter data in all fields",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                //Next get the weight from the weight EditTexts
                mEtWeight = view?.findViewById(R.id.et_weight) as EditText?
                if (!mEtWeight?.text.isNullOrBlank()) {
                    mWeight = Integer.parseInt(mEtWeight?.text.toString())
                }
                if (mWeight == 0 || mWeight == null) { //throw warning if bad data
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Please enter data in all fields",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                //Next check Sex Radio Group inputs
                if (mRBMale!!.isChecked)
                    mSex = 0
                else if (mRBFemale!!.isChecked)
                    mSex = 1
                else if (!mRBMale!!.isChecked || !mRBFemale!!.isChecked)
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Please Select a sex for BMI and Calorie calculation",
//                        Toast.LENGTH_SHORT
//                    ).show()

                //Next check Activity Level Radio Group inputs
                if (mRBActLow!!.isChecked)
                    mActivityLvl = 0
                else if (mRBActMed!!.isChecked)
                    mActivityLvl = 1
                else if (mRBActHigh!!.isChecked)
                    mActivityLvl = 2
                else
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Please select an activity level for BMI and calorie calculation",
//                        Toast.LENGTH_SHORT
//                    ).show()
                if(mWeight != null && splitByValues[0].isNotBlank() && splitByValues[0].isNotBlank())
                    mBMI = (703 * mWeight!!.toFloat()/(mHeight!!.toFloat().pow(2)))

                //Harris Benedict Equation
                when(mActivityLvl){
                    0 -> { mDailyCalories = (mBMI!! * 1.2f*100).toInt() }
                    1 -> { mDailyCalories = (mBMI!! * 1.55f*100).toInt() }
                    2 -> { mDailyCalories = (mBMI!! * 1.725f*100).toInt() }
                }
            }

            //no intents in fragments
            R.id.button_home ->{
                //<--TODO--> implement data sending to main activity
                //send intent for home page
                //val homeActivityIntent = Intent(this, HomePageActivity::class.java)
                //homeActivityIntent.putExtra("BMI",mBMI!!)
                //homeActivityIntent.putExtra("Calories", mDailyCalories!!)
                //startActivity(homeActivityIntent)
            }

            R.id.button_pic -> {
                //The button press should open a camera
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try{
                    cameraActivity.launch(cameraIntent)
                }catch(ex: ActivityNotFoundException){
                    //Do error handling here
                }
            }
        }
    }
    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            mIvPic = view?.findViewById<View>(R.id.iv_pic) as ImageView
            val thumbnailImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            {
                result.data!!.getParcelableExtra("data")
            } else
            {
                result.data!!.getParcelableExtra<Bitmap>("data")
            }
            mIvPic!!.setImageBitmap(thumbnailImage)

            //TODO--> decipher what is done in fragment vs in activity
            //if(isExternalStorageWritable)
            //{
           //    saveImage(thumbnailImage)
           // }
            //else
            //{
                //Toast.makeText(this, "External storage not writable.", Toast.LENGTH_SHORT).show()
            //}

        }
    }

   // private fun saveImage(finalBitmap: Bitmap?) {
        //<--TODO--> This should be Activity side only (everything below)
        //val root = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //val myDir = File("$root/saved_images")
        //myDir.mkdirs()
        //val fname = "profilepic.jpg"
        //val file = File(myDir, fname)
        //if (file.exists())
        //    file.delete()
        //try
        //{
        //    val out = FileOutputStream(file)
        //  finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
        //    out.flush()
         //   out.close()
         //   Toast.makeText(this, "Picture saved!", Toast.LENGTH_SHORT).show()
        //}
       // catch (e: java.lang.Exception)
        //{
       //     e.printStackTrace()
        //}
    //}

//    private val isExternalStorageWritable: Boolean
//        get() {
//            val state = Environment.getExternalStorageState()
//            return Environment.MEDIA_MOUNTED == state
//        }
//
//    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        super.onSaveInstanceState(outState, outPersistentState)
//        if(mFullName != null)
//            outState.putString("FullName",mFullName)
//        if(mFirstName!=null)
//            outState.putString("FirstName",mFirstName)
//        if(mLastName!=null)
//            outState.putString("LastName",mLastName)
//        if(mAge != null)
//            outState.putInt("Age", mAge!!)
//        if(mHeight != null)
//            outState.putInt("Height", mHeight!!)
//        if(mWeight != null)
//            outState.putInt("Weight",mWeight!!)
//        if(mBMI != null)
//            outState.putFloat("BMI",mBMI!!)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        if(savedInstanceState.getString("FullName") != null)
//            mFullName = savedInstanceState.getString("FullName").toString()
//        if(savedInstanceState.getString("FirstName") != null)
//            mFirstName = savedInstanceState.getString("FirstName").toString()
//        if(savedInstanceState.getString("LastName") != null)
//            mLastName = savedInstanceState.getString("LastName").toString()
//        if(savedInstanceState.getString("Height") != null)
//            mHeight = savedInstanceState.getString("Height").toString().toInt()
//        if(savedInstanceState.getString("Age") != null)
//            mAge = savedInstanceState.getString("Age").toString().toInt()
//        if(savedInstanceState.getString("Weight") != null)
//            mWeight = savedInstanceState.getString("Weight").toString().toInt()
//        if(savedInstanceState.getString("BMI") != null)
//            mBMI = savedInstanceState.getString("BMI").toString().toFloat()
//    }

//    override fun onStop() {
//        super.onStop()
//        val filename:String?
//        var fileContents:String?
//        if(mFullName != null) {
//            filename = mFullName!!
//            fileContents = ""
//            fileContents += mFullName!!.toString() + "\n"
//            fileContents += mFirstName!!.toString() + "\n"
//            fileContents += mLastName!!.toString() + "\n"
//            fileContents += mAge!!.toString() + "\n"
//            fileContents += mHeight!!.toString() + "\n"
//            fileContents += mWeight!!.toString() + "\n"
//            fileContents += mBMI!!.toString() + "\n"
//            try {
//                val output = openFileOutput(filename, AppCompatActivity.MODE_PRIVATE)
//                output.write(fileContents.toByteArray())
//                output.close()
//            } catch (e : Exception)
//            {
//                e.printStackTrace()
//            }
//        }
//
//    }
}