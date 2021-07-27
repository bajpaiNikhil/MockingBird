package com.example.ANDSProject.model

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.example.ANDSProject.R
import java.util.*

class HotelDialog : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        lateinit var myDlg : Dialog

        val parent = activity as FragmentActivity
        val args = arguments


        val mytitle = args?.getString("title")
        val message = args?.getString("msg")
        val dlgType = args?.getInt("type")

        val builder = AlertDialog.Builder(parent)
        when(dlgType){

            1 ->{
                //do something for date dialog
                val cal = Calendar.getInstance()
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)
                val day = cal.get(Calendar.DAY_OF_MONTH)
                val dateSetListener = targetFragment as OnDateSetListener? // getting passed fragment

                myDlg = DatePickerDialog(requireContext(), dateSetListener, year, month, day)

            }

            2 ->{
                //do somethings for pop up dialog

                builder.setTitle(mytitle)
                builder.setMessage(message)

                builder.setPositiveButton("YEs" , DialogInterface.OnClickListener { dialog, which ->


                    dialog.cancel()
                })

                builder.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                })

                builder.setIcon(R.drawable.ic_launcher_foreground)
                myDlg = builder.create()

            }

            3 ->{

                builder.setTitle(mytitle)
                builder.setMessage(message)

                builder.setPositiveButton("YEs" , DialogInterface.OnClickListener { dialog, which ->
                    findNavController().navigate(R.id.userHotelListFragment)
                })

                builder.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
                    findNavController().navigate(R.id.userHotelListFragment)
                })

                builder.setIcon(R.drawable.ic_launcher_foreground)
                myDlg = builder.create()

            }

        }
        return myDlg
    }
}