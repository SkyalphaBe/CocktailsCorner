package fr.graux.cocktailscornerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class AppInfo_Page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info_page)

        val btnBackInfo = findViewById<Button>(R.id.backBtnInfo)
        btnBackInfo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            ContextCompat.startActivity(this, intent, null)
        }
    }
}