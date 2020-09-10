package com.audiobookz.nz.app.more.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.audiobookz.nz.app.bookdetail.ui.CustomViewHolder
import com.audiobookz.nz.app.databinding.ListItemCurrentPlanBinding
import com.audiobookz.nz.app.databinding.ListItemWishlistBinding
import com.audiobookz.nz.app.more.data.SubscriptionsData
import com.audiobookz.nz.app.more.data.WishListData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PlanAdapter(private var viewModel: MoreViewModel) :
    ListAdapter<SubscriptionsData, CustomViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<SubscriptionsData>() {
        override fun areItemsTheSame(
            oldItem: SubscriptionsData,
            newItem: SubscriptionsData
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: SubscriptionsData,
            newItem: SubscriptionsData
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCurrentPlanBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = getItem(position)
        val itemBinding = holder.binding as ListItemCurrentPlanBinding
        var statusPlan = currentItem.status
        itemBinding.planName.text = currentItem.plan?.title
        itemBinding.dateStart.text = convertDate(currentItem.start_time)
        itemBinding.dateFinish.text = convertDate(currentItem.end_time)
        itemBinding.payPalAgreementId.text = currentItem.paypal_subscription_id
        itemBinding.status.text = statusPlan
        itemBinding.isCancelled = statusPlan == "Cancelled"
        itemBinding.cancel = deletePlan(currentItem.id)

    }

    private fun deletePlan(subscriptionId: Int): View.OnClickListener {
        return View.OnClickListener {
            viewModel.deleteSubscriptions(subscriptionId)
        }
    }

    private fun convertDate(stringTimeStamp: String): String {


        if (stringTimeStamp != "") {
            //convert timestamp to date
            var dateString = stringTimeStamp.toLong()
            val sdf: DateFormat = SimpleDateFormat("dd-MM-yyyy' 'HH:mm:aa")
            val calendar: Calendar = Calendar.getInstance()
            val tz: TimeZone = TimeZone.getDefault()
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            calendar.timeInMillis = dateString * 1000
            sdf.timeZone = tz

            return sdf.format(calendar.time)
        }

        return ""
    }
}