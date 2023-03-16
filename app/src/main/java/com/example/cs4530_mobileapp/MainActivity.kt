package com.example.cs4530_mobileapp

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

//Implement View.onClickListener to listen to button clicks. This means we have to override onClick().
class MainActivity : AppCompatActivity(), UserInfoFragment.DataPassingInterface,
                    HomePageFragment.DataPassingInterface {
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
    //Create the variable for the ImageView that holds the profile pic
    private var mIvPic: ImageView? = null
    private val isTablet: Boolean
        get() = resources.getBoolean(R.bool.isTablet)

    //Prep Master-Detail List
    private val listOfHeaders: ArrayList<String?> = arrayListOf ("Home Page",    //requirement #2 and #5
                                                         "User Info",    //requirement #1
                                                         "Hikes",        //requirement #3
                                                         "Weather")      //requirement #4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Place M-D into bundle for frags
        val mastDeetBundle = Bundle()
        mastDeetBundle.putStringArrayList("item_list", listOfHeaders)

        val listFrag = MasterListFragment()
        listFrag.arguments = mastDeetBundle
        val fTrans = supportFragmentManager.beginTransaction()

        if(isTablet){ //if tablet, only initialize the list side bar
            setContentView(R.layout.activity_main_tab)
            fTrans.replace(R.id.fl_listContainer_tab,listFrag, "md_list_frag")
        } else {    //else if phone, initialize entire screen with list
            setContentView(R.layout.activity_main_pho)
            fTrans.replace(R.id.fl_fragContainer, listFrag, "current_frag")
        }

        fTrans.commit()
    }

    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK) {
            mIvPic = findViewById<View>(R.id.iv_pic) as ImageView
            val thumbnailImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                {
                                    result.data!!.getParcelableExtra("data")
                                } else
                                {
                                    result.data!!.getParcelableExtra<Bitmap>("data")
                                }
            mIvPic!!.setImageBitmap(thumbnailImage)

            if(isExternalStorageWritable)
            {
                saveImage(thumbnailImage)
            }
            else
            {
                Toast.makeText(this, "External storage not writable.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun saveImage(finalBitmap: Bitmap?) {
        val root = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val fname = "profilepic.jpg"
        val file = File(myDir, fname)
        if (file.exists())
            file.delete()
        try
        {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Toast.makeText(this, "Picture saved!", Toast.LENGTH_SHORT).show()
        }
        catch (e: java.lang.Exception)
        {
            e.printStackTrace()
        }
    }

    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
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

    override fun passData(data: Array<String?>?) {
        when (data!![0]){
            "user info data" -> {
                mFirstName = data[1]
                mLastName = data[2]
                mAge = data[3]!!.toInt()
                mHeight = data[4]!!.toInt()
                mWeight = data[5]!!.toInt()
                mBMI = data[6]!!.toFloat()
                mDailyCalories = data[7]!!.toInt()
                mSex = data[8]!!.toInt()
                mActivityLvl = data[9]!!.toInt()
            }
            "frag change" -> {
                val newFrag: String = data[1]!!
                val fTrans = supportFragmentManager.beginTransaction()
                when (newFrag){
                    "list" -> {
                        if (!isTablet) {
                            val mastDeetBundle = Bundle()
                            mastDeetBundle.putStringArrayList("item_list", listOfHeaders)

                            val listFrag = MasterListFragment()
                            listFrag.arguments = mastDeetBundle

                            fTrans.replace(R.id.fl_fragContainer, listFrag, "current_fragment")
                            fTrans.commit()
                            }
                        }
                    "home" -> {
                        val mBundle = Bundle()
                        val homePageFragment = HomePageFragment()
                        mBundle.putFloat("BMI", mBMI!!)
                        mBundle.putInt("Calories", mDailyCalories!!)
                        homePageFragment.arguments = mBundle

                        fTrans.replace(R.id.fl_fragContainer, homePageFragment, "current_frag")
                        fTrans.commit()
                    }
                    "user info" -> {
                        //TODO: implement and add user info frag here
                    }
                    "hikes" -> {
                        //TODO: implement and add hike frag here
                    }
                    "weather" -> {
                        //TODO: implement and add weather info frag here
                    }
                }
            }
        }
    }
}