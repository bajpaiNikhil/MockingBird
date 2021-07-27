package com.example.ANDSProject.model

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.example.ANDSProject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class UserBookingFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    lateinit var auth : FirebaseAuth
    lateinit var RoomId : String
    lateinit var HotelPK : String

    lateinit var RoomType: String
    lateinit var RoomDiscount: String
    lateinit var RoomTariff: String

    lateinit var rtype : TextView
    lateinit var rdis : TextView
    lateinit var rcost : TextView
    lateinit var rCount : String

    lateinit var enterCount : EditText

    lateinit var checkButton : Button
    lateinit var bookButton  : Button

    lateinit var db: FirebaseDatabase

    lateinit var dateTextView  :  TextView
    lateinit var imageDate : ImageView

    var userUsername : String ?= ""
    var userPhoneNumber  : String ?= ""
    lateinit var room_img : String

    lateinit var viewKonfetti : KonfettiView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{
            RoomId = it.getString("RoomID")!!
            HotelPK = it.getString("HotelPK")!!
            RoomType = it.getString("room_Type")!!
            RoomDiscount = it.getString("room_Discount")!!
            RoomTariff = it.getString("room_Tariff")!!
        }

        rtype = view.findViewById(R.id.RTtextView)
        rdis = view.findViewById(R.id.RDtextView)
        rcost = view.findViewById(R.id.RCtextView)
        dateTextView = view.findViewById(R.id.dateE)

        rtype.setText(RoomType)
        rdis.setText(RoomDiscount)
        rcost.setText(RoomTariff)


        enterCount = view.findViewById(R.id.EnterRNtextView)
        enterCount.text.toString()

        checkButton = view.findViewById(R.id.checkButtonB)
        bookButton = view.findViewById(R.id.bookButtonB)

        db = Firebase.database
        auth = FirebaseAuth.getInstance()
        viewKonfetti = view.findViewById(R.id.viewKonfetti)

        imageDate = view.findViewById(R.id.dateIV)

        imageDate.setOnClickListener {

            val dlg = HotelDialog()
            dlg.setTargetFragment(this , 0)
            val b = Bundle()
            b.putInt("type" , 1)
            dlg.arguments = b
            dlg.show(this.requireFragmentManager(),"1")

        }

        checkButton.setOnClickListener{
            if(rCount.toInt() >= enterCount.text.toString().toInt()){
                Log.d("UserBooking" , "YOu can book the room ANAKIN")
                val dlg = HotelDialog()
                val b =  Bundle()
                b.putString("title", "Room Avalability! ")
                b.putString("msg" , "Proceed for booking.")
                b.putInt("type" , 2)
                dlg.arguments = b
                dlg.show((activity as FragmentActivity).supportFragmentManager,"2")
                bookButton.visibility = View.VISIBLE
            }else{
                Log.d("UserBooking" , "EnterRoom count is not up to the mark ANAKIN")
                val dlg = HotelDialog()
                val b =  Bundle()
                b.putString("title", "Room Avalability! ")
                b.putString("msg" , "Can't process your request.")
                b.putInt("type" , 2)
                dlg.arguments = b
                dlg.show((activity as FragmentActivity).supportFragmentManager,"2")
            }
        }
        bookButton.visibility = View.INVISIBLE

        bookButton.setOnClickListener{

            viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square, Shape.Circle)
                .addSizes(Size(12))
                .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)

            val updateRoomCount = rCount.toInt() - enterCount.text.toString().toInt()
            Log.d("UserBooking" , "YOu can book the room ANAKIN $updateRoomCount")
            val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("RoomInventory").child(RoomId)
            ref.child("no_of_Room").setValue(updateRoomCount.toString())

            findUserDetails()
        }

        getRoomCount()
    }
    private fun getRoomCount() {
        val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("RoomInventory")
        ref.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot : DataSnapshot) {
                if(snapshot.exists()){
                    for(roomCount in snapshot.children){
                        val roomCountis = roomCount.getValue(roomCountdataClass::class.java)
                        Log.d("UserBooking" , "Room count is ${roomCountis?.no_of_Room} ,${roomCountis?.room_id} , ${RoomId}")
                        if("${roomCountis?.room_id}"== RoomId){
                            Log.d("UserBooking" , "Matched!!!!!!!!!!!!!! , ${roomCountis?.no_of_Room}")
                            rCount = "${roomCountis?.no_of_Room}"
                            room_img = roomCountis?.room_image!!
                            break
                        }
                    }
                }
            }
            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun findUserDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("User_Details")
        ref.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot : DataSnapshot) {
                if(snapshot.exists()){
                    for(user in snapshot.children){
                        val detail_user = user.getValue(findUserdetails::class.java)
                        if(detail_user?.emailR?.lowercase() == auth.currentUser?.email){

                            userUsername = "${detail_user?.usernameR}"

                            userPhoneNumber = "${detail_user?.phoneNumberR}"
                            addOrderDetails()
                            break
                        }

                    }
                }
            }
            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addOrderDetails() {
        val Hotel_id = HotelPK
        val Room_id  = RoomId
        val Email_id = auth.currentUser?.email!!
        val UserName_id = userUsername
        val PhoneNumber = userPhoneNumber
        val OrderValue = (rcost.text.toString().toInt())*(enterCount.text.toString().toInt())

        val primaryKeyOrderDetail = "$Hotel_id+${auth.currentUser?.email!!.replace("@","").replace(".","")}+$Room_id"

        val orderDetailObject = Order_Details(Hotel_id , Room_id ,Email_id ,UserName_id!!,PhoneNumber!!,OrderValue.toString() ,enterCount.text.toString() ,RoomType , room_img , dateTextView.text.toString())

        val ref = db.getReference("Application_Database")
        ref.child("Order_Details").child(primaryKeyOrderDetail).setValue(orderDetailObject)

        val dlg = HotelDialog()
        val b =  Bundle()
        b.putString("title", "Room Booked ")
        b.putString("msg" , "Would you like to check our different hotel also")
        b.putInt("type" , 3)
        dlg.arguments = b
        dlg.show((activity as FragmentActivity).supportFragmentManager,"3")

    }

    override fun onDateSet(view : DatePicker?, year : Int, month : Int, dayOfMonth : Int) {
        Log.d("MainActivity" , "here is the way Date")
        val date = "$dayOfMonth/${month+1}/$year"
        dateTextView.text = date
    }
}
data class roomCountdataClass(
    val no_of_Room : String? = null,
    val room_id : String? = null,
    val room_image : String? = null,
    val room_type : String?= null
)

data class findUserdetails(
    val emailR : String? = null,
    val phoneNumberR : String? = null,
    val usernameR : String? = null
)

class Order_Details(){
    var Hotel_id : String = "" // primary key
    var Room_id : String = ""
    var Email_id : String = ""
    var userName_id : String = ""
    var phoneNumber : String = ""
    var OrderValue : String = ""
    var ordered_Room : String = ""
    var ordered_Room_type : String = ""
    var order_room_image : String = ""
    var order_data : String = ""

    constructor(
        Hotel_id : String,
        Room_id : String,
        Email_id : String,
        userName_id : String,
        phoneNumber : String,
        OrderValue : String,
        ordered_Room : String,
        ordered_Room_type : String,
        order_room_image : String,
        order_data : String,
    ) : this(){
        this.Hotel_id                = Hotel_id
        this.Room_id                = Room_id
        this.Email_id              = Email_id
        this.userName_id          = userName_id
        this.phoneNumber         = phoneNumber
        this.OrderValue         = OrderValue
        this.ordered_Room      = ordered_Room
        this.ordered_Room_type      = ordered_Room_type
        this.order_room_image      = order_room_image
        this.order_data      = order_data
    }
}


