package com.audiobookz.nz.app.player.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
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
    private lateinit var extraBookId: String
    private lateinit var extraBookTitle: String
    private lateinit var extraBookAuthor: String
    private lateinit var extraBookNarrator: String
    private lateinit var navController: NavController
    var menuItemPlayer: MenuItem? = null

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        setSupportActionBar(toolbarPlayer)
        var titleBook = findViewById<TextView>(R.id.titleBook)

        extraBookId = this.intent.getStringExtra("bookId")
        extraBookTitle = this.intent.getStringExtra("titleBook")
        extraBookAuthor = this.intent.getStringExtra("authorBook")
        extraBookNarrator = this.intent.getStringExtra("narratorBook")
        navController = findNavController(R.id.nav_player_fragment)

        //hide view in toolbar if fragment change
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioPlayerFragment -> {
                    titleBook.visibility = View.VISIBLE

                    if (menuItemPlayer != null){
                        menuItemPlayer!!.isVisible = true
                    }
                }
                else -> {
                    titleBook.visibility = View.GONE
                    menuItemPlayer!!.isVisible = false
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bookmarks -> {
                val direction =
                    AudioPlayerFragmentDirections.actionAudioPlayerFragmentToPlayerBookmarkFragment()
                this.findNavController(R.id.nav_player_fragment).navigate(direction)
                true
            }
            R.id.viewDetails -> {
                val direction =
                    AudioPlayerFragmentDirections.actionAudioPlayerFragmentToBookDetailFragment(
                        extraBookId.toInt(),
                        extraBookTitle
                    )
                this.findNavController(R.id.nav_player_fragment).navigate(direction)
                true
            }
            R.id.starReview -> {
                val direction =
                    AudioPlayerFragmentDirections.actionAudioPlayerFragmentToRateAndReviewFragment(
                        extraBookId.toInt(),
                        extraBookAuthor,
                        extraBookNarrator,
                        extraBookTitle
                    )
                this.findNavController(R.id.nav_player_fragment).navigate(direction)
                true
            }
            R.id.share -> {
                intentShareText(
                    this, //getString(R.string.share_lego_set, set.name, set.url ?: "")
                    "testShare"
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_player,menu)
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(applicationContext, menu,R.id.media_route_menu_item)
        menuItemPlayer = menu!!.findItem(R.id.action_option_player)
        return true
    }

}
