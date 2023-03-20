package fr.graux.cocktailscornerproject


import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class CocktailPage : Fragment(R.layout.fragment_cocktail__page){


    //la listeview pour afficher les cocktail
    lateinit var detailsListView: ListView
    //la lsite des cocktails
    var detailsArrayList:ArrayList<CocktailObject> = ArrayList()
    //la liste des cocktails favoris
    var favorisArrayList:ArrayList<String> = ArrayList()
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        loadData()


        val view = inflater.inflate(R.layout.fragment_cocktail__page,container,false)
        detailsListView = view.findViewById(R.id.listCocktail)

        run(view.context)

        val title = view?.findViewById<TextView>(R.id.titleHome)

        val uiModeManager: UiModeManager = requireContext().getSystemService(Context.UI_MODE_SERVICE)
                as UiModeManager
        val mode:Int = uiModeManager.nightMode
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            if (title != null) {
                title.background = ContextCompat.getDrawable(requireContext(),R.color.purple_500)
            }
        }
        else if(mode == UiModeManager.MODE_NIGHT_YES&& AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_UNSPECIFIED){
            if (title != null) {
                title.background = ContextCompat.getDrawable(requireContext(),R.color.purple_500)
            }
        }
        else{
            if (title != null) {
                title.background=ContextCompat.getDrawable(requireContext(),R.color.blue)
            }
        }


        return view
    }

    private fun run(context : Context) {
        val url = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val reponseString = response.body()!!.string()
                //on crée l'object JSON
                val contactJSON= JSONObject(reponseString)
                //on crée le tableau JSON
                val infoJSONArray: JSONArray = contactJSON.getJSONArray("drinks")
                val size:Int = infoJSONArray.length()
                detailsArrayList= ArrayList()
                for (i in 0 until size) {
                    val objectDetailJSON: JSONObject =infoJSONArray.getJSONObject(i)
                    val cocktail= CocktailObject()
                    cocktail.nom=objectDetailJSON.getString("strDrink")
                    cocktail.imageUrl=objectDetailJSON.getString("strDrinkThumb")
                    cocktail.id=objectDetailJSON.getString("idDrink")


                    //on regarde si il est dans la liste des favoris
                    for(y in 0 until favorisArrayList.size){
                        if(favorisArrayList[y] == cocktail.id){

                            cocktail.fav=true
                        }
                    }

                    if( cocktail.fav){
                        detailsArrayList.add(0,cocktail)

                    }else{
                        detailsArrayList.add(cocktail)
                    }

                }

                //on update l'UI
                val objectAdapter =
                    CocktailAdpater( context,detailsArrayList, favorisArrayList)
                activity?.runOnUiThread {
                    detailsListView.adapter = objectAdapter
                }

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()


    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    private val PREFS_FILE_NAME = "com.app.app.prefs"

    private fun saveData() {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_FILE_NAME, 0)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(favorisArrayList)
        editor.putString("cocktailList", json)
        editor.apply()
    }

    //fonction qui charge la liste de favoris
    private fun loadData() {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_FILE_NAME, 0)
        val gson = Gson()
        val json = sharedPreferences.getString("cocktailList", "")
        val type = object: TypeToken<MutableList<String>>() {}.type

        favorisArrayList = if(json == null || json == "")
            ArrayList()
        else
            gson.fromJson(json, type)
    }
}
