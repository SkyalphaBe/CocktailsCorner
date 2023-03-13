package fr.graux.cocktailscornerproject

import android.app.UiModeManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        bottomNavView.setupWithNavController(navController)

        val uiModeManager: UiModeManager = applicationContext
            .getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val mode:Int = uiModeManager.nightMode

        if(mode == UiModeManager.MODE_NIGHT_YES){
            bottomNavView.background=ContextCompat
                .getDrawable(this,R.drawable.bottom_nav_backgound_dark)
            bottomNavView.itemTextColor=ContextCompat
                .getColorStateList(this,R.drawable.color_item_nav_dark)
            bottomNavView.itemIconTintList=ContextCompat
                .getColorStateList(this,R.drawable.color_item_nav_dark)
        }
    }
}