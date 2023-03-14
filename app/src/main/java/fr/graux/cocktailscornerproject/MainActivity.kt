package fr.graux.cocktailscornerproject


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.app.UiModeManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.graux.cocktailscornerproject.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        val intent = Intent(this, ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,0,intent,FLAG_IMMUTABLE)

        bottomNavView.setupWithNavController(navController)
        createNotificationChannel()
        var alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val time = System.currentTimeMillis()
        val tenSec = 1000*10

        alarmManager.set(AlarmManager.RTC_WAKEUP,time+tenSec,pendingIntent)
    }

    @SuppressLint("ResourceType")
    fun createNotificationChannel(){
        val name = "notifChannel"
        val desc = "channel pour notif "
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channelID",name,importance)
        channel.description=desc

        val notifManager = getSystemService(NotificationManager::class.java)
        notifManager.createNotificationChannel(channel)
        val uiModeManager: UiModeManager = applicationContext
            .getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val mode:Int = uiModeManager.nightMode

        if(mode == UiModeManager.MODE_NIGHT_YES){
            binding.bottomNavigationView.background=ContextCompat
                .getDrawable(this,R.drawable.bottom_nav_backgound_dark)
            binding.bottomNavigationView.itemTextColor=ContextCompat
                .getColorStateList(this,R.drawable.color_item_nav_dark)
            binding.bottomNavigationView.itemIconTintList=ContextCompat
                .getColorStateList(this,R.drawable.color_item_nav_dark)
        }
    }
}