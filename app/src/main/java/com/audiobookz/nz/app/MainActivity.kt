package com.audiobookz.nz.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.RemoteViews
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.audiobookz.nz.app.basket.ui.ActivityBasket
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.databinding.ActivityMainBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.audioengine.mobile.AudioEngine
import io.audioengine.mobile.LogLevel
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,Injectable {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun supportFragmentInjector() = dispatchingAndroidInjector
    private var isDiscover: Boolean = false
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
        viewModel = injectViewModel(viewModelFactory)
        isDiscover = intent.getBooleanExtra(EXTRA_MESSAGE, false)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main)
        bottomNavigation = binding.bottomNavigation
        binding.isDiscover = isDiscover
        navController = findNavController(R.id.nav_fragment)
        appBarConfiguration =  AppBarConfiguration.Builder(R.id.browse,R.id.mylibrary, R.id.more, R.id.me).build()
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        //hide bottomNav on some fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.bookDownloadFragment -> {
                    bottomNavigation.visibility = View.GONE
                }
                R.id.editProfileFragment -> {
                    bottomNavigation.visibility = View.GONE
                }
                R.id.faqProfileFragment -> {
                    bottomNavigation.visibility = View.GONE
                }
                else -> {
                    if(!isDiscover){
                    bottomNavigation.visibility = View.VISIBLE}
                }
            }
        }
    }
}
