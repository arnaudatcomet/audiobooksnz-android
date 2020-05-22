package com.audiobookz.nz.app.profile.ui

import android.app.Activity.RESULT_OK
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.io.File
import javax.inject.Inject


class EditProfileFragment : Fragment() , Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: EditProfileViewModel
    private val PICK_IMAGE = 100
    private val TAKE_IMAGE= 1
    var imageUri: Uri? = null
    var imageFile: File? = null
    var SaveChangeBtn: Button? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = injectViewModel(viewModelFactory)
       return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var accessToken: String? = null
        subscribeUi()
        var EditProfileCard = view.findViewById<CardView>(R.id.CardEditProfile)
        var FirstNameEdit = view.findViewById<TextView>(R.id.editFirstNameProfile)
        var LastNameEdit = view.findViewById<TextView>(R.id.editLastNameProfile)
        var CurrentPassEdit = view.findViewById<TextView>(R.id.editCurrentPass)
        var NewPassProfileEdit = view.findViewById<TextView>(R.id.editNewPassProfile)
        var PassConfirmProfileEdit = view.findViewById<TextView>(R.id.editPassConfirmProfile)
        var SaveChangeBtn = view.findViewById<Button>(R.id.btnSaveChange)
        var profileImage = view.findViewById<ImageView>(R.id.profileImage)
        val items = arrayOf("Camera", "Gallery")

        val msg: String =
            FirstNameEdit.text.toString() + " " + LastNameEdit.text.toString()
        val msg2: String =
            CurrentPassEdit.text.toString() + " " + NewPassProfileEdit.text.toString() + " " + PassConfirmProfileEdit.text.toString()
        val msg3 = "$msg $msg2"
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        val takePicture= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        EditProfileCard.setOnClickListener{view ->
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.PleaseSelectImage))
                .setNeutralButton(resources.getString(R.string.AlertCancel)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setItems(items) { dialog, which ->
                   if (items[which] == items[0]){
                       startActivityForResult(takePicture, TAKE_IMAGE)

                   }
                    else{
                       startActivityForResult(gallery, PICK_IMAGE)
                   }
                }
                .show()
        }

        SaveChangeBtn.setOnClickListener{view ->
            Toast.makeText(getActivity(), msg3, Toast.LENGTH_SHORT).show()
            viewModel.editProfile("Bearer K9FioHyGOjWT1rjEiwjLIuWU34M8C1bJ",
                imageUri?.let { getRealPathFromURI(it) }.toString()
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && (requestCode == PICK_IMAGE )){
            imageUri = data?.getData();
            profileImage.setImageURI(imageUri);
        }else if(resultCode == TAKE_IMAGE) {
//            val file =
//                File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg")
//
//            //Uri of camera image
//            //Uri of camera image
//            val uri = getActivity()?.let {
//                FileProvider.getUriForFile(
//                    it,
//                    activity?.getPackageName().toString() + ".provider",
//                    file
//                )
//            }
//            profileImage.setImageURI(uri);

        }
    }




    private fun getRealPathFromURI(contentURI: Uri): String? {
        var result: String? = null
        val cursor: Cursor? = activity?.contentResolver?.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            if (cursor.moveToFirst()) {
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
            }
            cursor.close()
        }
        return result
    }

    companion object {
        fun newInstance(): EditProfileFragment =
            EditProfileFragment()
    }
    private fun subscribeUi() {
        viewModel.editProfileResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    SaveChangeBtn?.setText("Login")
                    val intent = Intent(activity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                Result.Status.LOADING ->  SaveChangeBtn?.setText("Loading")
                Result.Status.ERROR -> {
                    SaveChangeBtn?.setText("Login")
                    Toast.makeText(getActivity(),result.message ,Toast.LENGTH_SHORT).show();3
                }
            }
        })
    }

}

