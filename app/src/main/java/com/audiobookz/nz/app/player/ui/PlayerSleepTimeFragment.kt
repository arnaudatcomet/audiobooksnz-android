package com.audiobookz.nz.app.player.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentPlayerSleepTimeBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.setTitle
import javax.inject.Inject


class PlayerSleepTimeFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PlayerViewModel
    lateinit var extraID: String
    lateinit var extraLicenseId: String
    var mapTimeSleep: Map<String, Int> = mapOf(
        "Off" to 0,
        "8 Mins" to 8,
        "15 Mins" to 15,
        "30 Mins" to 30,
        "45 Mins" to 45,
        "60 Mins" to 60
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = injectViewModel(viewModelFactory)
        var binding = FragmentPlayerSleepTimeBinding.inflate(inflater, container, false)
        var previousTimer = (viewModel.currentSleepTimer?.div(60000))?.toInt()
        var adapter = PlayerSleepTimeAdapter(mapTimeSleep, viewModel, previousTimer)

        extraID = activity?.intent?.getStringExtra("idBook").toString()
        extraLicenseId = activity?.intent?.getStringExtra("licenseIDBook").toString()
        setTitle("Chapter")
        binding.timeSleepRecycleView.adapter = adapter

        return binding.root
    }

}
