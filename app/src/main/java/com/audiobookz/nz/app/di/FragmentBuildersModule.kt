package com.audiobookz.nz.app.di


import com.audiobookz.nz.app.appbar.AppbarFragment
import com.audiobookz.nz.app.audiobookList.ui.AudiobookListFragment
import com.audiobookz.nz.app.basket.ui.BasketFragment
import com.audiobookz.nz.app.bookdetail.ui.BookDetailFragment
import com.audiobookz.nz.app.browse.BrowseFragment
import com.audiobookz.nz.app.browse.categories.ui.CategoryFragment
import com.audiobookz.nz.app.browse.categories.ui.SubCategoryFragment
import com.audiobookz.nz.app.browse.featured.ui.FeaturedFragment
import com.audiobookz.nz.app.login.ui.ForgotPasswordFragment
import com.audiobookz.nz.app.login.ui.LoginEmailFragment
import com.audiobookz.nz.app.profile.ui.EditProfileFragment
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
    abstract fun contributeSubCategoryFragmentFragment(): SubCategoryFragment

    @ContributesAndroidInjector
    abstract fun contributeFeaturedFragmentFragmentFragment(): FeaturedFragment

    @ContributesAndroidInjector
    abstract fun contributeCategoryDetailFragment(): AudiobookListFragment

    @ContributesAndroidInjector
    abstract fun contributeBookDetailFragmentFragment(): BookDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginEmailFragmentFragment(): LoginEmailFragment

    @ContributesAndroidInjector

    abstract fun contributeSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeEditProfileFragment(): EditProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeBrowseFragment(): BrowseFragment


    @ContributesAndroidInjector
    abstract fun contributeAppbarFragment(): AppbarFragment


    @ContributesAndroidInjector
    abstract fun contributeBasketFragment(): BasketFragment

//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetsFragment(): LegoSetsFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetFragment(): LegoSetFragment
}
