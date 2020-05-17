package com.audiobookz.nz.app.di


import com.audiobookz.nz.app.audiobookList.ui.AudiobookListFragment
import com.audiobookz.nz.app.browse.categories.ui.CategoryFragment
import com.audiobookz.nz.app.login.ui.LoginEmailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeCategoryFragment(): CategoryFragment
    @ContributesAndroidInjector
    abstract fun contributeCategoryDetailFragment(): AudiobookListFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginEmailFragmentFragment(): LoginEmailFragment

//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetsFragment(): LegoSetsFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetFragment(): LegoSetFragment
}
