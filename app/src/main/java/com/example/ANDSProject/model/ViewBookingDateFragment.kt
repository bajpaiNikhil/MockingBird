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
import com.example.ANDSProject.adapter.GuestDateAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ViewBookingDateFragment : Fragment() {

    lateinit var hotelPk  : String
    lateinit var enterDate : String

    lateinit var recyclerView  : RecyclerView

    lateinit var db: FirebaseDatabase

    lateinit var guestOnDate : ArrayList<DateGuestList>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_booking_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let{
            hotelPk = it.getString("Hotel_ID")!!
            enterDate = it.getString("dateis")!!
        }

        recyclerView = view.findViewById(R.id.DateGuestListRV)
        recyclerView.layoutManager = LinearLayoutManager(context)

        guestOnDate = arrayListOf()
        getDateDetails()
    }

    private fun getDateDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Order_Details")
        ref.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot : DataSnapshot) {
                if (snapshot.exists()) {
                    for (order in snapshot.children) {
                        val DateDetails = order.getValue(DateGuestList::class.java)
                        Log.d("DateVIew", "REached till here $hotelPk , ${DateDetails?.Hotel_id}")
                        if (enterDate == "${DateDetails?.order_data}") {
                            guestOnDate.add(DateDetails!!)
                            Log.d("bookingView" , "Reached till here $guestOnDate")
                        }
                    }
                    Log.d("DateVIew" , "Reached till $guestOnDate")
                    recyclerView.adapter = GuestDateAdapter(guestOnDate)
                }
            }
            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}

data class DateGuestList(
    var Hotel_id : String = "",
    var room_id : String = "",
    var email_id : String = "",
    var userName_id : String = "",
    var phoneNumber : String = "",
    var orderValue : String = "",
    var ordered_Room : String = "",
    var ordered_Room_type : String = "",
    var order_room_image : String = "",
    var order_data : String ="",
)