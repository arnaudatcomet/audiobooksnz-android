package com.audiobookz.nz.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
    val BrowseFrag = CategoryFragment.newInstance()
    var bottomNav: BottomNavigationView? = null

    override fun supportFragmentInjector() = dispatchingAndroidInjector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pointer = arrayOf("home","Profile")
        bottomNav = findViewById(R.id.bottom_navigation)
        //start main fragment
        setContentView(R.layout.activity_main)
        fm
            .beginTransaction()
            .add(R.id.layout_fragment_container, ProfileFrag)
            .commit()

//         bottomNav.setOnNavigationItemSelectedListener( pointer ->
//        when()
//        )

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
