package com.example.ANDSProject.model

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ANDSProject.R
import com.example.ANDSProject.adapter.UserHotelAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UserHotelListFragment : Fragment() {

    lateinit var recyclerView : RecyclerView
    lateinit var db: FirebaseDatabase
    lateinit var auth : FirebaseAuth
    lateinit var image: ImageView

    lateinit var UserHotelArrayList : ArrayList<User_Hotel_Details>
    lateinit var searchHotelArrayList : ArrayList<User_Hotel_Details>
    lateinit var seachviewis : SearchView
    var newTextInSearchBar = ""
    var flag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_hotel_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.database
        recyclerView = view.findViewById(R.id.UserRView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        seachviewis = view.findViewById(R.id.userSeachView)

        UserHotelArrayList = arrayListOf()
        searchHotelArrayList = arrayListOf()

        getHotelDetail()
        seachviewis.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query : String?) : Boolean {
                return false
            }

            override fun onQueryTextChange(newText : String?) : Boolean {
                Log.d("UserActivity", "enterText is $newText")

                newTextInSearchBar = newText!!
                if (flag == 1) {

                    Log.d("Iamstupid" , " REached till here True searchHotelArrayList , ${searchHotelArrayList.size}")
                    getHotelDetail()
                }
                else if(newTextInSearchBar.length > 3){
                    flag = 1
                    getHotelDetail()

                    Log.d("Iamstupid" , " REached till here True searchHotelArrayList , ${newTextInSearchBar.length}")
                }
                return false
            }

        })
    }

    private fun getHotelDetail() {
        UserHotelArrayList.clear()
        searchHotelArrayList.clear()
        val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Hotel_Details")

        ref.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot : DataSnapshot) {

                if(snapshot.exists()){
                    UserHotelArrayList.clear()
                    for(hotelSnapshot in snapshot.children){

                        val hotel = hotelSnapshot.getValue(User_Hotel_Details::class.java)

                        Log.d("userActivity" , "hotel name is ${hotel?.hotel_ID} , ${newTextInSearchBar} ")

                        if("${hotel?.hotel_ID}".contains(newTextInSearchBar)){
                            searchHotelArrayList.add(hotel!!)

                        }

                        UserHotelArrayList.add(hotel!!)

                    }
                    fun onitemSelected(userHotelDetails : User_Hotel_Details){
                        val b = bundleOf("HotelPk" to userHotelDetails.hotel_ID)
                        findNavController().navigate(R.id.action_userHotelListFragment_to_userRoomViewFragment, b)
                    }
                    if(searchHotelArrayList.size != 0){
                        recyclerView.adapter = UserHotelAdapter(searchHotelArrayList ,  ::onitemSelected)


                    }else{
                        recyclerView.adapter = UserHotelAdapter(UserHotelArrayList ,  ::onitemSelected)
                        flag = 0
                    }

                }
            }

            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}

data class User_Hotel_Details(
    val hotel_ID : String? = null,
    val hotel_Description : String? = null,
    val hotel_Location : String? = null,
    var image_url : String = ""
)