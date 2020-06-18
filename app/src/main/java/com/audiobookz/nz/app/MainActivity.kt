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
import android.view.Menu
import android.view.MenuItem
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
import com.audiobookz.nz.app.databinding.ActivityMainBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
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
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.audiobookz.nz.app"
    private val description = "Test notification"
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
        val isDiscover = intent.getStringExtra(EXTRA_MESSAGE)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main)
        BottomNavigation = binding.bottomNavigation
        binding.isDiscover = isDiscover!=null
        navController = findNavController(R.id.nav_fragment)
        appBarConfiguration =  AppBarConfiguration.Builder(R.id.browse,R.id.mylibrary, R.id.more, R.id.me).build()
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val contentView = RemoteViews(packageName,R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title,"CodeAndroid")
        contentView.setTextViewText(R.id.tv_content,"Text notification")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(false)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this,channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.facebook40)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.facebook40))
                .setContentIntent(pendingIntent)
        }


        val timer = object: CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
               // notificationManager.notify(Random.nextInt(0, 10000),builder.build())
            }
            override fun onFinish() {
             //   notificationManager.notify(Random.nextInt(0, 10000),builder.build())
             //   viewModel.notifier("test","test")
            }
        }

        timer.start()
    }
}
