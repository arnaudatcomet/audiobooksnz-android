package com.audiobookz.nz.app.di


import com.audiobookz.nz.app.audiobookList.ui.AudiobookListFragment
import com.audiobookz.nz.app.basket.ui.BasketFragment
import com.audiobookz.nz.app.basket.ui.ConfirmOrderFragment
import com.audiobookz.nz.app.basket.ui.PayPalWebViewFragment
import com.audiobookz.nz.app.bookdetail.ui.BookDetailFragment
import com.audiobookz.nz.app.bookdetail.ui.ReviewsFragment
import com.audiobookz.nz.app.bookdetail.ui.SummaryFragment
import com.audiobookz.nz.app.browse.BrowseFragment
import com.audiobookz.nz.app.browse.BrowseNavFragment
import com.audiobookz.nz.app.browse.categories.ui.CategoryFragment
import com.audiobookz.nz.app.browse.categories.ui.SubCategoryFragment
import com.audiobookz.nz.app.browse.featured.ui.FeaturedFragment
import com.audiobookz.nz.app.login.ui.ForgotPasswordFragment
import com.audiobookz.nz.app.login.ui.LoginEmailFragment
import com.audiobookz.nz.app.more.ui.*
import com.audiobookz.nz.app.mylibrary.ui.BookDownloadFragment
import com.audiobookz.nz.app.mylibrary.ui.CloudLibraryFragment
import com.audiobookz.nz.app.mylibrary.ui.DeviceLibraryFragment
import com.audiobookz.nz.app.mylibrary.ui.MyLibraryFragment
import com.audiobookz.nz.app.player.ui.*
import com.audiobookz.nz.app.profile.ui.EditProfileFragment
import com.audiobookz.nz.app.profile.ui.ProfileFragment
import com.audiobookz.nz.app.register.ui.SignUpFragment
import com.audiobookz.nz.app.review.RateAndReviewFragment
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
    abstract fun contributeBasketFragment(): BasketFragment

    @ContributesAndroidInjector
    abstract fun contributeBrowseNavFragment(): BrowseNavFragment

    @ContributesAndroidInjector
    abstract fun contributeSummaryFragment(): SummaryFragment
    
    @ContributesAndroidInjector
    abstract fun contributeReviewFragment(): ReviewsFragment

    @ContributesAndroidInjector
    abstract fun contributeMyLibraryFragment(): MyLibraryFragment

    @ContributesAndroidInjector
    abstract fun contributeCloudLibraryFragment(): CloudLibraryFragment

    @ContributesAndroidInjector
    abstract fun contributeDeviceLibraryFragment(): DeviceLibraryFragment

    @ContributesAndroidInjector
    abstract fun contributeBookDownloadFragment(): BookDownloadFragment

    @ContributesAndroidInjector
    abstract fun contributeAudioPlayerFragment(): AudioPlayerFragment

    @ContributesAndroidInjector
    abstract fun contributePlayerChapterFragment(): PlayerChapterFragment

    @ContributesAndroidInjector
    abstract fun contributePlayerSleepTimeFragment(): PlayerSleepTimeFragment

    @ContributesAndroidInjector
    abstract fun contributePlayerSpeedFragment(): PlayerSpeedFragment

    @ContributesAndroidInjector
    abstract fun contributePlayerBookmarkFragment(): PlayerBookmarkFragment

    @ContributesAndroidInjector
    abstract fun contributeBookmarkNoteFragment(): BookmarkNoteFragment

    @ContributesAndroidInjector
    abstract fun contributeRateAndReviewFragment(): RateAndReviewFragment

    @ContributesAndroidInjector
    abstract fun contributeWishListFragment(): WishListFragment

    @ContributesAndroidInjector
    abstract fun contributeAddCreditsFragment(): AddCreditsFragment

    @ContributesAndroidInjector
    abstract fun contributeConfirmOrderFragment(): ConfirmOrderFragment

    @ContributesAndroidInjector
    abstract fun contributePayPalWebViewFragment(): PayPalWebViewFragment

    @ContributesAndroidInjector
    abstract fun contributeCurrentPlanFragment(): CurrentPlanFragment

    @ContributesAndroidInjector
    abstract fun contributeUpgradeProFragment(): UpgradeProFragment

    @ContributesAndroidInjector
    abstract fun contributeMoreFragment(): MoreFragment

    @ContributesAndroidInjector
    abstract fun contributeListCreditCardFragment(): ListCreditCardFragment

//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetsFragment(): LegoSetsFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetFragment(): LegoSetFragment
}
