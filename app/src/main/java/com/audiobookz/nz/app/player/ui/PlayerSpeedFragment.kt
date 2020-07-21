package com.audiobookz.nz.app.player.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        var binding = FragmentPlayerSpeedBinding.inflate(inflater, container, false)
        var titleBook = activity?.findViewById<TextView>(R.id.titleBook)
        titleBook?.text = ""
        setTitle("Narration Speed")
        binding.select05Click = select05(binding)
        binding.select075Click = select075(binding)
        binding.select1Click = select1(binding)
        binding.select125Click = select125(binding)
        binding.select15Click = select15(binding)
        binding.select175Click = select175(binding)
        viewModel.getCurrentSpeed()?.let { checkSpeed(binding, it) }
        return binding.root
    }

    fun checkSpeed(binding: FragmentPlayerSpeedBinding, speed: Float){

        when (speed) {
            1F -> {
                binding.is1Click = true
            }
            0.5F -> {
                binding.is05Click = true
            }
            0.75F -> {
                binding.is075Click = true
            }
            1.25F -> {
                binding.is125Click = true
            }
            1.5F -> {
                binding.is15Click = true
            }
            else -> {
                binding.is175Click = true
            }
        }
    }

    fun selectSpeed(speed: Float, binding: FragmentPlayerSpeedBinding){
        when (speed) {
            1F -> {
                viewModel.setSpeed(speed)
                binding.is05Click = false
                binding.is075Click = false
                binding.is1Click = true
                binding.is125Click = false
                binding.is15Click = false
                binding.is175Click = false
            }
            0.5F -> {
                viewModel.setSpeed(speed)
                binding.is05Click = true
                binding.is075Click = false
                binding.is1Click = false
                binding.is125Click = false
                binding.is15Click = false
                binding.is175Click = false
            }
            0.75F -> {
                viewModel.setSpeed(speed)
                binding.is05Click = false
                binding.is075Click = true
                binding.is1Click = false
                binding.is125Click = false
                binding.is15Click = false
                binding.is175Click = false
            }
            1.25F -> {
                viewModel.setSpeed(speed)
                binding.is05Click = false
                binding.is075Click = false
                binding.is1Click = false
                binding.is125Click = true
                binding.is15Click = false
                binding.is175Click = false
            }
            1.5F -> {
                viewModel.setSpeed(speed)
                binding.is05Click = false
                binding.is075Click = false
                binding.is1Click = false
                binding.is125Click = false
                binding.is15Click = true
                binding.is175Click = false
            }
            else -> {
                viewModel.setSpeed(speed)
                binding.is05Click = false
                binding.is075Click = false
                binding.is1Click = false
                binding.is125Click = false
                binding.is15Click = false
                binding.is175Click = true
            }
        }
    }

    private fun select05(binding: FragmentPlayerSpeedBinding): View.OnClickListener {
        return View.OnClickListener {
            selectSpeed(0.5F,binding)
        }
    }

    private fun select075(binding: FragmentPlayerSpeedBinding): View.OnClickListener {
        return View.OnClickListener {
            selectSpeed(0.75F,binding)
        }
    }

    private fun select1(binding: FragmentPlayerSpeedBinding): View.OnClickListener {
        return View.OnClickListener {
            selectSpeed(1F,binding)
        }
    }

    private fun select125(binding: FragmentPlayerSpeedBinding): View.OnClickListener {
        return View.OnClickListener {
            selectSpeed(1.25F,binding)
        }
    }

    private fun select15(binding: FragmentPlayerSpeedBinding): View.OnClickListener {
        return View.OnClickListener {
            selectSpeed(1.5F,binding)
        }
    }

    private fun select175(binding: FragmentPlayerSpeedBinding): View.OnClickListener {
        return View.OnClickListener {
            selectSpeed(1.75F,binding)
        }
    }

}
