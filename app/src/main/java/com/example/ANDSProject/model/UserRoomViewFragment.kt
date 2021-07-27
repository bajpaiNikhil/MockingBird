package com.example.ANDSProject.model

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ANDSProject.R
import com.example.ANDSProject.adapter.UserRoomAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UserRoomViewFragment : Fragment() {


    lateinit var recyclerView : RecyclerView

    lateinit var db: FirebaseDatabase

    lateinit var UserRoomArrayList : ArrayList<User_Room_Details>
    lateinit var HotelPK: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_room_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HotelPK = arguments?.getString("HotelPk")!!
        db = Firebase.database
        recyclerView = view.findViewById(R.id.UserRoomRView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        UserRoomArrayList = arrayListOf()
        getRoomDetail()
    }
    private fun getRoomDetail() {
        val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Hotel_Details").child(HotelPK).child("RoomType")
        ref.addValueEventListener( object  : ValueEventListener {
            override fun onDataChange(snapshot : DataSnapshot) {
                if(snapshot.exists()){
                    for(roomSnapshot in snapshot.children){
                        val room  = roomSnapshot.getValue(User_Room_Details::class.java)
                        UserRoomArrayList.add(room!!)
                    }

                    fun onitemSelected(userRoomDetails : User_Room_Details) {
                        val b = bundleOf("HotelPK" to HotelPK, "RoomID" to userRoomDetails.room_Id,
                            "room_Type" to  userRoomDetails.room_Type, "room_Discount" to userRoomDetails.room_Discount,
                            "room_Tariff" to userRoomDetails.room_Tariff)
                        findNavController().navigate(R.id.action_userRoomViewFragment_to_userBookingFragment, b)

                    }
                    recyclerView.adapter = UserRoomAdapter(UserRoomArrayList , ::onitemSelected )
                }
                recyclerView.adapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

data class User_Room_Details(
    val room_Id : String? =null,
    val room_Type : String? = null,
    val room_Discount : String? = null,
    val room_Tariff : String? = null,
    val room_image : String =""
)