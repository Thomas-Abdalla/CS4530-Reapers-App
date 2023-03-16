package com.example.cs4530_mobileapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * A fragment representing a list of Items.
 */
class MasterListFragment : Fragment(), MyRVAdapter.passData {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    lateinit var dataPasser: OnDataPass



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_master_list_list, container, false)

        mRecyclerView = fragmentView.findViewById<View>(R.id.rv_master) as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(activity)
        mRecyclerView!!.layoutManager = layoutManager

        val inputList = arguments!!.getStringArrayList("item_list")

        mAdapter = MyRVAdapter(inputList,this)
        mRecyclerView!!.adapter = mAdapter
        return fragmentView
    }
    private fun passData(data: String?){
        dataPasser.onDataPass(data)
    }
    interface OnDataPass {
        fun onDataPass(data: String?)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    override fun onDataPass(data: String?) {
        passData(data)
    }
}