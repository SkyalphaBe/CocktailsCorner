package fr.graux.cocktailscornerproject


import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial

class Setting_Page : Fragment(R.layout.fragment_setting__page) {

    private lateinit var settingText:TextView
    private lateinit var darkModeSwitch:Switch

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        darkModeSwitch = view.findViewById(R.id.darkModeSwitch)
        settingText = view.findViewById(R.id.settingText)

        checkUITheme()

        darkModeSwitch.setOnCheckedChangeListener {buttonView, isChecked->
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
    }

    private fun checkUITheme(){
        val uiModeManager:UiModeManager = requireContext().getSystemService(Context.UI_MODE_SERVICE)
                as UiModeManager
        val mode:Int = uiModeManager.nightMode
        if(mode == UiModeManager.MODE_NIGHT_YES){
            darkModeSwitch.isChecked=true
            settingText.background=ContextCompat.getDrawable(requireContext(),R.color.purple_500)
        }
    }
}