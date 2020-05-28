package com.audiobookz.nz.app.browse.di

import android.app.Application
import android.content.Context
import com.audiobookz.nz.app.api.AudiobookService
import com.audiobookz.nz.app.api.AuthInterceptor
import com.audiobookz.nz.app.api.SharedPreferencesService
import com.audiobookz.nz.app.browse.categories.data.CategoryRemoteDataSource
import com.audiobookz.nz.app.data.AppDatabase
import com.google.android.gms.common.api.GoogleApiClient
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        return upstreamClient.newBuilder().addInterceptor(AuthInterceptor(sharePref)).build()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideUserDataDao(db: AppDatabase) = db.userDataDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application) = SharedPreferencesService(app)

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
