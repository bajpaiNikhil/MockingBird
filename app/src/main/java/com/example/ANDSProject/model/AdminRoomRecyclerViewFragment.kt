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
import com.example.ANDSProject.adapter.RoomAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class AdminRoomRecyclerViewFragment : Fragment() {

    lateinit var recyclerView : RecyclerView

    lateinit var db: FirebaseDatabase

    lateinit var RoomArrayList : ArrayList<Added_Room_Details>

    lateinit var HotelIdIS : String
    lateinit var RoomTypeis : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_room_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let{
            HotelIdIS = it.getString("HotelIDis")!!
            RoomTypeis = it.getString("RoomType")!!
        }
        db = Firebase.database
        recyclerView = view.findViewById(R.id.RoomrView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        RoomArrayList = arrayListOf()
        getRoomDetail()
    }

    private fun getRoomDetail() {
        val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Hotel_Details").child(HotelIdIS).child("RoomType")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot : DataSnapshot) {
                if(snapshot.exists()){

                    for(roomSnapshot in snapshot.children){
                        val room  = roomSnapshot.getValue(Added_Room_Details::class.java)
                        RoomArrayList.add(room!!)
                    }


                    fun onitemSelected(addedRoomDetails : Added_Room_Details) {
                        val b = bundleOf("room_idIS" to RoomTypeis,
                            "HotelIdIs" to HotelIdIS, "roomType" to addedRoomDetails.room_Type,
                            "roomDis" to  addedRoomDetails.room_Discount, "roomCost" to addedRoomDetails.room_Tariff )
                        findNavController().navigate(R.id.action_adminRoomRecyclerViewFragment_to_adminRoomModifyFragment, b)
                    }
                    recyclerView.adapter = RoomAdapter(RoomArrayList , :: onitemSelected)
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }


            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

data class Added_Room_Details(
    val room_id : String? = null,
    val room_Type : String? = null,
    val room_Discount : String? = null,
    val room_Tariff : String? = null,
    val room_image : String =""
)
