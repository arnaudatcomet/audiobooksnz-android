package com.audiobookz.nz.app.di


import com.audiobookz.nz.app.browse.BrowseActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class BrowseActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): BrowseActivity
}
