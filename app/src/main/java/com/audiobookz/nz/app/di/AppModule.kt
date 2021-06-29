package com.audiobookz.nz.app.browse.di

import android.app.Application
import com.audiobookz.nz.app.api.*
import com.audiobookz.nz.app.browse.categories.data.CategoryRemoteDataSource
import com.audiobookz.nz.app.data.AppDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class, CoreDataModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideLegoService(@AudiobooksAPI okhttpClient: OkHttpClient,
            converterFactory: GsonConverterFactory
    ) = provideService(okhttpClient, converterFactory, AudiobookService::class.java)

    @Singleton
    @Provides
    fun provideLegoSetRemoteDataSource(audioService: AudiobookService)
            = CategoryRemoteDataSource(audioService)

    @AudiobooksAPI
    @Provides
    fun providePrivateOkHttpClient(sharePref : SharedPreferencesService,
                                   upstreamClient: OkHttpClient
    ): OkHttpClient {
        return upstreamClient.newBuilder()
            //set time for test when long fetch data time out
            .connectTimeout(60,TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(sharePref)).build()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideUserDataDao(db: AppDatabase) = db.userDataDao()


    @Singleton
    @Provides
    fun provideBookRoomDao(db: AppDatabase) = db.bookRoomDao()

    @Singleton
    @Provides
    fun provideSessionDataDao(db: AppDatabase) = db.sessionDataDao()

    @Singleton
    @Provides
    fun provideLocalBookDataDao(db: AppDatabase) = db.localBookDataDao()

    @Singleton
    @Provides
    fun provideChapterDataDao(db: AppDatabase) = db.chapterDataDao()

    @Singleton
    @Provides
    fun provideCardDataDao(db: AppDatabase) = db.cardDataDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application) = SharedPreferencesService(app)

    @Singleton
    @Provides
    fun provideBookEngineService() = BookEngineService()

    @Singleton
    @Provides
    fun provideNotificationService(app: Application) =
        NotificationService(app)

    @Singleton
    @Provides
    fun provideFirebaseAnalyticsService() =
        FirebaseAnalyticsService()

    @Singleton
    @Provides
    fun provideAlertDialogsService(app: Application) =
        AlertDialogsService(app)

//    @Singleton
//    @Provides
//    fun provideLegoThemeDao(db: AppDatabase) = db.legoThemeDao()

    @CoroutineScropeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    private fun createRetrofit(
            okhttpClient: OkHttpClient,
            converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(AudiobookService.ENDPOINT)
                .client(okhttpClient)
                .addConverterFactory(converterFactory)
                .build()
    }

    private fun <T> provideService(okhttpClient: OkHttpClient,
            converterFactory: GsonConverterFactory, clazz: Class<T>): T {
        return createRetrofit(okhttpClient, converterFactory).create(clazz)
    }
}
