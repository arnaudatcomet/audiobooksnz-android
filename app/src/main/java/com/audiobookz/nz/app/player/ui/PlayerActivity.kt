package com.audiobookz.nz.app.player.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.util.intentShareText
import com.google.android.gms.cast.framework.CastButtonFactory
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_player.*
import javax.inject.Inject

class PlayerActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    lateinit var mediaRouteMenuItem: MenuItem
    override fun supportFragmentInjector() = dispatchingAndroidInjector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        setSupportActionBar(toolbarPlayer)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.bookmarks -> {

                true
            }R.id.viewDetails -> {

                true
            }R.id.ratingStartReview -> {

                true
            }R.id.share -> {
                intentShareText( this, //getString(R.string.share_lego_set, set.name, set.url ?: "")
                    "testShare")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_player,menu)
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(applicationContext, menu,R.id.media_route_menu_item)
        return true
    }


}
