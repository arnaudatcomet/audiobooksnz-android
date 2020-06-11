package com.audiobookz.nz.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.audiobookz.nz.app.basket.ui.ActivityBasket
import com.audiobookz.nz.app.browse.BrowseNavFragment
import com.audiobookz.nz.app.databinding.ActivityMainBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.audiobookz.nz.app.profile.ui.EditProfileFragment
import com.audiobookz.nz.app.profile.ui.FaqProfileFragment
import com.audiobookz.nz.app.profile.ui.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,Injectable {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var BottomNavigation: BottomNavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    val fm: FragmentManager = supportFragmentManager
    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_basket,menu)
        val badgeLayout: FrameLayout =
            menu!!.findItem(R.id.action_basket).actionView as FrameLayout
        val tv = badgeLayout.findViewById(R.id.view_alert_count_textview) as TextView
        val openCart = badgeLayout.findViewById(R.id.openCart) as ImageButton
        viewModel = injectViewModel(viewModelFactory)
        viewModel.count.observe(this, Observer { count ->
            tv.text = count.toString()
        })
        openCart.setOnClickListener {
            openBasket()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_basket -> {
                openBasket()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun openBasket(){
        val intent = Intent(this, ActivityBasket::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main)
        BottomNavigation = binding.bottomNavigation
        navController = findNavController(R.id.nav_fragment)
        appBarConfiguration =  AppBarConfiguration.Builder(R.id.browse,R.id.mylibrary, R.id.more, R.id.me).build()
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

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
}
