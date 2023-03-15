package fr.graux.cocktailscornerproject

import android.Manifest
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat


class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val builder = Notification.Builder(context, "i.apps.notifications")
            .setContentTitle("test2")
            .setContentText("testContent2")
            .setSmallIcon(R.drawable.ic_launcher_background)
        val notificationManager = NotificationManagerCompat.from(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "test",Toast.LENGTH_SHORT).show()
            notificationManager.notify(1234,builder.build())
        }
    }
}