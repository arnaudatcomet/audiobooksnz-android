package com.audiobookz.nz.app.browse.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.audiobookz.nz.app.audiobookList.ui.AudiobookListViewModel
import com.audiobookz.nz.app.browse.categories.ui.CategoryViewModel
import com.audiobookz.nz.app.login.ui.LoginViewModel
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

//    @Binds
//    @IntoMap
//    @ViewModelKey(LegoSetsViewModel::class)
//    abstract fun bindLegoSetsViewModel(viewModel: LegoSetsViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(LegoSetViewModel::class)
//    abstract fun bindLegoSetViewModel(viewModel: LegoSetViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
