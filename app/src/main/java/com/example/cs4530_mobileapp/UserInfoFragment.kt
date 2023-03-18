package com.example.cs4530_mobileapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream

class UserInfoFragment : Fragment(), View.OnClickListener,  SeekBar.OnSeekBarChangeListener {
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
    private var mButtonCamera: ImageButton? = null
    private var mButtonHome: Button? = null
    private var mEtFullName: EditText? = null
    private var SBAge: SeekBar? = null
    private var SBHeight: SeekBar? = null
    private var SBWeight: SeekBar? = null
    private var mRBMale: RadioButton? = null
    private var mRBFemale: RadioButton? = null
    private var mRBActLow: RadioButton? = null
    private var mRBActMed: RadioButton? = null
    private var mRBActHigh: RadioButton? = null
    //Create the variable for the ImageView that holds the profile pic
    private var mIvPic: ImageView? = null

    //fragment stuff
    var inBundle : Bundle? = null
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
        inBundle = arguments
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

        //Get the buttons
        mButtonSubmit = view?.findViewById(R.id.button_submit) as Button?
        mButtonCamera = view?.findViewById(R.id.button_pic) as ImageButton?
        mButtonHome = view?.findViewById(R.id.button_home) as Button?

        //Get the sliders
        SBAge = view?.findViewById(R.id.sb_age) as SeekBar?
        SBWeight = view?.findViewById(R.id.sb_weight) as SeekBar?
        SBHeight = view?.findViewById(R.id.sb_height) as SeekBar?


        //Get the Radio Buttons
        mRBMale = view?.findViewById(R.id.rb_male) as RadioButton?
        mRBFemale = view?.findViewById(R.id.rb_female) as RadioButton?
        mRBActLow = view?.findViewById(R.id.rb_light) as RadioButton?
        mRBActMed = view?.findViewById(R.id.rb_moderate) as RadioButton?
        mRBActHigh = view?.findViewById(R.id.rb_heavy) as RadioButton?
        mIvPic = view?.findViewById((R.id.iv_pic)) as ImageView?

        //Say that this class itself contains the listener.
        mButtonSubmit!!.setOnClickListener(this)
        mButtonCamera!!.setOnClickListener(this)
        mButtonHome!!.setOnClickListener(this)

        // Get the sliders and listeners
        SBAge!!.setOnSeekBarChangeListener(this)
        SBWeight!!.setOnSeekBarChangeListener(this)
        SBHeight!!.setOnSeekBarChangeListener(this)
        if(inBundle != null) {
            if(inBundle!!.getString("firstName") != null)
                mFirstName = inBundle!!.getString("firstName")
            if(inBundle!!.getString("lastName") != null)
                mLastName = inBundle!!.getString("lastName")
            if(inBundle!!.getString("age") != null) {
                SBAge?.progress = inBundle!!.getString("age").toString().toInt()
                (view?.findViewById(R.id.tv_age_curr_value) as TextView).text =
                    inBundle!!.getString("age").toString()
            }
            if(inBundle!!.getString("height") != null) {
                SBHeight?.progress = inBundle!!.getString("height").toString().toInt()
                (view?.findViewById(R.id.tv_height_curr_value) as TextView).text =
                    inBundle!!.getString("height").toString()
            }
            if(inBundle!!.getString("weight") != null) {
                SBWeight?.progress = inBundle!!.getString("weight").toString().toInt()
                (view?.findViewById(R.id.tv_weight_curr_value) as TextView).text = inBundle!!.getString("weight").toString()
            }
            if(inBundle!!.getString("sex") != null){
            val sex: Int = inBundle!!.getString("sex").toString().toInt()
            if (sex == 0) {
                mRBMale?.isChecked = true
            }
            else if (sex == 1) {
                mRBFemale?.isChecked = true
            }

             }
            if(inBundle!!.getString("activity") != null) {
                val act: Int = inBundle!!.getString("activity").toString().toInt()
                if (act == 0) {
                    mRBActLow?.isChecked = true
                }
                else if (act == 1) {
                    mRBActMed?.isChecked = true
                }
                else if (act == 2) {
                    mRBActHigh?.isChecked = true
                }
                if (mFirstName != null && mLastName != null)
                {
                    mFullName = mFirstName + " " + mLastName
                    mEtFullName?.setText(mFullName)
                }
            }
        }
       val root = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mIvPic?.setImageURI(Uri.fromFile(File("$root/saved_images", "profilepic.jpg")))


        return view
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState != null) {
            if(savedInstanceState.getString("firstName") != null)
                mFirstName = savedInstanceState.getString("firstName")
            if(savedInstanceState.getString("lastName") != null)
                mLastName = savedInstanceState.getString("lastName")
            if(savedInstanceState.getString("age") != null) {
                SBAge?.progress = savedInstanceState.getString("age").toString().toInt()
                (view?.findViewById(R.id.tv_age_curr_value) as TextView).text =
                    savedInstanceState.getString("age").toString()
            }
            if(savedInstanceState.getString("height") != null) {
                SBHeight?.progress = savedInstanceState.getString("height").toString().toInt()
                (view?.findViewById(R.id.tv_height_curr_value) as TextView).text =
                    savedInstanceState.getString("height").toString()
            }
            if(savedInstanceState.getString("weight") != null) {
                SBWeight?.progress = savedInstanceState.getString("weight").toString().toInt()
                (view?.findViewById(R.id.tv_weight_curr_value) as TextView).text = inBundle!!.getString("weight").toString()
            }
            if(savedInstanceState.getString("sex") != null){
                val sex: Int = savedInstanceState.getString("sex").toString().toInt()
                if (sex == 0) {
                    mRBMale?.isChecked = true
                }
                else if (sex == 1) {
                    mRBFemale?.isChecked = true
                }

            }
            if(savedInstanceState.getString("act") != null) {
                val act: Int = savedInstanceState.getString("act").toString().toInt()
                if (act == 0) {
                    mRBActLow?.isChecked = true
                }
                else if (act == 1) {
                    mRBActMed?.isChecked = true
                }
                else if (act == 2) {
                    mRBActHigh?.isChecked = true
                }
                if (mFirstName != null && mLastName != null)
                {
                    mFullName = mFirstName + " " + mLastName
                    mEtFullName?.setText(mFullName)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(mFirstName != null)
            outState.putString("firstName",mFirstName)
        if(mLastName != null)
            outState.putString("lastName",mLastName)
        outState.putString("age",SBAge?.progress.toString())
        outState.putString("height",SBHeight?.progress.toString())
        outState.putString("weight",SBWeight?.progress.toString())
        if(mRBMale!!.isChecked)
            outState.putString("sex","0")
        else
            outState.putString("sex","1")
        if (mRBActLow!!.isChecked) {
                    outState.putString("act","0")
        }
        else if (mRBActMed!!.isChecked) {
                    outState.putString("act","1")
        }
        else if (mRBActHigh!!.isChecked) {
                    outState.putString("act","2")
        }



    }
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.sb_age -> {
                val age = (view?.findViewById(R.id.sb_age) as SeekBar?)?.progress
                try {
                    (view?.findViewById(R.id.tv_age_curr_value) as TextView).text = age.toString()
                }
                catch (e : Exception){
                    //do error handling here
                }
            }

            R.id.sb_weight -> {
                val weight = (view?.findViewById(R.id.sb_weight) as SeekBar?)?.progress
                try {
                    (view?.findViewById(R.id.tv_weight_curr_value) as TextView).text =
                        weight.toString()
                }
                catch (e : Exception){
                    //do error handling here
                }
            }


            R.id.sb_height -> {
                val height = (view?.findViewById(R.id.sb_height) as SeekBar?)?.progress
                try {
                    (view?.findViewById(R.id.tv_height_curr_value) as TextView).text =
                        height.toString()
                }
                catch (e : Exception){
                    //do error handling here
                }
            }


        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    //Handle clicks for ALL buttons here
    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {

                //First, get the names from the name EditText
                mEtFullName = getView()?.findViewById(R.id.et_name) as EditText?
                mFullName = mEtFullName!!.text.toString()

                //Check if the EditText string is empty
                if (mFullName.isNullOrBlank()) {
                    //Complain that there's no text
                    Toast.makeText(
                        activity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //Reward them for submitting their names
                    Toast.makeText(
                        activity,
                        "Data Saved!",
                        Toast.LENGTH_SHORT
                    ).show()

                    //Remove any leading spaces or tabs
                    mFullName = mFullName!!.replace("^\\s+".toRegex(), "")

                    //Separate the string into first and last name using simple Java stuff
                    val splitStrings = mFullName!!.split("\\s+".toRegex()).toTypedArray()
                    when (splitStrings.size) {
                        1 -> {
                            Toast.makeText(
                                activity,
                                "Enter both first and last name, separated by a space",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        2 -> {
                            mFirstName = splitStrings[0]
                            mLastName = splitStrings[1]

                        }
                        else -> {
                            Toast.makeText(
                                activity,
                                "Enter only first and last name!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                //Next get the age from the age NumberPicker
                SBAge = getView()?.findViewById(R.id.sb_age) as SeekBar?
                mAge = Integer.parseInt(SBAge?.progress.toString())
                if (mAge == 0 || mAge == null){ //throw warning if incorrect data
                    Toast.makeText(
                        activity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Next get the height from the height NumberPicker
                SBHeight = getView()?.findViewById(R.id.sb_height) as SeekBar?
                mHeight = Integer.parseInt(SBHeight?.progress.toString())
                val splitByValues = Array(2){i->i.toString()}
                if (mHeight == 0 || mHeight == null) { //throw warning if no data
                    Toast.makeText(
                        activity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Next get the weight from the weight EditTexts
                SBWeight = getView()?.findViewById(R.id.sb_weight) as SeekBar?
                mWeight = Integer.parseInt(SBWeight?.progress.toString())
                if (mWeight == 0 || mWeight == null) { //throw warning if bad data
                    Toast.makeText(
                        activity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Next check Sex Radio Group inputs
                if (mRBMale!!.isChecked)
                    mSex = 0
                else if (mRBFemale!!.isChecked)
                    mSex = 1
                else if (!mRBMale!!.isChecked || !mRBFemale!!.isChecked)
                    Toast.makeText(
                        activity,
                        "Please Select a sex for BMI and Calorie calculation",
                        Toast.LENGTH_SHORT
                    ).show()

                //Next check Activity Level Radio Group inputs
                if (mRBActLow!!.isChecked)
                    mActivityLvl = 0
                else if (mRBActMed!!.isChecked)
                    mActivityLvl = 1
                else if (mRBActHigh!!.isChecked)
                    mActivityLvl = 2
                else
                    Toast.makeText(
                        activity,
                        "Please select an activity level for BMI and calorie calculation",
                        Toast.LENGTH_SHORT
                    ).show()

                if(mWeight != null && splitByValues[0].isNotBlank() && splitByValues[0].isNotBlank()) {
                    if (mSex == 0)
                        mBMI = (10 * mWeight!!.toFloat() + (mHeight!!.toFloat() * 6.25f) - 5* mAge!!.toFloat() + 5.0f)
                    else
                        mBMI = (10 * mWeight!!.toFloat() + (mHeight!!.toFloat() * 6.25f) - 5* mAge!!.toFloat() - 161.0f)
                }

                //Harris Benedict Equation
                when(mActivityLvl){
                    0 -> { mDailyCalories = (mBMI!! * 1.2f).toInt() }
                    1 -> { mDailyCalories = (mBMI!! * 1.55f).toInt() }
                    2 -> { mDailyCalories = (mBMI!! * 1.725f).toInt() }
                }
                try {
                    val dataToPass: Array<String?> = arrayOf(
                        "user info data", mFirstName, mLastName, mAge.toString(),
                        mHeight.toString(), mWeight.toString(),
                        mBMI.toString(), mDailyCalories.toString(),
                        mSex.toString(), mActivityLvl.toString()
                    )
                    mDataPasser!!.passData(dataToPass)
                }
                catch(e: Exception)
                {
                    //do error handling here
                }

            }

            //no intents in fragments
            R.id.button_home ->{
                //pass user info into activity
                if (mFirstName.isNullOrBlank() || mLastName.isNullOrBlank()) {
                    mFirstName = "John"
                    mLastName = "Doe"
                    Toast.makeText(
                        activity,
                        "First or last name left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (mAge == null)
                {
                    mAge = 18
                    Toast.makeText(
                        activity,
                        "Age left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (mHeight == null)
                {
                    mHeight = 70
                    Toast.makeText(
                        activity,
                        "Height left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (mWeight == null)
                {
                    mWeight = 200
                    Toast.makeText(
                        activity,
                        "Weight left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (mSex == null)
                {
                    mSex = 0;
                    Toast.makeText(
                        activity,
                        "Sex left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (mActivityLvl == null)
                {
                    mActivityLvl = 1
                    Toast.makeText(
                        activity,
                        "Activity Level left blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                try {
                    var dataToPass: Array<String?>? = arrayOf(
                        "user info data", mFirstName, mLastName, mAge.toString(),
                        mHeight.toString(), mWeight.toString(),
                        mBMI.toString(), mDailyCalories.toString(),
                        mSex.toString(), mActivityLvl.toString()
                    )
                    mDataPasser!!.passData(dataToPass)


                    //pass fragment change request to activity
                    dataToPass = arrayOf("frag change", "list")
                    mDataPasser!!.passData(dataToPass)
                }catch(e: Exception)
                {
                    val dataToPass : Array<String?> = arrayOf("frag change", "list")
                    mDataPasser!!.passData(dataToPass)
                }
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

    private val cameraActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val thumbnailImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data!!.getParcelableExtra("data")
                } else {
                    result.data!!.getParcelableExtra<Bitmap>("data")
                }
                mIvPic!!.setImageBitmap(thumbnailImage)

                if (isExternalStorageWritable) {
                    saveImage(thumbnailImage)
                } else {
                    Toast.makeText(activity, "External storage not writable.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    private fun saveImage(finalBitmap: Bitmap?) {
        val root = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val fname = "profilepic.jpg"
        val file = File(myDir, fname)
        if (file.exists())
            file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Toast.makeText(activity, "Picture saved!", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }
}