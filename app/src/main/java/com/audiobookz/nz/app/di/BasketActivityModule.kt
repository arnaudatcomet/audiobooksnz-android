package com.audiobookz.nz.app.di

import com.audiobookz.nz.app.basket.ui.ActivityBasket
import com.audiobookz.nz.app.browse.BrowseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class BasketActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeActivityBasket(): ActivityBasket
}
