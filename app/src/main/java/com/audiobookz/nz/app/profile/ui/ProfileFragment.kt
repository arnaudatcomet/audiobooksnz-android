package com.audiobookz.nz.app.profile.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.SplashScreenActivity
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.browse.BrowseFragmentDirections
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.player.ui.PlayerActivity
import com.bumptech.glide.Glide
import javax.inject.Inject


class ProfileFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: EditProfileViewModel
    var fullNameTxt: TextView? = null
    var emailTxt: TextView? = null
    var profileImg: ImageView? = null
    var creditTxt: TextView? = null
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

        subscribeUi()

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var faceBtn = view.findViewById<Button>(R.id.btnFacebook)
        var outBtn = view.findViewById<Button>(R.id.signOut)
        var playBtn = view.findViewById<ImageButton>(R.id.imgPlay)
        var twitterBtn = view.findViewById<Button>(R.id.btnTwitter)
        var bugTxt = view.findViewById<TextView>(R.id.txtBugReport)
        var customerCareTxt = view.findViewById<TextView>(R.id.txtEmailCustomer)
        var faqTxt = view.findViewById<TextView>(R.id.txtfaq)
        var profileCard = view.findViewById<CardView>(R.id.CardProfile)
        emailTxt = view.findViewById(R.id.txtProfile_email)
        fullNameTxt = view.findViewById(R.id.txtProfile_user)
        profileImg = view.findViewById(R.id.imgProfile1)
        creditTxt = view.findViewById(R.id.txtBookCreditsValue)

        faceBtn.setOnClickListener { View ->
            val navController = Navigation.findNavController(view!!)
            navController.navigate(ProfileFragmentDirections.actionMeToWebViewProfileFragment("Facebook"))
        }
        twitterBtn.setOnClickListener { View ->
            val navController = Navigation.findNavController(view!!)
            navController.navigate(ProfileFragmentDirections.actionMeToWebViewProfileFragment("Twitter"))
        }
        bugTxt.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:appbugs@audiobooksnz.co.nz" + "?subject=Bug Report")
            startActivity(intent)

        }
        customerCareTxt.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:support@audiobooksnz.co.nz" + "?subject=Customers Care")
            startActivity(intent)
        }
        faqTxt.setOnClickListener {
            val navController = Navigation.findNavController(view!!)
            navController.navigate(ProfileFragmentDirections.actionMeToWebViewProfileFragment("FAQ"))
        }

        outBtn.setOnClickListener {

            viewModel.destroyProfile

            val intent = Intent(
                activity,
                SplashScreenActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            //never go back if done
            activity?.finish()
        }

        playBtn.setOnClickListener {

            var bookDetail = viewModel?.getMultiValueCurretBook
            if (bookDetail != null) {
                val intent = Intent(activity, PlayerActivity::class.java).apply {
                    putExtra("contentId", bookDetail[0])
                    putExtra("licenseIDBook", bookDetail[1])
                    putExtra("cloudBookId", bookDetail[2])
                    putExtra("titleBook", bookDetail[3])
                    putExtra("urlImage", bookDetail[4])
                    putExtra("bookId", bookDetail[5])
                    putExtra("authorBook", bookDetail[6])
                    putExtra("narratorBook", bookDetail[7])
                }
                startActivity(intent)
            }
        }

        profileCard.setOnClickListener {
            val direction = ProfileFragmentDirections.actionMeToEditProfileFragment()
            view.findNavController().navigate(direction)
        }

    }

    private fun subscribeUi() {
        viewModel.queryProfile?.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    fullNameTxt?.text = result.data?.full_name
                    emailTxt?.text = result.data?.email
                    profileImg?.let {
                        Glide.with(this)
                            .load(result.data?.image_url)
                            .into(it)
                    }
                    if (result.data?.credit_count != null) {
                        creditTxt?.text = result.data?.credit_count.toString()
                    }
                }
                Result.Status.LOADING -> Log.d("TAG", "loading")
                Result.Status.ERROR -> {
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) };
                }
            }
        })
        viewModel.queryProfile?.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    fullNameTxt?.text = result.data?.full_name
                    emailTxt?.text = result.data?.email
                    profileImg?.let {
                        Glide.with(this)
                            .load(result.data?.image_url)
                            .into(it)
                    }
                }
                Result.Status.LOADING -> Log.d("TAG", "loading")
                Result.Status.ERROR -> {
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) };
                }
            }
        })
    }

    //binding fragment
    companion object {
        fun newInstance(): ProfileFragment =
            ProfileFragment()
    }

}
