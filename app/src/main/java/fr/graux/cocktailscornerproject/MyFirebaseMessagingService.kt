package fr.graux.cocktailscornerproject

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notificationChannel"
const val channelName = "fr.graux.cocktailscornerproject"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null){
            remoteMessage.notification!!.title?.let { remoteMessage.notification!!.body?.let { it1 ->
                generateNotification(it,
                    it1
                )
            } }
        }
    }

    fun getRemoteView(title :String, message:String): RemoteViews {
        val remoteView = RemoteViews("fr.graux.cocktailscornerproject",R.layout.notification)

        remoteView.setTextViewText(R.id.titleNotification,title)
        remoteView.setTextViewText(R.id.messageNotification,message)
        remoteView.setImageViewResource(R.id.app_logo,R.drawable.png_clipart_computer_icons_chromatography_50x50_chromatogram_miscellaneous_text)

        return remoteView
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title : String, message : String){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)

        var builder :NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,channelId)
            .setSmallIcon(R.drawable.png_clipart_computer_icons_chromatography_50x50_chromatogram_miscellaneous_text)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }
}