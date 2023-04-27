package com.example.cs4530_mobileapp

import android.annotation.SuppressLint
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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import java.io.File
import java.io.FileOutputStream

class UserInfoFragment : Fragment(), View.OnClickListener,  SeekBar.OnSeekBarChangeListener {
    //import parent VM
    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((activity!!.application as HikingApplication).userRepository)
    }
    //declare variable to update live data
    private var mUserData: UserData = UserData()

    //Create variables for the UI elements that we need to control
    private var mButtonSubmit: Button? = null
    private var mButtonCamera: ImageButton? = null
    private var mButtonHome: Button? = null
    private var mEtFullName: EditText? = null
    private var mSBAge: SeekBar? = null
    private var mSBHeight: SeekBar? = null
    private var mSBWeight: SeekBar? = null
    private var mRBMale: RadioButton? = null
    private var mRBFemale: RadioButton? = null
    private var mRBActLow: RadioButton? = null
    private var mRBActMed: RadioButton? = null
    private var mRBActHigh: RadioButton? = null

    //Create the variable for the ImageView that holds the profile pic
    private var mIvPic: ImageView? = null

    //fragment stuff
    private var mDataPasser: DataPassingInterface? = null

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

        //Set VM observer
        mUserViewModel.data.observe(this, userObserver)

        //Get the buttons
        mButtonSubmit = view?.findViewById(R.id.button_submit) as Button?
        mButtonCamera = view?.findViewById(R.id.button_pic) as ImageButton?
        mButtonHome = view?.findViewById(R.id.button_home) as Button?

        //Get the sliders
        mSBAge = view?.findViewById(R.id.sb_age) as SeekBar?
        mSBWeight = view?.findViewById(R.id.sb_weight) as SeekBar?
        mSBHeight = view?.findViewById(R.id.sb_height) as SeekBar?

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
        mSBAge!!.setOnSeekBarChangeListener(this)
        mSBWeight!!.setOnSeekBarChangeListener(this)
        mSBHeight!!.setOnSeekBarChangeListener(this)

       val root = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mIvPic?.setImageURI(Uri.fromFile(File("$root/saved_images", "profilepic.jpg")))

        return view
    }

    //Handle clicks for ALL buttons here
    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {
                //First, get the names from the name EditText
                mEtFullName = getView()?.findViewById(R.id.et_name) as EditText?
                var fullName = mEtFullName!!.text.toString()

                //Check if the EditText string is empty
                if (fullName.isBlank()) {
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
                    fullName = fullName.replace("^\\s+".toRegex(), "")

                    //Separate the string into first and last name using simple Java stuff
                    val splitStrings = fullName.split("\\s+".toRegex()).toTypedArray()
                    when (splitStrings.size) {
                        1 -> {
                            Toast.makeText(
                                activity,
                                "Enter both first and last name, separated by a space",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        2 -> {
                            mUserData.firstName = splitStrings[0]
                            mUserData.lastName = splitStrings[1]
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
                mSBAge = getView()?.findViewById(R.id.sb_age) as SeekBar?
                val tempAge = Integer.parseInt(mSBAge?.progress.toString())
                mUserData.age = tempAge
                if (tempAge == 0){ //throw warning if incorrect data
                    Toast.makeText(
                        activity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Next get the height from the height NumberPicker
                mSBHeight = getView()?.findViewById(R.id.sb_height) as SeekBar?
                val tempHeight = Integer.parseInt(mSBHeight?.progress.toString())
                mUserData.height = tempHeight
                Array(2){i->i.toString()}
                if (tempHeight == 0) { //throw warning if no data
                    Toast.makeText(
                        activity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Next get the weight from the weight EditTexts
                mSBWeight = getView()?.findViewById(R.id.sb_weight) as SeekBar?
                val tempWeight = Integer.parseInt(mSBWeight?.progress.toString())
                mUserData.weight = tempWeight
                if (tempWeight == 0) { //throw warning if bad data
                    Toast.makeText(
                        activity,
                        "Please enter data in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Next check Sex Radio Group inputs
                if (mRBMale!!.isChecked)
                    mUserData.sex = 0
                else if (mRBFemale!!.isChecked)
                    mUserData.sex = 1
                else
                    Toast.makeText(
                        activity,
                        "Please Select a sex for BMI and Calorie calculation",
                        Toast.LENGTH_SHORT
                    ).show()

                //Next check Activity Level Radio Group inputs
                if (mRBActLow!!.isChecked)
                    mUserData.activityLvl = 0
                else if (mRBActMed!!.isChecked)
                    mUserData.activityLvl = 1
                else if (mRBActHigh!!.isChecked)
                    mUserData.activityLvl = 2
                else
                    Toast.makeText(
                        activity,
                        "Please select an activity level for BMI and calorie calculation",
                        Toast.LENGTH_SHORT
                    ).show()

                mUserViewModel.setData(mUserData)
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

            R.id.button_home -> {
                    val dataToPass: Array<String?> = arrayOf("frag change", "list")
                    mDataPasser!!.passData(dataToPass)
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

    @SuppressLint("SetTextI18n")
    private val userObserver: Observer<UserData> = Observer {
        userData ->
            if(userData != null) { //populate relevant UI with appropriate data
                (view?.findViewById(R.id.et_name) as EditText).setText(userData.firstName + " " + userData.lastName)
                mSBAge?.progress = userData.age!!
                (view?.findViewById(R.id.tv_age_curr_value) as TextView).text = userData.age.toString()
                mSBHeight?.progress = userData.height!!
                (view?.findViewById(R.id.tv_height_curr_value) as TextView).text = userData.height.toString()
                mSBWeight?.progress = userData.weight!!
                (view?.findViewById(R.id.tv_weight_curr_value) as TextView).text = userData.weight.toString()
                val sex = userData.sex!!
                when(sex){
                    0 -> mRBMale?.isChecked = true
                    1 -> mRBFemale?.isChecked = true
                }
                val aLvl = userData.activityLvl!!
                when(aLvl){
                    0 -> mRBActLow?.isChecked = true
                    1 -> mRBActMed?.isChecked = true
                    2 -> mRBActHigh?.isChecked = true
                }
            }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.sb_age -> {
                val age = (view?.findViewById(R.id.sb_age) as SeekBar?)?.progress
                try {
                    (view?.findViewById(R.id.tv_age_curr_value) as TextView).text = age.toString()
                } catch (e: Exception) {
                    //handle errors here
                }
            }
            R.id.sb_weight -> {
                val weight = (view?.findViewById(R.id.sb_weight) as SeekBar?)?.progress
                try {
                    (view?.findViewById(R.id.tv_weight_curr_value) as TextView).text = weight.toString()
                } catch (e: Exception) {
                    //handle errors here
                }
            }
            R.id.sb_height -> {
                val height = (view?.findViewById(R.id.sb_height) as SeekBar?)?.progress
                try {
                    (view?.findViewById(R.id.tv_height_curr_value) as TextView).text = height.toString()
                } catch (e: Exception) {
                    //handle errors here
                }
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}