package com.example.ANDSProject.ui

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

class LoginFragment : Fragment() {

    lateinit var auth : FirebaseAuth
    lateinit var emailL: EditText
    lateinit var passL: EditText
    lateinit var registerHereL: TextView
    lateinit var login:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        emailL = view.findViewById(R.id.emailL)
        passL = view.findViewById(R.id.passL)
        registerHereL = view.findViewById(R.id.RegisterhereL)
        login = view.findViewById(R.id.LoginB)

        registerHereL.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        login.setOnClickListener{
            val eMail = emailL.text.toString()
            val passWord = passL.text.toString()

            if(eMail.length < 7){
                emailL.setError("PLEASE ENTER VALID EMAIL...")
            }
            else if(passWord.isEmpty()){
                passL.setError("PLEASE ENTER PASSWORD..")
            }
            else if(eMail.isNotEmpty() && passWord.isNotEmpty())
            {
                auth.signInWithEmailAndPassword(eMail , passWord)
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                        {
                            val emailSplit = emailL.text.toString().split("@")
                            Log.d("LoginActivitgy" , "Emailis ${emailSplit} ,${emailSplit[1]}")
                            Toast.makeText(context, "Login Complete", Toast.LENGTH_LONG).show()
                            if (emailL.text.toString() == "admin@admin123.com" && passL.text.toString() == "adminpassword")
                            {
                                Toast.makeText(context, "Welcome Admin", Toast.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_loginFragment_to_adminRecyclerViewFragment)
//                                activity?.onBackPressed()
                            }
                            else if(emailSplit[1] == "employee.com"){
                                Log.d("LoginActivitgy" , "Emailis ${emailSplit[0]}")
                                Log.d("LoginActivitgy" , "Emailis ${emailSplit} ,${emailSplit[1]} ")
                                val b = bundleOf("employeeName" to emailSplit[0])
                                findNavController().navigate(R.id.action_loginFragment_to_employeeFragment, b)

                            }
                            else
                            {
                                Toast.makeText(context, "Welcome Customer", Toast.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_loginFragment_to_userHotelListFragment)

                            }
//                            activity?.onBackPressed()
                        }
                        else {
                            Toast.makeText(context, "Something wrong...", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                Toast.makeText(context, "Please Enter Details", Toast.LENGTH_LONG).show()
            }
        }

    }

}