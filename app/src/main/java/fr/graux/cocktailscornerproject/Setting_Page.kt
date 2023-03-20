package fr.graux.cocktailscornerproject


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.IOException


class Setting_Page : Fragment(R.layout.fragment_setting__page) {

    private lateinit var settingText:TextView
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var darkModeSwitch:Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var notifSwitch:Switch
    private lateinit var deleteDataBtn:Button
    private val PREFSFILENAME = "com.app.app.prefs"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        darkModeSwitch = view.findViewById(R.id.darkModeSwitch)
        settingText = view.findViewById(R.id.settingText)
        notifSwitch = view.findViewById(R.id.switchNotification)

        if (this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } == PackageManager.PERMISSION_GRANTED
        ){
            notifSwitch.isChecked=true
        }
        notifSwitch.setOnCheckedChangeListener{_,isChecked->
            if (isChecked){
                if (this.context?.let {
                        ActivityCompat.checkSelfPermission(
                            it,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    } != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        0)
                }
            }
        }
        deleteDataBtn = view.findViewById(R.id.deleteData)

        checkUITheme()

        darkModeSwitch.setOnCheckedChangeListener {_, isChecked->
            val bottomNavView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                bottomNavView.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bottom_nav_backgound_dark
                )
                bottomNavView.itemTextColor=ContextCompat
                    .getColorStateList(requireContext(),R.drawable.color_item_nav_dark)
                bottomNavView.itemIconTintList=ContextCompat
                    .getColorStateList(requireContext(),R.drawable.color_item_nav_dark)
                settingText.background=ContextCompat.getDrawable(requireContext(),R.color.purple_500)
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                bottomNavView.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bottom_nav_backgound
                )
                bottomNavView.itemTextColor=ContextCompat
                    .getColorStateList(requireContext(),R.drawable.color_item_nav)
                bottomNavView.itemIconTintList=ContextCompat
                    .getColorStateList(requireContext(),R.drawable.color_item_nav)
                settingText.background=ContextCompat.getDrawable(requireContext(),R.color.blue)
            }
        }

        val textAbout = view.findViewById<TextView>(R.id.aboutApp)

        textAbout.setOnClickListener{
            val intent = Intent(view.context, AppInfoPage::class.java)
            ContextCompat.startActivity(view.context,intent,null)
        }

        deleteDataBtn.setOnClickListener{_->
            AlertDialog.Builder(requireContext())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete your favorites bars and cocktails?")
                .setPositiveButton("Yes") { _, _ ->
                    val file: File = requireContext().getFileStreamPath("FavoriteBar.txt")
                    if (file.exists()) {
                        try {
                            file.delete()
                            Toast.makeText(
                                requireContext(),
                                "Favorites are deleted",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } catch (e: IOException) {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    val sharedPreferences = requireContext().getSharedPreferences(PREFSFILENAME, 0)
                    val editor = sharedPreferences.edit()
                    editor.remove("cocktailList").apply()
                }
                .setNegativeButton("No") { _, _ ->
                }
                .show()
        }
    }

    private fun checkUITheme(){
        val uiModeManager:UiModeManager = requireContext().getSystemService(Context.UI_MODE_SERVICE)
                as UiModeManager
        val mode:Int = uiModeManager.nightMode
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_NO) {
            darkModeSwitch.isChecked = false
            settingText.background=ContextCompat.getDrawable(requireContext(),R.color.blue)
        }
        else if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            darkModeSwitch.isChecked = true
            settingText.background=ContextCompat.getDrawable(requireContext(),R.color.purple_500)
        }
        else if(mode == UiModeManager.MODE_NIGHT_YES){
            darkModeSwitch.isChecked=true
            settingText.background=ContextCompat.getDrawable(requireContext(),R.color.purple_500)
        }
    }
}