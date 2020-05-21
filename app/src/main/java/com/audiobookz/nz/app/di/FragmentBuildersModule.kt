package com.audiobookz.nz.app.di


import com.audiobookz.nz.app.audiobookList.ui.AudiobookListFragment
import com.audiobookz.nz.app.browse.categories.ui.CategoryFragment
import com.audiobookz.nz.app.login.ui.LoginEmailFragment
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
    abstract fun contributeLoginEmailFragmentFragment(): LoginEmailFragment

    @ContributesAndroidInjector
<<<<<<< HEAD
    abstract fun contributeSignUpFragment(): SignUpFragment
<<<<<<< Updated upstream
=======

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment
>>>>>>> Stashed changes
=======
    abstract fun SignUpFragment(): SignUpFragment
>>>>>>> c083581127d87af9dfa930e74be2814af0e9b397

//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetsFragment(): LegoSetsFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetFragment(): LegoSetFragment
}
