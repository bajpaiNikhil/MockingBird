package com.example.ANDSProject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ANDSProject.R
import com.example.ANDSProject.model.User_Room_Details

class UserRoomAdapter(val UserRoomList : ArrayList<User_Room_Details>, val listner : (User_Room_Details) -> Unit) : RecyclerView.Adapter<UserRoomAdapter.RoomAdapterHolder>(){

    class RoomAdapterHolder(view : View) : RecyclerView.ViewHolder(view)  {

        val RType = view.findViewById<TextView>(R.id.UserRoomE)
        val Rdis = view.findViewById<TextView>(R.id.UserRoomDisE)
        val Rcost = view.findViewById<TextView>(R.id.UserRoomCostE)
        val imageToPick = view.findViewById<ImageView>(R.id.UserRoomImage)

    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : RoomAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_room_list_item, parent , false)
        return RoomAdapterHolder(view)
    }

    override fun onBindViewHolder(holder : RoomAdapterHolder, position : Int) {
        val currentItem = UserRoomList[position]
        holder.RType.text = currentItem.room_Type
        holder.Rdis.text = currentItem.room_Discount
        holder.Rcost.text = currentItem.room_Tariff

        Glide.with(holder.itemView.context).load(currentItem.room_image).into(holder.imageToPick)

        holder.itemView.setOnClickListener {
            listner(currentItem)
        }

    }

    override fun getItemCount() : Int {
       return UserRoomList.size
    }
}