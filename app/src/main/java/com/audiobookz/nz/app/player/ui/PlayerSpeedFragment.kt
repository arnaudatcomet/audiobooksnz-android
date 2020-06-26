package com.audiobookz.nz.app.player.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentPlayerSpeedBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.ui.setTitle
import javax.inject.Inject

class PlayerSpeedFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PlayerViewModel
    private val args: PlayerSpeedFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        var binding = FragmentPlayerSpeedBinding.inflate(inflater, container, false)
        var titleBook = activity?.findViewById<TextView>(R.id.titleBook)
        titleBook?.text = ""
        setTitle("Narration Speed")
        checkSpeed(binding)
        subscribeUi(binding)

        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    fun checkSpeed(binding: FragmentPlayerSpeedBinding){
        if (args.speedX == 1F){
            binding.speed1xIcon.visibility = View.VISIBLE
           // binding.speed1xText.setTextColor(R.color.colorPrimaryDark)
        }
    }

    private fun subscribeUi(binding: FragmentPlayerSpeedBinding) {

    }


}
