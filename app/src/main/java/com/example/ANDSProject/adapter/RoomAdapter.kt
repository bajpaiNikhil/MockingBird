package com.example.ANDSProject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ANDSProject.model.Added_Room_Details
import com.example.ANDSProject.R

class RoomAdapter (val roomList : ArrayList<Added_Room_Details>, val listner : (Added_Room_Details) -> Unit) : RecyclerView.Adapter<RoomAdapter.AdapterHolder>(){
    class AdapterHolder(view : View) : RecyclerView.ViewHolder(view) {

        val RType = view.findViewById<TextView>(R.id.Room_typeE)
        val Rdis = view.findViewById<TextView>(R.id.Room_disE)
        val Rcost = view.findViewById<TextView>(R.id.Room_costE)
        val imageToPick = view.findViewById<ImageView>(R.id.adminRoomimage)

    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : AdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item_list, parent , false)
        return AdapterHolder(view)
    }

    override fun onBindViewHolder(holder : AdapterHolder, position : Int) {
        val currentItem = roomList[position]
        holder.RType.text = currentItem.room_Type
        holder.Rdis.text = currentItem.room_Discount
        holder.Rcost.text = currentItem.room_Tariff
        Glide.with(holder.itemView.context).load(currentItem.room_image).into(holder.imageToPick)


        holder.itemView.setOnClickListener {
            listner(currentItem)
        }

    }

    override fun getItemCount() : Int {
        return roomList.size
    }


}