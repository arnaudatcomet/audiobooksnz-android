package com.audiobookz.nz.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.audiobookz.nz.app.profile.ui.EditProfileFragment
import com.audiobookz.nz.app.profile.ui.ProfileFragment
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

    fun ChangeToEditProfileFragment() {

        supportFragmentManager
            .beginTransaction()
            .remove(CurrentFragment)
            .commit()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.layout_fragment_container, EditProfileFragment.newInstance())
            .commit()
    }
}
