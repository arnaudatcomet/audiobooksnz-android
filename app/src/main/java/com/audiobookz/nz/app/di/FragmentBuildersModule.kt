package com.audiobookz.nz.app.browse.di


//import com.elifox.legocatalog.legoset.ui.LegoSetFragment
//import com.elifox.legocatalog.legoset.ui.LegoSetsFragment
//import com.elifox.legocatalog.legotheme.ui.LegoThemeFragment
import com.audiobookz.nz.app.browse.categories.ui.CategoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeCategoryFragment(): CategoryFragment

//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetsFragment(): LegoSetsFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeLegoSetFragment(): LegoSetFragment
}
