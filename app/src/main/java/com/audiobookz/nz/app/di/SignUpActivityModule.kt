package com.audiobookz.nz.app.di


import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.di.FragmentBuildersModule
import com.audiobookz.nz.app.login.ui.LoginEmailActivity
import com.audiobookz.nz.app.register.ui.SignUpActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class SignUpActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): SignUpActivity
}
