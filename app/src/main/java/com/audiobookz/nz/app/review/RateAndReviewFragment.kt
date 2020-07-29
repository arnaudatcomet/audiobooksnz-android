package com.audiobookz.nz.app.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.FragmentRateAndReviewBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModelWithActivityLifeCycle
import com.audiobookz.nz.app.review.RateAndReviewFragmentArgs
import javax.inject.Inject

class RateAndReviewFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: RateReviewViewModel
    private val args: RateAndReviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModelWithActivityLifeCycle(viewModelFactory)
        val binding = FragmentRateAndReviewBinding.inflate(inflater, container, false)
        binding.ratingBookTitle.text = args.title
        binding.ratingBookAuthor.text = "Written by: ${args.author}"
        binding.ratingBookNarrator.text = "Narrated by: ${args.narrator}"
        binding.clickSubmit = clickSubmit(binding)

        subscribeUI(binding)
        return binding.root
    }

    private fun clickSubmit(binding: FragmentRateAndReviewBinding): View.OnClickListener {
        return View.OnClickListener {
            var rateSatisfaction = binding.ratingSatisfactionBar.rating
            var rateNarration = binding.ratingNarrationBar.rating
            var rateStory = binding.ratingStoryBar.rating
            var reviewComment = binding.reviewCommentTxt.text.toString()
            if (reviewComment.isNotBlank()) {
                viewModel.postBookReview(args.id, reviewComment, rateSatisfaction, rateStory, rateNarration)
            }
            else{
                Toast.makeText(activity, "Comment cannot blank", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private fun subscribeUI(binding: FragmentRateAndReviewBinding) {
        viewModel.postBookReviewResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                }
                Result.Status.LOADING -> {

                }
                Result.Status.ERROR -> {

                }
            }
        })

    }

}
