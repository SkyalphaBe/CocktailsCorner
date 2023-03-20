package fr.graux.cocktailscornerproject

import android.app.UiModeManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

class AppInfoPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info_page)

        val uiModeManager: UiModeManager = applicationContext
            .getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val mode:Int = uiModeManager.nightMode

        val btnBackInfo = findViewById<Button>(R.id.backBtnInfo)
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            btnBackInfo.background=ContextCompat.getDrawable(this,R.drawable.btn_back_dark)
        }
        else if(mode==UiModeManager.MODE_NIGHT_YES
            && AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_UNSPECIFIED){
            btnBackInfo.background=ContextCompat.getDrawable(this,R.drawable.btn_back_dark)
        }
        else{
            btnBackInfo.background=ContextCompat.getDrawable(this,R.drawable.btn_back)
        }
        btnBackInfo.setOnClickListener {
            finish()
        }
    }
}