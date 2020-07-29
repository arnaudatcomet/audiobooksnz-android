package com.audiobookz.nz.app.data

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.audiobookz.nz.app.data.Result.Status.ERROR
import com.audiobookz.nz.app.data.Result.Status.SUCCESS
import io.audioengine.mobile.DownloadEvent
import kotlinx.coroutines.Dispatchers
import rx.Observable

/**
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 * [Result.Status.SUCCESS] - with data from database
 * [Result.Status.ERROR] - if error has occurred from any source
 * [Result.Status.LOADING]
 */
fun <T, A> resultLiveData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Result<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<T>())
        val source = databaseQuery.invoke().map { Result.success(it) }
        emitSource(source)
        val responseStatus = networkCall.invoke()
        if (responseStatus.status == SUCCESS) {
            saveCallResult(responseStatus.data!!)
        } else if (responseStatus.status == ERROR) {
            emit(Result.error<T>(responseStatus.message!!))
            emitSource(source)
        }

    }

fun <T, A> resultMergeMultiNetworkCallLiveData(
    databaseQuery: () -> LiveData<T>,
    listNetworkCall: List<suspend () -> Result<A>>,
    listSaveCallResult: List<suspend (Any) -> Unit>
): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<T>())
        val source = databaseQuery.invoke().map { Result.success(it) }
        emitSource(source)
        for((index,networkCall) in listNetworkCall.withIndex())
        {
            val responseStatus = networkCall.invoke()
            if (responseStatus.status == SUCCESS) {
                listSaveCallResult[index](responseStatus.data!!)
            } else if (responseStatus.status == ERROR) {
                emit(Result.error<T>(responseStatus.message!!))
                emitSource(source)
            }
        }
    }

fun <A> resultFetchOnlyLiveData(networkCall: suspend () -> Result<A>): LiveData<Result<A>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<A>())
        val responseStatus = networkCall.invoke()
        if (responseStatus.status == SUCCESS) {
            emit(Result.success<A>(responseStatus.data!!))
        } else if (responseStatus.status == ERROR) {
            emit(Result.error<A>(responseStatus.message!!))
        }
    }

 fun <A> resulObservableData(
     networkCall: Observable<A>?,
     onDownloading: (A) -> Unit,
     onPartComplete: (A)->Unit,
     onComplete:()->Unit,
     onDataError : (Throwable)->Unit)
    {
        networkCall?.subscribe(object: rx.Observer<A> {
            override fun onError(e: Throwable?) {
                if (e != null) {
                    onDataError(e)
                };
            }
            override fun onNext(t: A?) {
                if (t!=null){
                    onDownloading(t)
                    if(t is DownloadEvent) {
                        if(t.code?.equals(DownloadEvent.CONTENT_DOWNLOAD_COMPLETED)!!){
                           onPartComplete(t)
                           onComplete();
                        }
                    }
                }
            }
            override fun onCompleted() {
                onComplete();
            }

        })
    }

fun <A> resultSimpleLiveData(
    networkCall: suspend () -> Result<A>,
    saveCallResult: suspend (A) -> Unit,
    onCallSuccess:()->Unit
): LiveData<Result<A>> = liveData(Dispatchers.IO)

{
    emit(Result.loading<A>())
    val responseStatus = networkCall.invoke()
    if (responseStatus.status == SUCCESS) {
        saveCallResult(responseStatus.data!!)
        emit(Result.success<A>(responseStatus.data))
        onCallSuccess();
    } else if (responseStatus.status == ERROR) {
        emit(Result.error<A>(responseStatus.message!!))
    }
}

    fun  resultLocalSaveOnlyLiveData(
        saveCallResult: suspend () -> Unit
    ): LiveData<Result<kotlin.String>> = liveData(Dispatchers.IO)
    {
        try {
            saveCallResult.invoke()
            emit(Result.success("good"))
        }
        catch (e: Exception) {
           emit(Result.error(e.message.toString()))
        }

    }
    fun <T> resultLocalGetOnlyLiveData(
        databaseQuery: () -> LiveData<T>
    ): LiveData<Result<T>> = liveData(Dispatchers.IO)
    {
        emit(Result.loading())
        val source = databaseQuery.invoke().map { Result.success(it) }
        emitSource(source)
    }

    fun cownDownTimerSleepTime(countTime:Long, onComplete: () -> Unit, saveCountTimeToShare: (Long) -> Unit){

        saveCountTimeToShare(countTime)
        var countdown_timer = object : CountDownTimer(countTime, 1000){
            override fun onFinish() {
                onComplete()
            }

            override fun onTick(millisUntilFinished: Long) {
               //do nothing
            }

        }
        countdown_timer.start()
    }




