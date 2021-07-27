package com.example.ANDSProject.model

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ANDSProject.R
import com.example.ANDSProject.adapter.HotelAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class AdminRecyclerViewFragment : Fragment() {
    lateinit var recyclerView : RecyclerView

    lateinit var db: FirebaseDatabase

    lateinit var hotelArrayList : ArrayList<Hotel_Details>
    lateinit var AdminSeachhotelArrayList : ArrayList<Hotel_Details>
    lateinit var fab:FloatingActionButton

    var AdminSeachText = ""
    var flag = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.database
        recyclerView = view.findViewById(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        fab = view.findViewById(R.id.FAB)


        val AdminSearchView = view.findViewById<SearchView>(R.id.searchView)

        hotelArrayList = arrayListOf()
        AdminSeachhotelArrayList = arrayListOf()

        fab.setOnClickListener{
            findNavController().navigate(R.id.action_adminRecyclerViewFragment_to_adminFragment)
        }

        getHotelDetail()

        AdminSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query : String?) : Boolean {
                return false
            }

            override fun onQueryTextChange(newText : String?) : Boolean {

                AdminSeachText = newText!!
                if (flag == 1) {

                    Log.d("I_am_stupid" , " Reached till here True searchHotelArrayList , ${AdminSeachhotelArrayList.size}")
                    getHotelDetail()
                    //AdminSeachText = ""
                }
                else if(AdminSeachText.length > 3){
                    flag = 1
                    getHotelDetail()

                    Log.d("I_am_stupid" , " Reached till here True searchHotelText , ${AdminSeachText.length}")
                }
                return false
            }

        })
    }

    private fun getHotelDetail() {
        hotelArrayList.clear()
        AdminSeachhotelArrayList.clear()

        val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Hotel_Details")

        ref.addValueEventListener(object  : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    hotelArrayList.clear()
                    for (hotelSnapshot in snapshot.children) {

                        val hotel = hotelSnapshot.getValue(Hotel_Details::class.java)

                        if ("${hotel?.hotel_ID}".lowercase().contains(AdminSeachText.lowercase())) {
                            Log.d("I_am_stupid", " Reached till here Text is . $AdminSeachText")
                            AdminSeachhotelArrayList.add(hotel!!)
                            //UserHotelArrayList.add(hotel!!)
                            Log.d(
                                "I_am_stupid",
                                " Reached till here True searchHotelArrayList , $AdminSeachhotelArrayList"
                            )
                        }

                        hotelArrayList.add(hotel!!)

                    }
                    fun onitemSelected(hotelDetails: Hotel_Details) {
                        val b = bundleOf("hotelname" to hotelDetails.hotel_ID,
                        "hoteldesc" to hotelDetails.hotel_Description, "hotelemp" to hotelDetails.hotel_Employee,
                        "hotelloc" to hotelDetails.hotel_Location, "hotelisAcitve" to hotelDetails.hotel_isActive)
                        findNavController().navigate(R.id.action_adminRecyclerViewFragment_to_adminHotelModifyFragment,b)
                    }
                    if (AdminSeachhotelArrayList.size != 0) {
                        recyclerView.adapter =
                            HotelAdapter(AdminSeachhotelArrayList, ::onitemSelected)

                    } else {
                        recyclerView.adapter = HotelAdapter(hotelArrayList, ::onitemSelected)
                        flag = 0
                    }


                    val touchHelper = ItemTouchHelper(HotelTouchCallBack())
                    touchHelper.attachToRecyclerView(recyclerView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")

            }

        })
    }

    inner class HotelTouchCallBack : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
        {
            Log.d("ADMIN","onSwiped:$viewHolder")
            val hotel = hotelArrayList[viewHolder.adapterPosition]
            val hotelH = hotel.hotel_ID
            var builder = AlertDialog.Builder(context!!)
            builder.setCancelable(false)
            builder.setTitle("DELETE $hotelH")
            builder.setMessage("Are you sure you want to Delete")
            builder.setPositiveButton("yes", DialogInterface.OnClickListener
            {dialog, which ->
                Toast.makeText(context, "Hotel ${hotel.hotel_ID} Deleted", Toast.LENGTH_LONG).show()
                hotelArrayList.removeAt(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Hotel_Details").child(hotelH!!)
                ref.removeValue()})
            builder.setNegativeButton("No", DialogInterface.OnClickListener
            {dialog, which ->
                dialog.cancel()  })
            builder.create().show()
        }
    }
}

data class Hotel_Details(
    val hotel_ID : String? = null,
    val hotel_Description : String? = null,
    val hotel_Employee : String? = null,
    val hotel_Location : String? = null,
    val hotel_isActive : String? = null,
    var image_url : String = ""
)