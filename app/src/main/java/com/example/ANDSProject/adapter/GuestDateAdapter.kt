package com.example.ANDSProject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ANDSProject.model.DateGuestList
import com.example.ANDSProject.R

class GuestDateAdapter(val DateList : ArrayList<DateGuestList> ) : RecyclerView.Adapter<GuestDateAdapter.DateHolder>() {

    class DateHolder (view : View) : RecyclerView.ViewHolder(view) {

        val CNamee = view.findViewById<TextView>(R.id.CustNameTT)
        val Cemaile = view.findViewById<TextView>(R.id.CustEmailTT)
        val CPhonee = view.findViewById<TextView>(R.id.CustPhTT)
        val CRoomide = view.findViewById<TextView>(R.id.CustRoomIDTT)
        val CRoomTypee = view.findViewById<TextView>(R.id.CustRoomTypeTT)
        val CRoomCounte = view.findViewById<TextView>(R.id.CustRoomCountTT)
        val CRoomCoste = view.findViewById<TextView>(R.id.CustRoomCostTT)

        val imageToPick = view.findViewById<ImageView>(R.id.orderViewImageTT)

    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : DateHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.date_guest_list_item, parent , false)
        return DateHolder(view)
    }

    override fun onBindViewHolder(holder : DateHolder, position : Int) {

        val currentItem = DateList[position]
        holder.CNamee.text = currentItem.userName_id
        holder.Cemaile.text = currentItem.email_id
        holder.CPhonee.text = currentItem.phoneNumber
        holder.CRoomide.text = currentItem.room_id
        holder.CRoomTypee.text = currentItem.ordered_Room_type
        holder.CRoomCounte.text = currentItem.ordered_Room
        holder.CRoomCoste.text = currentItem.orderValue

        Glide.with(holder.itemView.context).load(currentItem.order_room_image).into(holder.imageToPick)

    }

    override fun getItemCount() : Int {
       return DateList.size
    }
}