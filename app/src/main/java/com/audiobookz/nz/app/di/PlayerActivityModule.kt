package com.audiobookz.nz.app.di

import com.audiobookz.nz.app.player.ui.PlayerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class PlayerActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributePlayerActivity(): PlayerActivity
}