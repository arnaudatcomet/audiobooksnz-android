package com.audiobookz.nz.app.di

import android.app.Application
import com.audiobookz.nz.app.App
import com.audiobookz.nz.app.browse.di.AppModule
import com.audiobookz.nz.app.browse.di.MainActivityModule
import com.audiobookz.nz.app.di.LoginEmailActivityModule
import com.audiobookz.nz.app.di.SignUpActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        MainActivityModule::class,
        LoginEmailActivityModule::class,
        SignUpActivityModule::class,
        ForgotPasswordActivityModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: App)
}
