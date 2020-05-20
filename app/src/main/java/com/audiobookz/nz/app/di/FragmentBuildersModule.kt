package com.audiobookz.nz.app.di


import com.audiobookz.nz.app.audiobookList.ui.AudiobookListFragment
import com.audiobookz.nz.app.browse.categories.ui.CategoryFragment
import com.audiobookz.nz.app.login.ui.LoginEmailFragment
import com.audiobookz.nz.app.profile.ui.ProfileFragment
import com.audiobookz.nz.app.register.ui.SignUpFragment
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
    abstract fun contributeLoginEmailFragment(): LoginEmailFragment

    @ContributesAndroidInjector
    abstract fun contributeSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetsFragment(): LegoSetsFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetFragment(): LegoSetFragment
}
