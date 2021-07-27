package com.example.ANDSProject.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ANDSProject.model.BookingView
import com.example.ANDSProject.R

class OrderAdapter(val hotelList : ArrayList<BookingView> ) : RecyclerView.Adapter<OrderAdapter.OrderHolder>() {

    class OrderHolder (view : View) : RecyclerView.ViewHolder(view){

        val CName = view.findViewById<TextView>(R.id.CustNameT)
        val Cemail = view.findViewById<TextView>(R.id.CustEmailT)
        val CPhone = view.findViewById<TextView>(R.id.CustPhT)
        val CRoomid = view.findViewById<TextView>(R.id.CustRoomIDT)
        val CRoomType = view.findViewById<TextView>(R.id.CustRoomTypeT)
        val CRoomCount = view.findViewById<TextView>(R.id.CustRoomCountT)
        val CRoomCost = view.findViewById<TextView>(R.id.CustRoomCostT)

        val imageToPick = view.findViewById<ImageView>(R.id.orderViewImage)


    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : OrderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_booking_hotel, parent , false)
        return OrderHolder(view)

    }

    override fun onBindViewHolder(holder : OrderHolder, position : Int) {
        val currentItem = hotelList[position]
        holder.CName.text = currentItem.userName_id
        holder.Cemail.text = currentItem.email_id
        holder.CPhone.text = currentItem.phoneNumber
        holder.CRoomid.text = currentItem.room_id
        holder.CRoomType.text = currentItem.ordered_Room_type
        holder.CRoomCount.text = currentItem.ordered_Room
        holder.CRoomCost.text = currentItem.orderValue

        Log.d("bookingView", "REached till here1111 ${holder.CName.text}")

        Glide.with(holder.itemView.context).load(currentItem.order_room_image).into(holder.imageToPick)

    }

    override fun getItemCount() : Int {
        return hotelList.size
    }

}















