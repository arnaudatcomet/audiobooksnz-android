package com.audiobookz.nz.app.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentWebviewProfileBinding
import com.audiobookz.nz.app.ui.setTitle


class WebViewProfileFragment : Fragment() {
    private val args: WebViewProfileFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWebviewProfileBinding.inflate(inflater, container, false)
        binding.webView.settings.javaScriptEnabled = true
//        binding.webView.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl(url)
//                return true
//            }
//        }
        when (args.linkName) {
            "FAQ" -> {
                setTitle("FAQ")
                binding.webView.loadUrl("https://www.audiobooksnz.com/android-faq.html")
            }
            "Facebook" -> {
                setTitle("Audiobooks NZ Facebook Page")
                binding.webView.loadUrl("https://www.facebook.com/audiobooksnz")
            }
            else -> {
                setTitle("Audiobooks NZ Twitter Page")
                binding.webView.loadUrl("https://twitter.com/audiobooksnz")
            }
        }

        return binding.root
    }

}
