package com.example.ANDSProject.model

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ANDSProject.R
import com.example.ANDSProject.adapter.GuestAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class GuestListFragment : Fragment() {

    lateinit var hotelPk  : String

    lateinit var recyclerView  : RecyclerView

    lateinit var db: FirebaseDatabase

    lateinit var GuestList : ArrayList<GuestView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hotelPk = arguments?.getString("Hotel_ID")!!
        recyclerView = view.findViewById(R.id.GuestRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        GuestList = arrayListOf()

        db = Firebase.database
        getGuestDetails()

    }

    private fun getGuestDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Order_Details")
        ref.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot : DataSnapshot) {
                if (snapshot.exists()) {
                    for (order in snapshot.children) {
                        val orderDetails = order.getValue(GuestView::class.java)
                        Log.d("bookingView", "REached till here $hotelPk , ${orderDetails?.Hotel_id}")
                        if (hotelPk == "${orderDetails?.Hotel_id}") {
                            GuestList.add(orderDetails!!)
                            Log.d("bookingView" , "Reached till here $GuestList")
                        }
                    }
                    Log.d("bookingView" , "Reached till $GuestList")
                    recyclerView.adapter = GuestAdapter(GuestList)
                }
            }
            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}

data class GuestView(
    var Hotel_id : String = "",
    var room_id : String = "",
    var email_id : String = "",
    var userName_id : String = "",
    var phoneNumber : String = "",
    var orderValue : String = "",
    var ordered_Room : String = "",
    var ordered_Room_type : String = "",
    var order_room_image : String = "",
)