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
import androidx.navigation.fragment.findNavController
import com.example.ANDSProject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AdminFragment : Fragment() {

    lateinit var auth : FirebaseAuth

    lateinit var Hotel_ID : TextView
    lateinit var Hotel_Description : TextView
    lateinit var Hotel_Location : TextView
    lateinit var Hotel_isActive : TextView
    lateinit var Hotel_Employee : TextView
    lateinit var addButton : Button

    //for image related stuff
    lateinit var image: ImageView
    lateinit var uri : Uri
    var imageUrl : String =""
    var imageName : String =""
    lateinit var imageUploadProgressBar: ProgressBar

    lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        Hotel_ID = view.findViewById(R.id.Hotel_NameT)
        Hotel_Description = view.findViewById(R.id.Hotel_DescriptionT)
        Hotel_Location = view.findViewById(R.id.hotelLo)
        Hotel_isActive = view.findViewById(R.id.Hotel_isActiveT)
        Hotel_Employee =  view.findViewById(R.id.Hotel_EmployeeT)
        image =  view.findViewById(R.id.ivPhoto)

        imageUploadProgressBar = view.findViewById(R.id.AdminHotelPB)
        imageUploadProgressBar.visibility = View.INVISIBLE

        db = Firebase.database

        addButton =  view.findViewById(R.id.addHotelB)

        addButton.visibility = View.INVISIBLE
        addButton.setOnClickListener {
            addHotel()
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
                    addButton.visibility = View.VISIBLE
                    Toast.makeText(context,"Image Successfully Uploaded", Toast.LENGTH_LONG).show()
                }.addOnFailureListener{
                    Toast.makeText(context,"Image Upload failed", Toast.LENGTH_LONG).show()
                }
        }

    }

    private fun addHotel() {

        val Hotel_ID = Hotel_ID.text.toString()
        val Hotel_Description = Hotel_Description.text.toString()
        val Hotel_Location = Hotel_Location.text.toString()
        val Hotel_isActive = Hotel_isActive.text.toString()
        val Hotel_Employee = Hotel_Employee.text.toString()

        val hotelObject  =  HotelHolder(Hotel_ID , Hotel_Location , Hotel_Description, Hotel_isActive , Hotel_Employee , imageUrl)

        val ref = db.getReference("Application_Database")
        ref.child("Hotel_Details").child(Hotel_ID).setValue(hotelObject)
        findNavController().navigate(R.id.action_adminFragment_to_adminRecyclerViewFragment)

    }

}

class HotelHolder() {
    var Hotel_ID : String = "" // primary key
    var Hotel_Location : String = ""
    var Hotel_Description : String = ""
    var Hotel_isActive : String = ""
    var Hotel_Employee : String = ""
    var image_url : String = ""

    constructor(
        Hotel_ID : String,
        Hotel_Location : String,
        Hotel_Description : String,
        Hotel_isActive : String,
        Hotel_Employee : String,
        image_url : String

    ) : this(){
        this.Hotel_ID               = Hotel_ID
        this.Hotel_Location         = Hotel_Location
        this.Hotel_Description      = Hotel_Description
        this.Hotel_isActive         = Hotel_isActive
        this.Hotel_Employee         = Hotel_Employee
        this.image_url              = image_url

    }
}