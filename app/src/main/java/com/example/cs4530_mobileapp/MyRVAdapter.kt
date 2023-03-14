package com.example.cs4530_mobileapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRVAdapter(private val mListItems: ArrayList<String>?) :
    RecyclerView.Adapter<MyRVAdapter.ViewHolder>() {
    private var mContext: Context? = null

    class ViewHolder(var itemLayout: View) : RecyclerView.ViewHolder(itemLayout){
        var itemTVData: TextView = itemLayout.findViewById(R.id.tv_data) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        val myView = layoutInflater.inflate(R.layout.item_layout, parent, false)
        return ViewHolder(myView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTVData.text = mListItems!![position]
        holder.itemLayout.setOnClickListener{ sendData(holder.absoluteAdapterPosition)}
    }

    private fun sendData(position: Int){
        //TODO send to activity which frag was clicked
    }

    override fun getItemCount(): Int {
        return mListItems!!.size
    }
}