package com.audiobookz.nz.app.audiobookList.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.audiobookList.data.Audiobook
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentAudiobookListBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.setTitle
import com.audiobookz.nz.app.ui.show
import com.audiobookz.nz.app.util.AUDIOBOOKLIST_PAGE_SIZE
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import com.audiobookz.nz.app.util.CLOUDBOOK_PAGE_SIZE
import com.google.android.material.snackbar.Snackbar
import java.util.*
import javax.inject.Inject


class AudiobookListFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AudiobookListViewModel
    private val args: AudiobookListFragmentArgs by navArgs()
    lateinit var spinnerLang: Spinner
    var defaultLang: String = "English"
    private val adapter = AudiobookListAdapter()
    lateinit var binding: FragmentAudiobookListBinding
    private lateinit var fragmentStatus: String
    private var countryCode: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentStatus = "onAttach"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (fragmentStatus == "onAttach") {
            fragmentStatus = "onViewCreated"

            args.titleList?.let { setTitle(it) }
            viewModel = injectViewModel(viewModelFactory)
            binding = FragmentAudiobookListBinding.inflate(inflater, container, false)
            context ?: return binding.root

            //set up selectLangDialog
            activity?.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.Languages,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.selectLangDialog.adapter = adapter
                }
            }

            //check orientation
            val currentOrientation = resources.configuration.orientation
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.recyclerViewCatecoryDetail.layoutManager =
                    GridLayoutManager(context, 5, GridLayoutManager.VERTICAL, false)
                binding.recyclerViewCatecoryDetail.adapter = adapter
            } else {
                binding.recyclerViewCatecoryDetail.layoutManager =
                    GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                binding.recyclerViewCatecoryDetail.adapter = adapter
            }

            getCountryCode()
            //first page fetch
            when {
                //open by showAll
                args.listItem != null -> {
                    binding.progressBarDetail.hide()
                    binding.selectLangDialog.show()
                    adapter.submitList(args.listItem!!.map { featured -> featured.audiobook })
                }
                //open by category
                args.id != 0 -> {
                    binding.selectLangDialog.show()
                    viewModel.fetchCategory(
                        args.id,
                        1,
                        AUDIOBOOKLIST_PAGE_SIZE,
                        defaultLang,
                        countryCode
                    )
                }
                //open by search
                else -> {
                    args.keyword?.let {
                        viewModel.fetchSearch(
                            it,
                            1,
                            AUDIOBOOKLIST_PAGE_SIZE,
                            countryCode
                        )
                    }
                }
            }

            //paging
            binding.recyclerViewCatecoryDetail.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && viewModel.isLatest == false) {

                        when {
                            //open by showAll
//                            args.listItem != null -> {
//                                binding.progressBarDetail.hide()
//                                binding.selectLangDialog.show()
//                                adapter.submitList(args.listItem!!.map { featured -> featured.audiobook })
//                            }
                            //open by category
                            args.id != 0 -> {
                                binding.selectLangDialog.show()
                                viewModel.pageCount?.plus(1)?.let {
                                    viewModel.fetchCategory(
                                        args.id,
                                        it, AUDIOBOOKLIST_PAGE_SIZE, defaultLang, countryCode
                                    )
                                }
                            }
                            //open by search
                            else -> {
                                args.keyword?.let {
                                    viewModel.pageCount?.plus(1)?.let { it1 ->
                                        viewModel.fetchSearch(
                                            it,
                                            it1, AUDIOBOOKLIST_PAGE_SIZE, countryCode
                                        )
                                    }
                                }
                            }
                        }
                        viewModel.pageCount = viewModel.pageCount?.plus(1)
                    }
                }
            })
            subscribeUi(binding, adapter)
            return binding.root
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerLang = view.findViewById(R.id.selectLang_Dialog)
        spinnerLang?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //do not nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                //select language from category
                if (args.id != 0 && defaultLang != spinnerLang.selectedItem.toString()) {
                    defaultLang = spinnerLang.selectedItem.toString()
                    if (viewModel.stackBook != null)
                        adapter.submitList(viewModel.stackBook?.filter { item -> item.language == defaultLang })

                } else {
                    //change lang in show all
                    defaultLang = spinnerLang.selectedItem.toString()
                    var afterFilter =
                        args.listItem?.filter { it.audiobook?.language == defaultLang }
                    adapter.submitList(afterFilter?.map { featured -> featured.audiobook })
                }

            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recyclerViewCatecoryDetail.layoutManager =
                GridLayoutManager(context, 5, GridLayoutManager.VERTICAL, false)
            binding.recyclerViewCatecoryDetail.adapter = adapter
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.recyclerViewCatecoryDetail.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            binding.recyclerViewCatecoryDetail.adapter = adapter
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

    private fun subscribeUi(binding: FragmentAudiobookListBinding, adapter: AudiobookListAdapter) {
        viewModel.bookListResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBarDetail.hide()
                    result.data?.let { adapter.submitList(it) }
                }
                Result.Status.LOADING -> binding.progressBarDetail.show()
                Result.Status.ERROR -> {
                    binding.progressBarDetail.hide()
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

        viewModel.searchListResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBarDetail.hide()
                    result.data?.let { adapter.submitList(it) }
                }
                Result.Status.LOADING -> binding.progressBarDetail.show()
                Result.Status.ERROR -> {
                    binding.progressBarDetail.hide()
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })
    }

}
