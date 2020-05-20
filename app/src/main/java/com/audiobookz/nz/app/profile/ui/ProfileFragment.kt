package com.audiobookz.nz.app.profile.ui

import android.os.AsyncTask
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.browse.di.Injectable
import com.audiobookz.nz.app.browse.di.injectViewModel
import com.audiobookz.nz.app.data.AppDatabase
import com.audiobookz.nz.app.login.data.UserDataDao
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProfileViewModel
    var tempFullname : String? = null
    var tempEmail : String? = null

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

        AsyncTask.execute {
            // Get Data
            viewModel.token =
                activity?.let { AppDatabase.getInstance(it).ProfileDataDao().getAccessToken() }.toString()}
        subscribeUi()

        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        var FaceBtn = view.findViewById<Button>(R.id.btnFacebook)
        var OutBtn = view.findViewById<Button>(R.id.signOut)
        var PlayBtn = view.findViewById<ImageButton>(R.id.imgPlay)
        var TwitterBtn = view.findViewById<Button>(R.id.btnTwitter)
        var BugTxt = view.findViewById<TextView>(R.id.txtBugReport)
        var CustomeCareTxt = view.findViewById<TextView>(R.id.txtEmailCustomer)
        var FAQTxt = view.findViewById<TextView>(R.id.txtfaq)
        var ProfileCard = view.findViewById<CardView>(R.id.CardProfile)
        var NameTxt = view.findViewById<TextView>(R.id.txtProfile_user)
        var EmailTxt = view.findViewById<TextView>(R.id.txtProfile_email)

        NameTxt.setText(tempFullname)
        EmailTxt.setText(tempEmail)

        FaceBtn.setOnClickListener { View ->
            Toast.makeText(getActivity(), "facebook", Toast.LENGTH_SHORT).show()
        }
        TwitterBtn.setOnClickListener { View ->
            Toast.makeText(getActivity(), "twitter", Toast.LENGTH_SHORT).show()
        }
        BugTxt.setOnClickListener { view ->
            Toast.makeText(getActivity(), "bugtext", Toast.LENGTH_SHORT).show()
        }
        CustomeCareTxt.setOnClickListener { view ->
            Toast.makeText(getActivity(), "CustomerCare", Toast.LENGTH_SHORT).show()
        }
        FAQTxt.setOnClickListener { view ->
            Toast.makeText(getActivity(), "FAQ", Toast.LENGTH_SHORT).show()
        }
        OutBtn.setOnClickListener { view ->
            Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show()
        }
        PlayBtn.setOnClickListener { view ->
            Toast.makeText(getActivity(), "play", Toast.LENGTH_SHORT).show()
        }
        ProfileCard.setOnClickListener { view ->
//            var NewFragment: MainActivity = activity as MainActivity
//            NewFragment.ChangeToEditProfileFragment()
        }

    }

    companion object {
        fun newInstance(): ProfileFragment =
            ProfileFragment()
    }

    private fun subscribeUi() {
        viewModel.showProfile?.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {

                    tempEmail = result.data?.email
                    tempFullname = result.data?.full_name

                }
                Result.Status.LOADING -> Log.d("TAG", "loading")
                Result.Status.ERROR -> {
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show();
                }
            }
        })
    }
}
