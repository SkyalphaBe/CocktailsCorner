package fr.graux.cocktailscornerproject


import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_setting__page.*

class Setting_Page : Fragment(R.layout.fragment_setting__page) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkUItheme()

        darkModeSwitch.setOnCheckedChangeListener {buttonView, isChecked->
            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun checkUItheme(){
        val uiModeManager:UiModeManager = requireContext().getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val mode:Int = uiModeManager.nightMode
        if(mode == UiModeManager.MODE_NIGHT_YES){
            darkModeSwitch.isChecked=true
        }
    }
}