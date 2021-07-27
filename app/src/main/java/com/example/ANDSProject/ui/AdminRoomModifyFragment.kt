package com.example.ANDSProject.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.ANDSProject.R
import com.google.firebase.database.FirebaseDatabase


class AdminRoomModifyFragment : Fragment() {

    lateinit var db : FirebaseDatabase

    lateinit var RoomPK : String
    lateinit var HotelPk  : String

    lateinit var RoomType : EditText
    lateinit var RoomDis : EditText
    lateinit var RoomCost : EditText
    lateinit var updateroom: Button
    lateinit var deleteroom:Button
    var rType : String? = null
    var rDis : String? = null
    var rCost : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_room_modify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let{
            RoomPK = it.getString("room_idIS")!!
            HotelPk = it.getString("HotelIdIs")!!
            rType = it.getString("roomType")
            rDis = it.getString("roomDis")
            rCost = it.getString("roomCost")
        }
        RoomType = view.findViewById(R.id.RoomtypeA)
        RoomDis = view.findViewById(R.id.RoomdisA)
        RoomCost = view.findViewById(R.id.RoomcostA)

        RoomType.setText("$rType")
        RoomDis.setText("$rDis")
        RoomCost.setText("$rCost")

        updateroom = view.findViewById(R.id.updateB)
        updateroom.setOnClickListener{
            val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Hotel_Details")
                .child(HotelPk).child("RoomType").child(RoomPK)
            ref.child("room_Type").setValue(RoomType.text.toString())
            ref.child("room_Discount").setValue(RoomDis.text.toString())
            ref.child("room_Tariff").setValue(RoomCost.text.toString())
            val b = bundleOf("HotelIDis" to HotelPk, "RoomType" to RoomPK )
            findNavController().navigate(R.id.action_adminRoomModifyFragment_to_adminRoomRecyclerViewFragment, b)
//            activity?.onBackPressed()

        }
        deleteroom = view.findViewById(R.id.deleteB)
        deleteroom.setOnClickListener{
            val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Hotel_Details")
                .child(HotelPk).child("RoomType").child(RoomPK)
            ref.removeValue()
            val ref2 = FirebaseDatabase.getInstance().getReference("Application_Database").child("RoomInventory").child(RoomPK)
            ref2.removeValue()
            val b = bundleOf("HotelIDis" to HotelPk, "RoomType" to RoomPK )
            findNavController().navigate(R.id.action_adminRoomModifyFragment_to_adminRoomRecyclerViewFragment, b)
//            activity?.onBackPressed()
        }
    }
}