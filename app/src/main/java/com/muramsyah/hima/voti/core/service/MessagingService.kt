package com.muramsyah.hima.voti.core.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.muramsyah.hima.voti.R
import com.muramsyah.hima.voti.ui.home.HomeActivity

class MessagingService : FirebaseMessagingService() {

    companion object {
        private val TAG = MessagingService::class.java.simpleName
    }

    private lateinit var broadCaster: LocalBroadcastManager

    override fun onCreate() {
        super.onCreate()
        broadCaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, "Refreshed Token: $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        p0.notification?.let {
            sendNotification(it)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(remoteMessage: RemoteMessage.Notification) {
        val channelId = "channel_1"
        val channelName = "channel_starting_vote"

        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(remoteMessage.title)
            .setContentText(remoteMessage.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationBuilder.setChannelId(channelId)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = notificationBuilder.build()
        mNotificationManager.notify(0, notification)
    }
}