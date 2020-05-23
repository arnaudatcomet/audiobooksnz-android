package com.audiobookz.nz.app.profile.ui

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.data.AppDatabase
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class EditProfileFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProfileViewModel
    var ProfileCard : ImageView? = null
    var FirstNameEdit : TextView?= null
    var LastNameEdit: TextView?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = injectViewModel(viewModelFactory)

        viewModel.token = AsyncTask.execute {
            "Bearer " + activity?.let {
                AppDatabase.getInstance(
                    it
                ).userDataDao().getAccessToken()
            }
        }.toString()

        subscribeUi()
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var EditProfileCard = view.findViewById<CardView>(R.id.CardEditProfile)
        ProfileCard = view.findViewById(R.id.ImgProfileEdit2)
        FirstNameEdit = view.findViewById(R.id.editFirstNameProfile)
        LastNameEdit = view.findViewById(R.id.editLastNameProfile)
        var CurrentPassEdit = view.findViewById<EditText>(R.id.editCurrentPass)
        var NewPassProfileEdit = view.findViewById<EditText>(R.id.editNewPassProfile)
        var PassConfirmProfileEdit = view.findViewById<EditText>(R.id.editPassConfirmProfile)
        var SaveChangeBtn = view.findViewById<Button>(R.id.btnSaveChange)
        val items = arrayOf("Camera", "Gallery")


        val msg2: String =
            CurrentPassEdit.text.toString() + " " + NewPassProfileEdit.text.toString() + " " + PassConfirmProfileEdit.text.toString()

        EditProfileCard.setOnClickListener { view ->
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.PleaseSelectImage))
                .setNeutralButton(resources.getString(R.string.AlertCancel)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setItems(items) { dialog, which ->
                    if (items[which] == items[0]) {

                    } else {

                    }
                }
                .show()
        }

        SaveChangeBtn.setOnClickListener { view ->
            Toast.makeText(getActivity(), msg2, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(): EditProfileFragment =
            EditProfileFragment()
    }

    private fun subscribeUi() {
        viewModel.queryProfile?.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {

                    FirstNameEdit?.text = result.data?.full_name
                    LastNameEdit?.text = result.data?.last_name

                    ProfileCard?.let {
                        Glide.with(this)
                            .load(result.data?.image_url)
                            .into(it)
                    }


                }
                Result.Status.LOADING -> Log.d("TAG", "loading")
                Result.Status.ERROR -> {
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show();
                }
            }
        })
    }

}

