package com.example.ANDSProject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ANDSProject.model.GuestView
import com.example.ANDSProject.R

class GuestAdapter(val GuestList : ArrayList<GuestView> ) : RecyclerView.Adapter<GuestAdapter.GuestHolder>()  {

    class GuestHolder(view : View) : RecyclerView.ViewHolder(view) {

        val CName = view.findViewById<TextView>(R.id.GuestNameT)
        val Cemail = view.findViewById<TextView>(R.id.GuestEmailT)
        val CPhone = view.findViewById<TextView>(R.id.GuestPhoneNoT)
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : GuestHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.guest_list_item, parent , false)
        return GuestHolder(view)
    }

    override fun onBindViewHolder(holder : GuestHolder, position : Int) {
        val currentItem = GuestList[position]
        holder.CName.text = currentItem.userName_id
        holder.Cemail.text = currentItem.email_id
        holder.CPhone.text = currentItem.phoneNumber
    }

    override fun getItemCount() : Int {
        return GuestList.size
    }

}