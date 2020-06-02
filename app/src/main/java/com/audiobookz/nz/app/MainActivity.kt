package com.audiobookz.nz.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.audiobookz.nz.app.browse.BrowseFragment
import com.audiobookz.nz.app.browse.categories.ui.CategoryFragment
import com.audiobookz.nz.app.profile.ui.EditProfileFragment
import com.audiobookz.nz.app.profile.ui.FaqProfileFragment
import com.audiobookz.nz.app.profile.ui.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    val fm: FragmentManager = supportFragmentManager
    val ProfileFrag = ProfileFragment.newInstance()
    val editProfileFragFrag = EditProfileFragment.newInstance()
    val FaqFrag = FaqProfileFragment.newInstance()
    val BrowseFrag = BrowseFragment.newInstance()

    override fun supportFragmentInjector() = dispatchingAndroidInjector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var bottomNav:BottomNavigationView = findViewById(R.id.bottom_navigation)

        fm
            .beginTransaction()
            .replace(R.id.layout_fragment_container, ProfileFrag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mylibrary -> {
//                    fm
//                        .beginTransaction()
//                        .replace(R.id.layout_fragment_container, ProfileFrag)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .commit()
                }

                R.id.browse -> {
                    fm
                        .beginTransaction()
                        .replace(R.id.layout_fragment_container, BrowseFrag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.more -> {
//                    fm
//                        .beginTransaction()
//                        .replace(R.id.layout_fragment_container, ProfileFrag)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .commit()
                }

                R.id.me -> {
                    fm
                        .beginTransaction()
                        .replace(R.id.layout_fragment_container, ProfileFrag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
            true
        }

    }

    //back stack for fragment
    override fun onBackPressed() {
        var count = fm.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            fm.popBackStack()
        }
    }

    fun ChangeToEditProfileFragment() {

        fm
            .beginTransaction()
            .add(R.id.layout_fragment_container, editProfileFragFrag)
            .addToBackStack(null)
            .commit()
    }

    fun ChangeToFAQFragment() {

        fm
            .beginTransaction()
            .add(R.id.layout_fragment_container, FaqFrag)
            .addToBackStack(null)
            .commit()
    }
}
