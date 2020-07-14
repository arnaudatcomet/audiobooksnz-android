package com.audiobookz.nz.app.player.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentPlayerBookmarkBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PlayerBookmarkFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PlayerViewModel
    private lateinit var extraCloudBookId: String
    private lateinit var extraContentId: String
    private lateinit var extraLicenseId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        var binding = FragmentPlayerBookmarkBinding.inflate(inflater, container, false)
        extraCloudBookId = activity?.intent?.getStringExtra("cloudBookId").toString()
        extraContentId = activity?.intent?.getStringExtra("contentId").toString()
        extraLicenseId = activity?.intent?.getStringExtra("licenseIDBook").toString()
        viewModel.getBookmarks(extraCloudBookId.toInt())
        subscribeUI(binding)
        return binding.root
    }

    private fun subscribeUI(binding: FragmentPlayerBookmarkBinding) {
        viewModel.getBookmarksResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    val adapter = PlayerBookmarkAdapter(viewModel, extraContentId, extraLicenseId)
                    adapter?.submitList(result.data)
                    binding.bookmarkRecycleView.adapter = adapter
                    //  Toast.makeText(activity, "done", Toast.LENGTH_SHORT).show();3
                }
                Result.Status.LOADING -> {
                    //  Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();3
                }
                Result.Status.ERROR -> {
                    // Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show();3
                }
            }
        })

    }

}
