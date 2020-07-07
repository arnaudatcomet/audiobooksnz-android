package com.audiobookz.nz.app.player.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.databinding.ListItemTimesleepBinding


class PlayerSleepTimeAdapter(val mapTimeSleep: Map<String,Int>, private val viewModel: PlayerViewModel, private val previousTimer:Int) :
    RecyclerView.Adapter<PlayerSleepTimeAdapter.ViewHolder>() {
    var oldPosition = previousTimer

    class ViewHolder(view: ListItemTimesleepBinding) :
        RecyclerView.ViewHolder(view.root) {
        val rvTime = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTimesleepBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var currentSleepTimeKey = mapTimeSleep.keys.toTypedArray()[position]
        var currentSleepTimeValue:Int = mapTimeSleep[currentSleepTimeKey] ?: 0
        val itemBinding = holder.rvTime
        itemBinding.timeSleepText.text = currentSleepTimeKey

        itemBinding.isClick = oldPosition == currentSleepTimeValue
        itemBinding.selectClick = selectSleepTime(currentSleepTimeValue)

        itemBinding.executePendingBindings()
    }

    private fun selectSleepTime(timeCount:Int): View.OnClickListener {
        return View.OnClickListener {
            var timeMiliSec = (timeCount*60000).toLong()
            oldPosition = timeCount
            notifyDataSetChanged()
            viewModel.setTimeSleep(timeMiliSec)
        }
    }

    override fun getItemCount(): Int {
        return mapTimeSleep.size
    }
}