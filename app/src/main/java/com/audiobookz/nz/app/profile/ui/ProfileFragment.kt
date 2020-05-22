package com.audiobookz.nz.app.profile.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.data.AppDatabase
import com.audiobookz.nz.app.data.Result
import javax.inject.Inject
import com.audiobookz.nz.app.SplashScreenActivity
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.login.data.UserDataDao

class ProfileFragment : Fragment() , Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProfileViewModel
    var fullnameTxt: TextView? =null
    var emailTxt: TextView? =null


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
        var accessToken: String? = null

        viewModel.token = AsyncTask.execute{
            accessToken = "Bearer "+ activity?.let {
                AppDatabase.getInstance(
                    it
                ).userDataDao().getAccessToken()
            }}.toString()

        subscribeUi()
        return inflater.inflate(R.layout.fragment_profile, container, false)}


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
        emailTxt = view.findViewById(R.id.txtProfile_email)
        fullnameTxt = view.findViewById(R.id.txtProfile_user)

        FaceBtn.setOnClickListener { View ->
            Toast.makeText(getActivity(), "facebook", Toast.LENGTH_SHORT).show()
        }
        TwitterBtn.setOnClickListener { View ->
            Toast.makeText(getActivity(), "twitter", Toast.LENGTH_SHORT).show()
        }
        BugTxt.setOnClickListener{view ->
            Toast.makeText(getActivity(), "bugtext", Toast.LENGTH_SHORT).show()
        }
        CustomeCareTxt.setOnClickListener{view ->
            Toast.makeText(getActivity(), "CustomerCare", Toast.LENGTH_SHORT).show()
        }
        FAQTxt.setOnClickListener{view ->
            Toast.makeText(getActivity(), "FAQ", Toast.LENGTH_SHORT).show()
        }
        OutBtn.setOnClickListener{view ->
            AsyncTask.execute {
            getActivity()?.let {
                AppDatabase.getInstance(
                    it
                ).userDataDao().logout()
            }
                }
            val intent = Intent(activity, SplashScreenActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        PlayBtn.setOnClickListener{view ->
            Toast.makeText(getActivity(), "play", Toast.LENGTH_SHORT).show()
        }
        ProfileCard.setOnClickListener{view ->
            var NewFragment : MainActivity = activity as MainActivity
            NewFragment.ChangeToEditProfileFragment()
        }

    }

    private fun subscribeUi() {
        viewModel.queryProfile?.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {

                    fullnameTxt?.text = result.data?.full_name
                    emailTxt?.text = result.data?.email

                }
                Result.Status.LOADING -> Log.d("TAG", "loading")
                Result.Status.ERROR -> {
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show();
                }
            }
        })
    }

    companion object {
        fun newInstance(): ProfileFragment =
            ProfileFragment()
    }

}
