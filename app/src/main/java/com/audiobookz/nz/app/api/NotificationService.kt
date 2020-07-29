package com.audiobookz.nz.app.api

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import com.audiobookz.nz.app.MainActivity
import com.audiobookz.nz.app.R
import kotlin.random.Random


class NotificationService(var context: Context) {
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.audiobookz.nz.app"
    private val description = "Test notification"
    var notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val contentView = RemoteViews(channelId, R.layout.notification_layout)
    fun simple(title:String,body:String){
        contentView.setTextViewText(R.id.tv_title,title)
        contentView.setTextViewText(R.id.tv_content,body)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(false)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(context,channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.icon40)
                .setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.icon40))
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(Random.nextInt(0, 10000),builder.build())
    }
}