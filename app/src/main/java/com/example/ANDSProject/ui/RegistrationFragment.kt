package com.example.ANDSProject.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ANDSProject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {
    lateinit var auth : FirebaseAuth

    lateinit var emailR: EditText
    lateinit var passwordR: EditText
    lateinit var usernameR: EditText
    lateinit var phoneNumberR: EditText
    lateinit var registerToLogin : TextView
    lateinit var db : FirebaseDatabase
    lateinit var register:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailR = view.findViewById(R.id.emailL)
        passwordR  =view.findViewById(R.id.passL)
        usernameR = view.findViewById(R.id.usernameR)
        phoneNumberR = view.findViewById(R.id.phoneR)
        registerToLogin  = view.findViewById(R.id.RegisterToLogin)
        register = view.findViewById(R.id.RegisterB)

        auth = FirebaseAuth.getInstance()
        db = Firebase.database
        registerToLogin.setOnClickListener {
           findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
        register.setOnClickListener{
            Log.d("this","Reached till here")
            val userName = usernameR.text.toString()
            val eMail = emailR.text.toString()
            val passWord = passwordR.text.toString()
            val phoneNumber = phoneNumberR.text.toString()
            if(userName.isEmpty()){
                usernameR.setError("PLEASE ENTER YOUR USERNAME")
            }
            else if(eMail.isEmpty()){
                emailR.setError("PLEASE ENTER YOUR EMAIL")
            }
            else if(passWord.length < 8){
                passwordR.setError("MINIMUM 8 CHARACTERS REQUIRED")
            }
            else if(phoneNumber.length < 1 && phoneNumber.length > 10){
                phoneNumberR.setError("10 DIGIT PHONE NUMBER REQUIRED")
            }
            else{
                auth.createUserWithEmailAndPassword( eMail , passWord)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            addUserInDb()
                            Toast.makeText(context,"Registration Complete" , Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
//                            activity?.onBackPressed()

                        }
                        else{
                            Toast.makeText(context,"Registration failed" , Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
    private fun addUserInDb() {
        val userName = usernameR.text.toString()
        val email = "${emailR.text}"
        //val password = passwordR.text.toString()
        val phoneNumber = phoneNumberR.text.toString()

        val UserObj = UserDetail(email,userName,phoneNumber)
        val ref = FirebaseDatabase.getInstance().getReference("Application_Database")
        ref.child("User_Details").child(userName).setValue(UserObj)
        Log.d("this is the way" , " Reached till here${ref}")

    }
}

class UserDetail() {
    var emailR : String = "" // primary key
    var usernameR : String = ""
    var phoneNumberR : String = ""
    constructor(
        emailR : String,
        usernameR : String,
        phoneNumberR : String,
    ) : this(){
        this.emailR             = emailR
        this.usernameR         = usernameR
        this.phoneNumberR      = phoneNumberR
    }

}