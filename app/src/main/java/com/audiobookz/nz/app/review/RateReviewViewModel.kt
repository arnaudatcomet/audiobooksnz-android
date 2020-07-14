package com.audiobookz.nz.app.review

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.bookdetail.data.BookReview
import javax.inject.Inject
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.player.data.PlayerRepository

class RateReviewViewModel @Inject constructor(private val repository: PlayerRepository) :
    ViewModel() {
    var postBookReviewResult = MediatorLiveData<Result<BookReview>>()

    fun postBookReview(bookId: Int, comment: String, statification: Float, story: Float, narration: Float){
        postBookReviewResult.addSource(repository.postBookReview(bookId,comment,statification,story,narration)){value ->
            postBookReviewResult.value = value
        }
    }
}