package com.audiobookz.nz.app.di

import com.audiobookz.nz.app.login.ui.ForgotPasswordActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ForgotPasswordActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): ForgotPasswordActivity
}