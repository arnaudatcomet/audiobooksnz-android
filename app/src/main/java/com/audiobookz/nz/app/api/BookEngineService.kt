package com.audiobookz.nz.app.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.audiobookz.nz.app.data.Result
import io.audioengine.mobile.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import retrofit2.Response
import rx.Observable
import rx.Observer
import rx.schedulers.Schedulers


class BookEngineService{
    val downloadEngine: DownloadEngine = AudioEngine.getInstance().downloadEngine
    val playbackEngine: PlaybackEngine = AudioEngine.getInstance().playbackEngine
    fun download(contentId:String,licenseId:String): Observable<DownloadEvent>? {
        val request = DownloadRequest(contentId = contentId, part = 0, chapter = 0, type = DownloadRequest.Type.TO_END_WRAP, licenseId = licenseId)
        return downloadEngine.submit(request).subscribeOn(Schedulers.io());
    }
    fun contentStatus(contentId:String):Observable<DownloadStatus>?{
       return downloadEngine.getStatus(contentId)
    }
    fun delete(contentId:String,licenseId:String){
        val request = DownloadRequest(contentId = contentId, part = 0, chapter = 0, type = DownloadRequest.Type.TO_END_WRAP, licenseId = licenseId)
        downloadEngine.delete(request)
    }
    fun cancelDownload(downloadId:String){
        val request = downloadEngine.downloadRequest(downloadId)
        if (request != null) {
            downloadEngine.cancel(request)
        }
    }
    fun getLocalBook(status: DownloadStatus): Observable<List<String>>{
       return downloadEngine.get(status)
    }

    fun getChapterList(contentId:String): Observable<List<Chapter>> {
        return downloadEngine.getChapters(contentId)
    }

    fun play(contentId:String,licenseId:String,partNumber:Int,chapterNumber:Int,position:Long): Observable<PlaybackEvent>? {
        val playRequest = PlayRequest(licenseId,contentId,partNumber,chapterNumber,position )
        return playbackEngine.play(playRequest)
    }
    fun pausePlay(){
         playbackEngine.pause()
    }
    fun resumePlay(){
         playbackEngine.resume()
    }
    fun nextChapter(){
         playbackEngine.nextChapter()
    }
    fun previousChapter(){
         playbackEngine.previousChapter()
    }
    fun getPosition(): Long {
       return playbackEngine.getPosition()
    }
    fun setSpeed(speed:Float)  {
         playbackEngine.speed = speed
    }


}