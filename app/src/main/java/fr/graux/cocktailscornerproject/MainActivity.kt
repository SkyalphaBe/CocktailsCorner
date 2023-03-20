package fr.graux.cocktailscornerproject


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.UiModeManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.graux.cocktailscornerproject.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(!CheckNetwork.isInternetAvailable(this)) {
            AlertDialog.Builder(this)
                .setTitle("No internet...")
                .setMessage("CocktailsCorner require internet Connexion to run correctly please try to connect your devise to internet")
                .setPositiveButton("Confirm"){_,_->
                    finish()
                }
                .show()
        }

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)
        bottomNavView.setupWithNavController(navController)


        val uiModeManager: UiModeManager = applicationContext
            .getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val mode:Int = uiModeManager.nightMode

        if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            binding.bottomNavigationView.background=ContextCompat
                .getDrawable(this,R.drawable.bottom_nav_backgound_dark)
            binding.bottomNavigationView.itemTextColor=ContextCompat
                .getColorStateList(this,R.drawable.color_item_nav_dark)
            binding.bottomNavigationView.itemIconTintList=ContextCompat
                .getColorStateList(this,R.drawable.color_item_nav_dark)
        }
        else if(mode == UiModeManager.MODE_NIGHT_YES&& AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_UNSPECIFIED){
            binding.bottomNavigationView.background=ContextCompat
                .getDrawable(this,R.drawable.bottom_nav_backgound_dark)
            binding.bottomNavigationView.itemTextColor=ContextCompat
                .getColorStateList(this,R.drawable.color_item_nav_dark)
            binding.bottomNavigationView.itemIconTintList=ContextCompat
                .getColorStateList(this,R.drawable.color_item_nav_dark)
        }
        else{
            binding.bottomNavigationView.background=ContextCompat
                .getDrawable(this,R.drawable.bottom_nav_backgound)
            binding.bottomNavigationView.itemTextColor=ContextCompat
                .getColorStateList(this,R.drawable.color_item_nav)
            binding.bottomNavigationView.itemIconTintList=ContextCompat
                .getColorStateList(this,R.drawable.color_item_nav)
        }
    }
}