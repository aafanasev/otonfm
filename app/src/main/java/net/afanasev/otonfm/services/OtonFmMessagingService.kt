package net.afanasev.otonfm.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import net.afanasev.otonfm.MainActivity
import net.afanasev.otonfm.R

class OtonFmMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "FCM token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "Message from: ${message.from}")

        val notification = message.notification ?: return

        val channelId = "fcm_default"
        val notificationManager = getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            channelId,
            "General",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "OtonFmMessaging"
    }
}
