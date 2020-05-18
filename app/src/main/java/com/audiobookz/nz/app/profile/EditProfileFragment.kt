package com.audiobookz.nz.app.profile

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
        var FirstNameEdit = view.findViewById<TextView>(R.id.editFirstName)
        var LastNameEdit = view.findViewById<TextView>(R.id.editLastName)
        var CurrentPassEdit = view.findViewById<TextView>(R.id.editCurrentPass)
        var NewPassProfileEdit = view.findViewById<TextView>(R.id.editNewPassProfile)
        var PassConfirmProfileEdit = view.findViewById<TextView>(R.id.editPassConfirmProfile)
        var SaveChangeBtn = view.findViewById<Button>(R.id.btnSaveChange)

        EditProfileCard.setOnClickListener { View ->
            Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show()
        }
        FirstNameEdit.setOnClickListener { View ->
            Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show()
        }
        LastNameEdit.setOnClickListener{view ->
            Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show()
        }
        CurrentPassEdit.setOnClickListener{view ->
            Toast.makeText(getActivity(), "4", Toast.LENGTH_SHORT).show()
        }
        NewPassProfileEdit.setOnClickListener{view ->
            Toast.makeText(getActivity(), "5", Toast.LENGTH_SHORT).show()
        }
        PassConfirmProfileEdit.setOnClickListener{view ->
            Toast.makeText(getActivity(), "6", Toast.LENGTH_SHORT).show()
        }
        SaveChangeBtn.setOnClickListener{view ->
            Toast.makeText(getActivity(), "7", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(): EditProfileFragment = EditProfileFragment()
    }

}

