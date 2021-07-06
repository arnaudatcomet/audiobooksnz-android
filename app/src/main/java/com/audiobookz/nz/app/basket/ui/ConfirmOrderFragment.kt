package com.audiobookz.nz.app.basket.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.AsyncTask
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.bookdetail.data.BookRoom
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentConfirmOrderBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.more.data.CardListData
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.google.android.material.snackbar.Snackbar
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class ConfirmOrderFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BasketViewModel
    private val args: ConfirmOrderFragmentArgs by navArgs()
    private lateinit var bookListProduct: ArrayList<BookRoom>
    private var orderId: Int? = null
    private var countryCode: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentConfirmOrderBinding.inflate(inflater, container, false)
        val adapter = OrderAdapter()
        binding.orderRecycleView.adapter = adapter
        binding.proceedPay = clickPay()
        getCountryCode()
        subscribeUi(binding, adapter)
        return binding.root
    }

    private fun clickPay(): View.OnClickListener {
        return View.OnClickListener {
            if (orderId != null)
                viewModel.getLocalCard()
        }
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

    private fun subscribeUi(binding: FragmentConfirmOrderBinding, adapter: OrderAdapter) {
        viewModel.basketResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    result.data?.let { adapter.submitList(it) }
                    if (result.data?.isNotEmpty()!!) {
                        bookListProduct = result.data as ArrayList<BookRoom>
                        binding.subTotalLinear.visibility = View.VISIBLE
                        if (countryCode != "") {
                            viewModel.orderBookList(bookListProduct, countryCode, args.coupon)
                        } else {
                            findNavController().popBackStack()
                        }

                    } else {
                        binding.subTotalLinear.visibility = View.GONE
                    }
                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

        viewModel.resultOrder.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    orderId = result.data?.id
                    binding.subTotalPriceOrderTxt.text = result.data?.subtotal.toString()
                    binding.totalPriceOrderTxt.text = result.data?.total.toString()
                    viewModel.getCredits()
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Toast.makeText(activity, "Please Check Your Coupon", Toast.LENGTH_SHORT)
                        .show();3
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT)
                        .show();3
                    findNavController().popBackStack()
                }
            }
        })

        viewModel.resultPayment.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (result.data != null) {
                        when (result.data.state) {
                            "pending" -> {

                            }
                            "completed" -> {
                                viewModel.statusNotification(
                                    "Payment Status",
                                    " Your Payment is successful"
                                )

                                viewModel.showPurchaseBook(orderId!!)
                                AsyncTask.execute {
                                    viewModel.deleteCartAll()
                                }
                                val intent = Intent(activity, MainActivity::class.java).setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                                )
                                startActivity(intent)
                                //never go back if done
                                activity?.finish()
                            }
                            else -> {
                                Toast.makeText(
                                    activity,
                                    "Payment Status " + result.data.msg,
                                    Toast.LENGTH_SHORT
                                ).show();3
                            }
                        }

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

        viewModel.resultCheckCredit.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (result.data != null) {
                        binding.orderCreditsTxt.text =
                            "Use Box Credits Box(${result.data.credit_count} available)"
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
//                    var localCard = result.data?.get("local") as List<CardData>

                    if (cloudCard.card?.size != 0) {

                        if (binding.useCreditBox.isChecked) {
                            viewModel.orderCheckout(orderId!!, "1", cloudCard.default!!)
                        } else {
                            viewModel.orderCheckout(orderId!!, "0", cloudCard.default!!)
                        }

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
