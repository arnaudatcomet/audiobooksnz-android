package com.audiobookz.nz.app.player.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.ListItemBookmarkBinding
import com.audiobookz.nz.app.player.data.BookmarksData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PlayerBookmarkAdapter(
    private val viewModel: PlayerViewModel,
    private val extraContentId: String,
    private val extraLicenseId: String
) : ListAdapter<BookmarksData, CustomViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<BookmarksData>() {
        override fun areItemsTheSame(oldItem: BookmarksData, newItem: BookmarksData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: BookmarksData, newItem: BookmarksData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBookmarkBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = getItem(position)
        val itemBinding = holder.binding as ListItemBookmarkBinding
        var chapter = currentItem.playback_position?.chapter
        var part = currentItem.playback_position?.part_number
        var position = currentItem.playback_position?.position_in_millisecond!!.toLong()
        var title = currentItem.title
       // var cloudId = currentItem.user_audiobook_id

        itemBinding.chapterBookmarkTxt.text = "Chapter $chapter"
        itemBinding.titleBookmarkTxt.text = "\"$title \""
        itemBinding.positionBookmarkTxt.text = position.toString()

        convertDate(itemBinding, currentItem.created_at, currentItem.playback_position.position_in_millisecond.toLong())

        itemBinding.goToBookmarks = goToBookmarkPosition(
            //cloudId,
            chapter.toString(), extraContentId, extraLicenseId, part!!, position)
        itemBinding.clickOptionMenuListener = openOptionMenu(currentItem.id, currentItem.user_audiobook_id)
        itemBinding.swipeDelete = swipeDelete(currentItem.id, currentItem.user_audiobook_id)

    }

    private fun goToBookmarkPosition(
       // cloudId: Int,
        chapter: String, extraContentId: String, extraLicenseId: String, part: Int, position: Long): View.OnClickListener{
        return View.OnClickListener {
            viewModel.playFromBookmark(
                //cloudId,
                chapter.toInt(), extraContentId, extraLicenseId, part, position)
        }
    }

    private fun swipeDelete(bookmarkId: Int, cloudId:Int):View.OnClickListener{
        return View.OnClickListener {
            viewModel.deleteBookmark(bookmarkId,cloudId)
        }
    }
    private fun openOptionMenu(bookmarkId: Int, cloudId:Int): View.OnClickListener {
        return View.OnClickListener {
            val pop = PopupMenu(it.context, it)
            pop.inflate(R.menu.bookmarks_option)
            pop.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.addNote -> {
                        val direction =
                            PlayerBookmarkFragmentDirections.actionPlayerBookmarkFragmentToBookmarkNoteFragment(bookmarkId)
                        it.findNavController().navigate(direction)
                    }
                    R.id.delete -> {
                        viewModel.deleteBookmark(bookmarkId,cloudId)
                    }
                };true
            }
            pop.show()
        }
    }

    private fun convertDate(binding:ListItemBookmarkBinding, dateBook:Long, time:Long){

        var hour = TimeUnit.MILLISECONDS.toHours(time)
        var min = TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1)
        var sec = TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1)

        //convert timestamp to date
        val sdf: DateFormat = SimpleDateFormat("dd-MM-yyyy' 'HH:mm:aa")
        val calendar: Calendar = Calendar.getInstance()
        val tz: TimeZone = TimeZone.getDefault()
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        calendar.timeInMillis = dateBook*1000
        sdf.timeZone = tz

        binding.positionBookmarkTxt.text =  String.format("%02d:%02d:%02d", hour, min, sec)
        binding.dateBookmarkTxt.text = sdf.format(calendar.time)
    }


}