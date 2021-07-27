package com.example.ANDSProject.model

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.ANDSProject.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EmployeeFragment : Fragment() {

    lateinit var employeeName : String

    lateinit var empHN: TextView
    lateinit var empHDesc : TextView
    lateinit var empHLoc : TextView
    lateinit var empHS : TextView
    lateinit var empName : TextView
    lateinit var logout: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        employeeName  =arguments?.getString("employeeName")!!

        empHN  = view.findViewById(R.id.empHName)
        empHDesc  = view.findViewById(R.id.empHDesc)
        empHLoc  = view.findViewById(R.id.empHLoc)
        empHS  = view.findViewById(R.id.empHStatus)
        empName  = view.findViewById(R.id.empName)
        logout = view.findViewById(R.id.LogoutEmployee)
        logout.setOnClickListener{
            findNavController().navigate(R.id.action_employeeFragment_to_loginFragment)
        }


        getEmployeeDetails()
    }

    private fun getEmployeeDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("Application_Database").child("Hotel_Details")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot : DataSnapshot) {
                if(snapshot.exists()){
                    for(emp in snapshot.children){
                        val employee = emp.getValue(Employee_Holder::class.java)
                        Log.d("LoginActivitgy" , "employeeName11${employee?.Hotel_Employee}")
                        if(employeeName.lowercase() == employee?.Hotel_Employee?.lowercase() ){
                            Log.d("LoginActivitgy" , "employeeName${employee.Hotel_Employee}")
                            empHN.text = employee.Hotel_ID
                            empHDesc.text = employee.Hotel_Description
                            empHLoc.text = employee.Hotel_Location
                            empHS.text = employee.Hotel_isActive
                            empName.text = employee.Hotel_Employee
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
}

data class Employee_Holder(
    var Hotel_ID : String = "",
    var Hotel_Location : String = "",
    var Hotel_Description : String = "",
    var Hotel_isActive : String = "",
    var Hotel_Employee : String = "",
)