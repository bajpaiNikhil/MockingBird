package com.example.ANDSProject.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.ANDSProject.model.HotelDialog
import com.example.ANDSProject.R
import com.google.firebase.database.FirebaseDatabase


class AdminHotelModifyFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    lateinit var db : FirebaseDatabase

    lateinit var HId : EditText
    lateinit var HdescE : EditText
    lateinit var Hemp : EditText
    lateinit var HLoc : EditText
    lateinit var HisAcitive : EditText
    lateinit var addRoom:Button
    lateinit var updateHotel:Button

    lateinit var viewBookingButton:Button
    lateinit var viewGuestList:Button
    lateinit var viewGuestDate:Button
    lateinit var dateTextView  : TextView
    lateinit var viewBookingDateT:ImageView

    lateinit var HotelPK : String

    var hotelName :String? = null
    var hotelDesc :String? = null
    var hotelemp :String? = null
    var hotelloc :String? = null
    var hotelisActive :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_hotel_modify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateTextView  = view.findViewById(R.id.ViewDateT)
        viewBookingButton = view.findViewById(R.id.ViewBookingButton)
        viewGuestList = view.findViewById(R.id.ViewGuestList)
        viewGuestDate = view.findViewById(R.id.ViewGuestDate)
        viewBookingDateT = view.findViewById(R.id.ViewBookingDateT)


        arguments?.let{
            hotelName = it.getString("hotelname")
            hotelDesc = it.getString("hoteldesc")
            hotelemp = it.getString("hotelemp")
            hotelloc = it.getString("hotelemp")
            hotelisActive = it.getString("hotelisAcitve")
        }

        HotelPK = hotelName!!

        HId = view.findViewById(R.id.hotelNE)
        HdescE = view.findViewById(R.id.hotelDE)
        Hemp = view.findViewById(R.id.hotelEmpT)
        HLoc = view.findViewById(R.id.hotelLo)
        HisAcitive = view.findViewById(R.id.HotelIsActive)
        addRoom  = view.findViewById(R.id.addroomB)
        updateHotel = view.findViewById(R.id.editBB)

        HId.setText("$hotelName")
        HdescE.setText("$hotelDesc")
        Hemp.setText("$hotelemp")
        HLoc.setText("$hotelloc")
        HisAcitive.setText("$hotelisActive")

        addRoom.setOnClickListener{
            val b = bundleOf("Hotel_ID" to HotelPK)
            findNavController().navigate(R.id.action_adminHotelModifyFragment_to_adminAddRoomActivityFragment,b)
        }
        updateHotel.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Hotel_Details").child(HotelPK)
            ref.child("hotel_Description").setValue(HdescE.text.toString())
            ref.child("hotel_Employee").setValue(Hemp.text.toString())
            ref.child("hotel_Location").setValue(HLoc.text.toString())
            ref.child("hotel_isActive").setValue(HisAcitive.text.toString())
            findNavController().navigate(R.id.action_adminHotelModifyFragment_to_adminRecyclerViewFragment)
        }
        viewBookingButton.setOnClickListener{
            val b = bundleOf("Hotel_ID" to HotelPK)
            findNavController().navigate(R.id.action_adminHotelModifyFragment_to_viewBookingFragment, b)
        }
        viewGuestList.setOnClickListener{
            val b = bundleOf("Hotel_ID" to HotelPK)
            findNavController().navigate(R.id.action_adminHotelModifyFragment_to_guestListFragment, b)
        }

        viewBookingDateT.setOnClickListener{
            val dlg = HotelDialog()
            dlg.setTargetFragment(requireParentFragment(),0)
            val b = Bundle()
            b.putInt("type" , 1)

            dlg.arguments = b
            fragmentManager?.let { it1 -> dlg.show(it1, "1") }
        }

        viewGuestDate.setOnClickListener{
            val b = bundleOf("Hotel_ID" to HotelPK, "dateis" to dateTextView.text.toString())
            findNavController().navigate(R.id.action_adminHotelModifyFragment_to_viewBookingDateFragment, b)
        }
    }
    override fun onDateSet(view : DatePicker?, year : Int, month : Int, dayOfMonth : Int) {
        Log.d("MainActivity" , "here is the way Date")
        val date = "$dayOfMonth/${month+1}/$year"
        dateTextView.text = date
    }
}