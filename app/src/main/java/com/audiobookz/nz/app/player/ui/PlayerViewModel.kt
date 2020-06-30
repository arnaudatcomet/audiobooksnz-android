package com.audiobookz.nz.app.player.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiobookz.nz.app.mylibrary.data.MyLibraryRepository
import io.audioengine.mobile.Chapter
import io.audioengine.mobile.PlaybackEvent
import io.audioengine.mobile.PlayerState
import java.text.FieldPosition
import javax.inject.Inject

class PlayerViewModel @Inject constructor(private val repository: MyLibraryRepository) : ViewModel() {
    var playBackResult = MutableLiveData<PlaybackEvent>()
    var listChapterResult = MutableLiveData<List<Chapter>>()
    var playerStateResult = MutableLiveData<PlayerState>()
    var currentPlay =  repository.getCurrentChapter()

    fun playAudioBook(firstChapter: Int, contentId: String, licenseId: String, partNumber: Int) {
        if (currentPlay?.chapter!! <= firstChapter){
            repository.playAudioBook(firstChapter, contentId, licenseId, partNumber, getPosition()
            ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
        }
        else{
            repository.playAudioBook(currentPlay?.chapter!!, contentId, licenseId, currentPlay!!.part, getPosition()
            ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
        }
    }

    fun chooseNewChapter(targetChapter: Int, contentId: String, licenseId: String, partNumber: Int) {
        repository.playAudioBook(targetChapter, contentId, licenseId, partNumber, getPosition()
        ) { playBackEvent -> playBackResult.postValue(playBackEvent) }
    }

    fun pauseAudioBook(){
        repository.pauseAudioBook()
    }

    fun resumeAudioBook(){
        repository.resumeAudioBook()
    }

    fun nextChapter(){
        repository.nextChapterOfBook()
    }

    fun previousChapter(){
        repository.previousChapterOfBook()
    }

    fun setSpeed(speed:Float){
        repository.setSpeed(speed)
    }

    fun getChapters(contentId: String){
        repository.getChapterBooks(contentId) {listChapters -> listChapterResult.postValue(listChapters)}
    }

    fun seekTo(position: Long){
        repository.seekTo(position)
    }

    fun getPosition():Long{
       return repository.getPosition()
    }

    fun getCurrentSpeed(): Float {
        return repository.getCurrentSpeed()
    }

    fun getPlayerState(){
        repository.getPlayerState {state -> playerStateResult.postValue(state)}
    }

}