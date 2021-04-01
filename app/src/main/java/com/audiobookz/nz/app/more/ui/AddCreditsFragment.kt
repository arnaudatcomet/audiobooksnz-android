package com.audiobookz.nz.app.more.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentAddCreditsBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.more.data.CardData
import com.audiobookz.nz.app.more.data.CardListData
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar
import java.util.*
import javax.inject.Inject

class AddCreditsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MoreViewModel
    private var tempOrderId: Int? = null
    private var countryCode: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentAddCreditsBinding.inflate(inflater, container, false)
        binding.buyNow = clickBuyCredits(binding)
        getCountryCode()
        subscribeUi(binding)
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

    private fun clickBuyCredits(binding: FragmentAddCreditsBinding): View.OnClickListener {
        return View.OnClickListener {
            var credits = binding.creditEditText.text.toString()

            if (credits.isNotBlank()) {
                if (countryCode != "") {
                    viewModel.buyCredits(credits, countryCode)
                } else {
                    getCountryCode()
                    if (countryCode != "") {
                        viewModel.buyCredits(credits, countryCode)
                    } else {
                        Toast.makeText(activity, "not found your location", Toast.LENGTH_SHORT)
                            .show();3
                    }
                }

            } else {
                Toast.makeText(activity, "field is blank", Toast.LENGTH_SHORT).show();3
            }
        }
    }

    private fun subscribeUi(binding: FragmentAddCreditsBinding) {
        viewModel.resultBuyCredits.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    tempOrderId = result.data?.id
                    viewModel.getLocalCard()
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    if (result.message == "Network :  400 Bad Request") {
                        Toast.makeText(
                            activity,
                            "Subscribe to buy more book credits (BC)",
                            Toast.LENGTH_SHORT
                        ).show();3
                    } else {
                        result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                    }
                }
            }
        })

        viewModel.resultPayment.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (result.data != null) {
                        viewModel.statusNotification(
                            "Payment Status",
                            " Your Payment is successful"
                        )
                        val intent = Intent(
                            activity,
                            MainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        //never go back if done
                        activity?.finish()
                    }
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

        viewModel.resultLocalCardList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    viewModel.getCardList(result.data)
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        viewModel.resultGetCardList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    var cloudCard = result.data?.get("cloud") as CardListData
                    //var localCard = result.data?.get("local") as List<CardData>

                    if (cloudCard.card?.size != 0) {
                        viewModel.orderCheckout(tempOrderId!!, "0", cloudCard.default!!)

                    } else {
                        binding.progressBar.hide()
                        AlertDialogsService(context!!).simple("Warning", "Payments Method is Empty")
                    }
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })
    }

}
