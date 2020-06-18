package com.audiobookz.nz.app.mylibrary.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs

import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.audiobookList.ui.AudiobookListFragmentArgs
import com.audiobookz.nz.app.databinding.FragmentBookDownloadBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BookDownloadFragment : Fragment() {
    private val args: BookDownloadFragmentArgs by navArgs()
    var statusCheck = "Not Downloaded"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBookDownloadBinding.inflate(inflater, container, false)
        binding.txtNameBookDownload.text = args.title
        binding.urlImage = args.url

        //need check status first
        binding.statusTxt.text = "Not Downloaded"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var playBtn = view.findViewById<FloatingActionButton>(R.id.imgPlayDownload)
        var downloadBtn = view.findViewById<Button>(R.id.btnDownload)
        var percetBar = view.findViewById<ProgressBar>(R.id.progress_download)
        var txtPercet = view.findViewById<TextView>(R.id.percentTxt)
        //use mock data test
        var percet = 0

        downloadBtn.setOnClickListener {
            if (statusCheck == "Not Downloaded"){
                statusCheck = "Downloading"
                downloadBtn.text = "Cancel"
//                while (percet < 100){
//                    percet += 10
//                    percetBar.progress = percet
//                    txtPercet.text = percet.toString()
//                    if (percet == 100){
//                        statusCheck = "Downloaded"
//                    }
//                }
            }
            else if(statusCheck == "Downloading"){
                statusCheck = "Not Downloaded"
                downloadBtn.text = "Download"
            }
        }

        playBtn.setOnClickListener {
//            val navController = Navigation.findNavController(view!!)
//            navController.navigate(
//                BookDownloadFragmentDirections.actionBookDownloadFragmentToAudioPlayerFragment()
//            )
        }
    }

}
