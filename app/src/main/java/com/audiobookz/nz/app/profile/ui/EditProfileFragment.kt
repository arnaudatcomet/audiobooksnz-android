package com.audiobookz.nz.app.profile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView

import com.audiobookz.nz.app.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_edit_profile, container, false)

    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var EditProfileCard = view.findViewById<CardView>(R.id.CardEditProfile)
        var FirstNameEdit = view.findViewById<TextView>(R.id.editFirstNameProfile)
        var LastNameEdit = view.findViewById<TextView>(R.id.editLastNameProfile)
        var CurrentPassEdit = view.findViewById<TextView>(R.id.editCurrentPass)
        var NewPassProfileEdit = view.findViewById<TextView>(R.id.editNewPassProfile)
        var PassConfirmProfileEdit = view.findViewById<TextView>(R.id.editPassConfirmProfile)
        var SaveChangeBtn = view.findViewById<Button>(R.id.btnSaveChange)
        val items = arrayOf("Camera", "Gallery")

        val msg: String =
            FirstNameEdit.text.toString() + " " + LastNameEdit.text.toString()
        val msg2: String =
            CurrentPassEdit.text.toString() + " " + NewPassProfileEdit.text.toString() + " " + PassConfirmProfileEdit.text.toString()
        val msg3 = "$msg $msg2"

        EditProfileCard.setOnClickListener{view ->
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.PleaseSelectImage))
                .setNeutralButton(resources.getString(R.string.AlertCancel)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setItems(items) { dialog, which ->
                   if (items[which] == items[0]){

                   }
                    else{

                   }
                }
                .show()
        }

        SaveChangeBtn.setOnClickListener{view ->
            Toast.makeText(getActivity(), msg3, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(): EditProfileFragment =
            EditProfileFragment()
    }

}

