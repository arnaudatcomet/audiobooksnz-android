package com.audiobookz.nz.app.browse.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.MainActivityViewModel
import com.audiobookz.nz.app.appbar.AppbarViewModel
import com.audiobookz.nz.app.audiobookList.ui.AudiobookListViewModel
import com.audiobookz.nz.app.basket.ui.BasketViewModel
import com.audiobookz.nz.app.bookdetail.ui.BookDetailViewModel
import com.audiobookz.nz.app.browse.categories.ui.CategoryViewModel
import com.audiobookz.nz.app.browse.featured.ui.FeaturedViewModel
import com.audiobookz.nz.app.login.ui.LoginViewModel
import com.audiobookz.nz.app.mylibrary.ui.MyLibraryViewModel
import com.audiobookz.nz.app.player.ui.PlayerViewModel
import com.audiobookz.nz.app.profile.ui.EditProfileViewModel
import com.audiobookz.nz.app.register.ui.SignUpViewModel
//import com.audiobookz.nz.app.browse.categories.ui.viewmo

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(viewModel: CategoryViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(FeaturedViewModel::class)
    abstract fun bindFeaturedViewModel(viewModel: FeaturedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AudiobookListViewModel::class)
    abstract fun bindCategoryDetailViewModel(viewModel: AudiobookListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun bindEditProfileViewModel(viewModel: EditProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BookDetailViewModel::class)
    abstract fun bindBookDetailViewModel(viewModel: BookDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AppbarViewModel::class)
    abstract fun bindAppbarViewModel(viewModel: AppbarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BasketViewModel::class)
    abstract fun bindBasketViewModel(viewModel: BasketViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyLibraryViewModel::class)
    abstract fun bindMyLibraryViewModel(viewModel: MyLibraryViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    abstract fun bindPlayerViewModel(viewModel: PlayerViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
