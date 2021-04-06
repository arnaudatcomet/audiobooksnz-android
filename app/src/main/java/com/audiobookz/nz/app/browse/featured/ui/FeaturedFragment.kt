package com.audiobookz.nz.app.browse.featured.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.audiobookz.nz.app.App
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.browse.BrowseFragmentDirections
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.data.resulObservableData
import com.audiobookz.nz.app.databinding.FragmentFeaturedBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_featured.*
import java.util.*
import javax.inject.Inject


class FeaturedFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: FeaturedViewModel
    private var countryCode: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)

        val binding = FragmentFeaturedBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        Log.d("TAG", "network status: "+isConnected)

        getCountryCode()
        subscribeUi(binding)

        if(isConnected){
        viewModel.fetchCategory(countryCode)
        subscribeUi(binding)
        }else{
            binding.progressBar.hide()
            AlertDialogsService(context!!).simple("Internet Problem","Please Check Your Internet");
        }
        return binding.root
    }

    private fun getCountryCode() {
        val tm =
            context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        countryCode = tm.simCountryIso.toUpperCase()

        //get code from wifi
        if (countryCode == "") {
            //countryCode = tm.networkCountryIso.toUpperCase()

            //get code from location
            if (countryCode == "") {
                val locationManager =
                    context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager != null) {

                    //request permission if not granted
                    if (ActivityCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        //Permission was not enabled
                        val permission = arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        //show popup to request permission
                        requestPermissions(permission, 1500)
                    } else {
                        var location =
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (location == null) {
                            location =
                                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        }
                        if (location != null) {
                            var gcd = Geocoder(context, Locale.getDefault())
                            var addresses = gcd.getFromLocation(
                                location.latitude,
                                location.longitude, 1
                            )

                            if (addresses != null && addresses.isNotEmpty()) {
                                countryCode = addresses[0].countryCode.toUpperCase()
                            }
                        }
                        //if location not turn on
                        else {
                            Toast.makeText(activity, "not found your location", Toast.LENGTH_SHORT)
                                .show();3
                        }
                    }
                }
            }
        }
    }

    private fun subscribeUi(binding: FragmentFeaturedBinding) {
       viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
           if(it == true){
               binding.progressBar.hide()
               AlertDialogsService(context!!).simple("Internet Problem","Please Check Your Internet")
           }
       })

        viewModel.featuredListResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {

                    binding.progressBar.hide()
                    result.data.let {
                        val mainAdapter = FeaturedTypeAdapter(result.data!!)
                        mainRecyclerView.adapter = mainAdapter
                    }
                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }
}