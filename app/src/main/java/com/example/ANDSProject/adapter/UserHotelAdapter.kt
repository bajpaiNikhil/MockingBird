package com.example.ANDSProject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ANDSProject.R
import com.example.ANDSProject.model.User_Hotel_Details

class UserHotelAdapter(val UserHotelList : ArrayList<User_Hotel_Details>, val listner : (User_Hotel_Details) -> Unit) : RecyclerView.Adapter<UserHotelAdapter.HolderAdapter>(){

    class HolderAdapter(view : View) : RecyclerView.ViewHolder(view)  {
        val hName = view.findViewById<TextView>(R.id.UserHotelNameT)
        val hDesc = view.findViewById<TextView>(R.id.UserHotelDescT)
        val hLocation = view.findViewById<TextView>(R.id.UserHotelLocT)
        val userHotelImage = view.findViewById<ImageView>(R.id.userHotelImage)
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : HolderAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_hotel_list_item, parent , false)
        return HolderAdapter(view)
    }

    override fun onBindViewHolder(holder : HolderAdapter, position : Int) {
        val currentItem = UserHotelList[position]
        holder.hName.text = currentItem.hotel_ID
        holder.hDesc.text = currentItem.hotel_Description
        holder.hLocation.text = currentItem.hotel_Location

        Glide.with(holder.itemView.context).load(currentItem.image_url).into(holder.userHotelImage)

        holder.itemView.setOnClickListener {
            listner(currentItem)
        }

    }

    override fun getItemCount() : Int {
        return UserHotelList.size
    }
}