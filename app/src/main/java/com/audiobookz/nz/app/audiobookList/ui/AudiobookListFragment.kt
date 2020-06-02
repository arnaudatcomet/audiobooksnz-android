package com.audiobookz.nz.app.audiobookList.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.browse.categories.ui.VerticalItemDecoration
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentAudiobookListBinding
import com.audiobookz.nz.app.ui.hide
import com.audiobookz.nz.app.ui.show
import com.audiobookz.nz.app.util.CATEGORY_PAGE_SIZE
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class AudiobookListFragment: Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AudiobookListViewModel
    private val args: AudiobookListFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentAudiobookListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = AudiobookListAdapter()
        binding.recyclerViewCatecoryDetail.addItemDecoration(
            VerticalItemDecoration(resources.getDimension(R.dimen.margin_normal).toInt(), true) )
        binding.recyclerViewCatecoryDetail.adapter = adapter

        //xok try to use fake input need to change data later
        viewModel.fetchCategory(args.id,1, CATEGORY_PAGE_SIZE,"English")


        subscribeUi(binding, adapter)

        return binding.root
    }
    private fun subscribeUi(binding: FragmentAudiobookListBinding, adapter: AudiobookListAdapter) {
        viewModel.bookListResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBarDetail.hide()
                    result.data?.let { adapter.submitList(it) }
                }
                Result.Status.LOADING -> binding.progressBarDetail.show()
                Result.Status.ERROR -> {
                    binding.progressBarDetail.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

}
