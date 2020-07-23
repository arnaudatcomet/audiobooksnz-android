package com.audiobookz.nz.app.basket.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs

import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentPayPalWebViewBinding

class PayPalWebViewFragment : Fragment() {
    private val args: PayPalWebViewFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPayPalWebViewBinding.inflate(inflater, container, false)
        binding.webViewPayPal.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        binding.webViewPayPal.loadUrl(args.linkUrl)
        return binding.root
    }

}
