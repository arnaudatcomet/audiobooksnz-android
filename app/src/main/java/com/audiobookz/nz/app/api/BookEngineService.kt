package com.audiobookz.nz.app.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.audiobookz.nz.app.data.Result
import io.audioengine.mobile.AudioEngine
import io.audioengine.mobile.DownloadEngine
import io.audioengine.mobile.DownloadEvent
import io.audioengine.mobile.DownloadRequest
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import retrofit2.Response
import rx.Observable
import rx.Observer
import rx.schedulers.Schedulers


class BookEngineService{
    val downloadEngine: DownloadEngine = AudioEngine.getInstance().downloadEngine
    fun download(): Observable<DownloadEvent>? {

        val request = DownloadRequest(contentId = "69617", part = 0, chapter = 0, type = DownloadRequest.Type.TO_END_WRAP, licenseId = "5ed31f783f0f62143c261adf")

//
//        downloadEngine.submit(request).subscribeOn(Schedulers.io()).subscribe(object: Observer<DownloadEvent>{
//            override fun onError(e: Throwable?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onNext(t: DownloadEvent?) {
//                if (t?.code?.equals(DownloadEvent.DOWNLOAD_PROGRESS_UPDATE)!!) {
//                    Log.d("TAG", "onNext: "+t?.contentPercentage)
//                }else{
//
//                }
//            }
//
//            override fun onCompleted() {
//                TODO("Not yet implemented")
//            }
//
//        });
          val test =  downloadEngine.submit(request).subscribeOn(Schedulers.io());
        return test;

    }
    fun delete(){
        downloadEngine.deleteAll()
    }

}