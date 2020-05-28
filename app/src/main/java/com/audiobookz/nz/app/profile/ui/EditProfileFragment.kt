package com.audiobookz.nz.app.profile.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.content.contentValuesOf
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
import com.bumptech.glide.Glide


class EditProfileFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: EditProfileViewModel

    private val PICK_IMAGE = 100
    private val PERMISSION_CODE = 1000;
    private val TAKE_IMAGE = 1
    var imageUri: Uri? = null
    var imageFile: File? = null
    var SaveChangeBtn: Button? = null
    var ProfileCard: ImageView? = null
    var FirstNameEdit: TextView? = null
    var LastNameEdit: TextView? = null

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

        GetInfoSubscribeUi()
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

        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        PutProfileSubscribeUi()

        EditProfileCard.setOnClickListener { view ->
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.PleaseSelectImage))
                .setNeutralButton(resources.getString(R.string.AlertCancel)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setItems(items) { dialog, which ->
                    if (items[which] == items[0]) {
                        // startActivityForResult(takePicture, TAKE_IMAGE)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getActivity()?.let {
                                    checkSelfPermission(
                                        it,
                                        Manifest.permission.CAMERA
                                    )
                                }
                                == PackageManager.PERMISSION_DENIED ||
                                getActivity()?.let {
                                    checkSelfPermission(
                                        it,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    )
                                }
                                == PackageManager.PERMISSION_DENIED) {
                                //Permission was not enabled
                                val permission = arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                                //show popup to request permission
                                requestPermissions(permission, PERMISSION_CODE)


                            } else {
                                //permission already granted
                                takeImage()

                            }
                        } else {
                            //system os is < marshmallow
                            takeImage()

                        }
                    } else {
                        startActivityForResult(gallery, PICK_IMAGE)
                    }
                }
                .show()
        }

        SaveChangeBtn.setOnClickListener { view ->
            if (CurrentPassEdit.text.isEmpty() || NewPassProfileEdit.text.isEmpty() || PassConfirmProfileEdit.text.isEmpty()) {
                Toast.makeText(activity, "Password is blank", Toast.LENGTH_SHORT).show();
            } else {

                viewModel.editProfile(

                    imageUri?.let { getRealPathFromURI(it) }.toString(),
                    FirstNameEdit?.text.toString(),
                    LastNameEdit?.text.toString(),
                    CurrentPassEdit.text.toString(),
                    NewPassProfileEdit.text.toString(),
                    PassConfirmProfileEdit.text.toString()
                )

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_IMAGE -> {
                imageUri = data?.getData();
                ProfileCard?.setImageURI(imageUri);
            }
            TAKE_IMAGE -> {
                ProfileCard?.setImageURI(imageUri);
            }
        }
    }

    private fun takeImage() {
        val value = contentValuesOf()
        value.put(MediaStore.Images.Media.TITLE, "New Picture")
        value.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri =
            activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        TAKE_IMAGE?.let { startActivityForResult(cameraIntent, it) }
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

    private fun PutProfileSubscribeUi() {
        viewModel.editProfileResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    SaveChangeBtn?.setText("Login")
                    val intent = Intent(
                        activity,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                Result.Status.LOADING -> SaveChangeBtn?.setText("Loading")
                Result.Status.ERROR -> {
                    SaveChangeBtn?.setText("Login")
                    Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();3
                }
            }
        })
    }

    private fun GetInfoSubscribeUi() {
        viewModel.queryProfile?.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {

                    FirstNameEdit?.text = result.data?.first_name
                    LastNameEdit?.text = result.data?.last_name

                    ProfileCard?.let {
                        Glide.with(this)
                            .load(result.data?.image_url)
                            .into(it)
                    }


                }
                Result.Status.LOADING -> Log.d("TAG", "loading")
                Result.Status.ERROR -> {
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


}

