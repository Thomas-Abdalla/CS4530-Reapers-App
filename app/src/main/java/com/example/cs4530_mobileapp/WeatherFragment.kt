import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cs4530_mobileapp.R

class WeatherFragment : Fragment(), View.OnClickListener {
    //initialize UI variables
    private var mButtonProfile: Button? = null
    private var mButtonHikes: Button? = null
    private var mButtonWeather: Button? = null
    //fun fragment function
    var mDataPasser: DataPassingInterface? = null

    interface DataPassingInterface {
        fun passData(data: Array<String?>?)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        //receive arguments bundle
        val inBundle = arguments

        //link .kt to .xml
        mButtonProfile = view?.findViewById(R.id.button_return) as Button?

        //link button to onClick
        mButtonProfile!!.setOnClickListener(this)
        return view
    }

    //prepping for sending to activity
    override fun onAttach(context: Context){
        super.onAttach(context)
        mDataPasser = try {
            context as WeatherFragment.DataPassingInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement WeatherFragment.DataPassingInterface")
        }
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.button_return ->{
                mDataPasser!!.passData(arrayOf("frag change", "list"))
                }
            }
        }
    }
