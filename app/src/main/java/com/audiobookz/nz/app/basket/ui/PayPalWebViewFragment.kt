package com.audiobookz.nz.app.basket.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.MainActivity

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

        binding.webViewPayPal.settings.javaScriptEnabled = true

        //hide progressbar when load url done
        binding.webViewPayPal.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100){
                    binding.webViewProgressBar.visibility = View.INVISIBLE
                    binding.webViewPayPal.visibility = View.VISIBLE
                }
                else{
                    binding.webViewProgressBar.visibility = View.VISIBLE
                    binding.webViewPayPal.visibility = View.INVISIBLE
                }
            }
        }

        binding.webViewPayPal.loadUrl(args.linkUrl)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe change url page
        webViewPayPal.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url == "$WEB_URL/" || url == "$WEB_URL/home") {
                    viewModel.statusNotification(
                        "Payment Status",
                        " Your Payment is Failed"
                    )
                } else if (url == "$WEB_URL/user/myaudio" || url == "$WEB_URL/home/login") {
                    viewModel.statusNotification(
                        "Payment Status",
                        " Your Payment is successful"
                    )
                    if (args.NavigateFrom == "Order"){
                        AsyncTask.execute {
                            viewModel.deleteCartAll()
                        }
                    }
                }
                else if (url == "$WEB_URL/book/productlist/new-releases?lang%5B%5D=English"){
                    viewModel.statusNotification(
                        "Payment Status",
                        " Congratulations! Subscription Successful, You can now use your Trial Book Credit(s)."
                    )
                    viewModel.saveIsSubscribed(true)
                    val intent = Intent(activity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    //never go back if done
                    activity?.finish()
                }
                else if(url == "$WEB_URL/home/index"){
                    viewModel.statusNotification(
                        "Payment Status",
                        " sorry,You cannot subscribe to a trial plan"
                    )
                    val intent = Intent(activity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    //never go back if done
                    activity?.finish()
                }

                return false
            }
        }
    }

}
