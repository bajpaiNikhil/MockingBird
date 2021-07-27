package com.example.ANDSProject.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.ANDSProject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AdminAddRoomActivityFragment : Fragment() {

    lateinit var Room_id : TextView
    lateinit var Room_Type : TextView
    lateinit var No_of_Room : TextView
    lateinit var Room_Tariff : TextView
    lateinit var Room_Discount : TextView
    lateinit var Max_Discount : TextView

    lateinit var addRoomButton : Button

    lateinit var db: FirebaseDatabase

    lateinit var auth : FirebaseAuth
    lateinit var image: ImageView
    //item image
    lateinit var uri : Uri
    var imageUrl : String =""
    var imageName : String =""
    lateinit var imageUploadProgressBar: ProgressBar


    lateinit var Hotelis : String
    lateinit var RoomTypeId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_add_room_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            Hotelis = it.getString("Hotel_ID")!!
        }

        Room_id = view.findViewById(R.id.Room_idT)
        No_of_Room = view.findViewById(R.id.roomCountT)

        Room_Type = view.findViewById(R.id.Room_TypeT)
        Room_Tariff = view.findViewById(R.id.Room_TariffT)
        Room_Discount = view.findViewById(R.id.Room_DiscountT)
        Max_Discount = view.findViewById(R.id.Max_DiscountT)
        image = view.findViewById(R.id.RoomivImage)
        imageUploadProgressBar = view.findViewById(R.id.AdminRomPB)
        imageUploadProgressBar.visibility = View.INVISIBLE
        db = Firebase.database
        auth = Firebase.auth


        addRoomButton = view.findViewById(R.id.addRoomB)
        addRoomButton.visibility = View.INVISIBLE
        addRoomButton.setOnClickListener {
            addRoomInventoryAndType()

        }
        image.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it,0)
            }
        }
    }
    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageUploadProgressBar.visibility = View.VISIBLE
        if(resultCode == Activity.RESULT_OK && requestCode == 0){
            uri = data?.data!! // this data refer to data of out intent

            image.setImageURI(uri)
            uploadImage(uri)
        }
    }

    private fun uploadImage(uri : Uri) {
        val auth = auth.currentUser.toString()
        Log.d("AdminActivity" , " Username is $auth")
        imageName = uri.lastPathSegment?.removePrefix("raw:/storage/emulated/0/Download/").toString()
        Log.d("AdminActivity","imageName : $imageName")
        val storageReference = FirebaseStorage.getInstance().reference.child("image/$imageName")

        if (uri != null) {
            storageReference.putFile(uri)
                .addOnSuccessListener {
                    it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                        imageUrl = it.toString()
                        Log.d("AddFragment","imageUrl: $imageUrl")
                    }
                    imageUploadProgressBar.visibility = View.INVISIBLE
                    addRoomButton.visibility = View.VISIBLE

                    Toast.makeText(context,"Image Successfully Uploaded", Toast.LENGTH_LONG).show()
                }.addOnFailureListener{
                    Toast.makeText(context,"Image Upload failed", Toast.LENGTH_LONG).show()
                }
        }

    }

    private fun addRoomInventoryAndType() {
        val Room_id = Room_id.text.toString()
        val No_of_Room = No_of_Room.text.toString()
        val Room_Type = Room_Type.text.toString()
        val Room_Tariff = Room_Tariff.text.toString()
        val Room_Discount = Room_Discount.text.toString()
        val Max_Discount = Max_Discount.text.toString()

        RoomTypeId = "$Room_id+$Hotelis"

        val roomInventoryObject = RoomInventoryHolder(RoomTypeId, No_of_Room , imageUrl)

        val ref = db.getReference("Application_Database")
        ref.child("RoomInventory").child(RoomTypeId).setValue(roomInventoryObject)


        val roomTypeInHotelObject = RoomDetailInHotel(RoomTypeId,Room_Type ,Room_Tariff, Room_Discount ,Max_Discount , imageUrl)

        val refType = db.getReference("Application_Database")
        refType.child("Hotel_Details").child(Hotelis).child("RoomType").child(RoomTypeId).setValue(roomTypeInHotelObject)
        val b = bundleOf("HotelIDis" to Hotelis, "RoomType" to RoomTypeId)
        findNavController().navigate(R.id.action_adminAddRoomActivityFragment_to_adminRoomRecyclerViewFragment, b )
//        activity?.onBackPressed()
    }
}

class RoomInventoryHolder(){
    var Room_id : String = ""  // primary key
    var No_of_Room : String = ""
    var Room_image : String =""
    constructor(
        Room_id : String,
        No_of_Room : String,
        Room_image : String
    ): this() {
        this.Room_id                = Room_id
        this.No_of_Room             = No_of_Room
        this.Room_image             = Room_image

    }
}

class RoomDetailInHotel(){
    var Room_Id : String = ""
    var Room_Type : String = ""
    var Room_Tariff : String = ""
    var Room_Discount : String = ""
    var Max_Discount : String = ""
    var Room_image : String =""
    constructor(
        Room_Id : String,
        Room_Type : String,
        Room_Tariff : String,
        Room_Discount : String,
        Max_Discount : String,
        Room_image : String
    ) : this(){
        this.Room_Id                = Room_Id
        this.Room_Type              = Room_Type
        this.Room_Tariff            = Room_Tariff
        this.Room_Discount          = Room_Discount
        this.Max_Discount           = Max_Discount
        this.Room_image           = Room_image
    }
}