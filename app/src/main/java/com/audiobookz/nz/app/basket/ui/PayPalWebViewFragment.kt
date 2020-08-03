package com.audiobookz.nz.app.basket.ui

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentPayPalWebViewBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.util.WEB_URL
import kotlinx.android.synthetic.main.fragment_pay_pal_web_view.*
import javax.inject.Inject

class PayPalWebViewFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BasketViewModel
    private val args: PayPalWebViewFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webViewPayPal.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url == WEB_URL || url == "$WEB_URL/home") {
                    viewModel.buyCreditStatusNotification(
                        "Payment Status",
                        " Your Payment is Failed"
                    )
                } else if (url == "$WEB_URL/user/myaudio" || url == "$WEB_URL/home/login") {
                    viewModel.buyCreditStatusNotification(
                        "Payment Status",
                        " Your Payment is succesful"
                    )
                    if (args.NavigateFrom == "Order"){
                        AsyncTask.execute {
                            viewModel.deleteCartAll()
                        }
                    }
                }
                return true
            }
        }
    }

}
