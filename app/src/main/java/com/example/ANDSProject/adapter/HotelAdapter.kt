package com.example.ANDSProject.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ANDSProject.model.Hotel_Details
import com.example.ANDSProject.R

class HotelAdapter(val hotelList : ArrayList<Hotel_Details>, val listner : (Hotel_Details)->Unit) : RecyclerView.Adapter<HotelAdapter.AdapterHolder>() {

    class AdapterHolder(view : View) : RecyclerView.ViewHolder(view) {
        val hName = view.findViewById<TextView>(R.id.HotelNameA)
        val hDesc = view.findViewById<TextView>(R.id.HotelLoc)
        val hLocation = view.findViewById<TextView>(R.id.HotelDecsA)
        val hActive = view.findViewById<TextView>(R.id.hotel_isAcitveT)
        val hEmployee = view.findViewById<TextView>(R.id.hotel_employee)
        val imageToPick = view.findViewById<ImageView>(R.id.adminHotelimage)
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : AdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotel_list_item, parent,false)
        return AdapterHolder(view)
    }

    override fun onBindViewHolder(holder : AdapterHolder, position : Int) {
        val currentItem = hotelList[position]
        holder.hName.text = currentItem.hotel_ID
        holder.hDesc.text = currentItem.hotel_Description
        holder.hLocation.text = currentItem.hotel_Location
        holder.hActive.text = currentItem.hotel_isActive
        holder.hEmployee.text = currentItem.hotel_Employee

        val imageurl = currentItem.image_url
        Glide.with(holder.itemView.context).load(imageurl).into(holder.imageToPick)

        Log.d("HotelAdapter", " the value is ${holder.hName.text}")

        holder.itemView.setOnClickListener {
            listner(currentItem)
        }
    }

    override fun getItemCount() : Int {
        return hotelList.size
    }
}