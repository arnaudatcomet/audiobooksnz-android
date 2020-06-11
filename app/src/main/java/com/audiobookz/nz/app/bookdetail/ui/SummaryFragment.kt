package com.audiobookz.nz.app.bookdetail.ui

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import javax.inject.Inject

class SummaryFragment(val despt: String) : Fragment()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val desptTxt = view.findViewById<TextView>(R.id.summary_description)
        desptTxt.text = HtmlCompat.fromHtml(despt, HtmlCompat.FROM_HTML_MODE_LEGACY)

    }

}
