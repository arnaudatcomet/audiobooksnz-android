package com.audiobookz.nz.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.audiobookz.nz.app.profile.EditProfileFragment
import com.audiobookz.nz.app.profile.ProfileFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    // temp current fragment
    var CurrentFragment = ProfileFragment.newInstance()

    override fun supportFragmentInjector() = dispatchingAndroidInjector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //start main fragment
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.layout_fragment_container, CurrentFragment)
            .commit()

    }

//    fun ChangeToEditProfileFragment() {
//
//        supportFragmentManager
//            .beginTransaction()
//            .remove(CurrentFragment)
//            .commit()
//
//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.layout_fragment_container, EditProfileFragment.newInstance())
//            .commit()
//    }
}
