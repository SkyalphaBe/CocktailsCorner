package fr.graux.cocktailscornerproject

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import fr.graux.cocktailscornerproject.databinding.DetailCocktailBinding
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class CocktailDetailsPage : AppCompatActivity() {

    private val client = OkHttpClient()

    private lateinit var binding: DetailCocktailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DetailCocktailBinding.inflate(layoutInflater)
        val view = binding.root

        val extras = intent.extras
        val id: String? = extras?.getString("id")
        val btnBack = binding.btnBack
        val nomCocktailView = binding.detailCocktailNom

        val uiModeManager: UiModeManager = applicationContext
            .getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val mode:Int = uiModeManager.nightMode
        if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            btnBack.background=ContextCompat.getDrawable(this,R.drawable.btn_back_dark)
            nomCocktailView.background=ContextCompat.getDrawable(this,R.drawable.btn_back_dark)
        }
        else if(mode==UiModeManager.MODE_NIGHT_YES
            && AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_UNSPECIFIED){
            btnBack.background=ContextCompat.getDrawable(this,R.drawable.btn_back_dark)
            nomCocktailView.background=ContextCompat.getDrawable(this,R.drawable.btn_back_dark)
        }
        else{
            btnBack.background=ContextCompat.getDrawable(this,R.drawable.btn_back)
            nomCocktailView.background=ContextCompat.getDrawable(this,R.drawable.btn_back)
        }

        run(id)

        setContentView(view)

        btnBack.setOnClickListener {
            finish()
        }

    }

    private fun run( id: String?) {

        //on crée un url avec l'id de la boisson qui a été clicker
        val url = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=$id"
        //on crée la requette
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                //on rècupére la réponse optenue avec l'appel du lien de l'api
                val reponseString = response.body()!!.string()
                //on crée l'object JSON
                val contactJSON = JSONObject(reponseString)
                //on crée le tableau JSON
                val infoJSONArray: JSONArray = contactJSON.getJSONArray("drinks")



                //on traite le JSON
                val objectDetailJSON: JSONObject = infoJSONArray.getJSONObject(0)

                //on crée la liste pour les ingrédients :
                val listIng = mutableListOf<String>()
                //on charge tous les éléments de la view
                val imageView = findViewById<ImageView>(R.id.detailCocktailImage)
                val titre = findViewById<TextView>(R.id.detailCocktailNom)
                val categories = findViewById<TextView>(R.id.detailCocktailCat)
                val alcoolique = findViewById<TextView>(R.id.detailCocktailAlcoolique)
                val instructions = findViewById<TextView>(R.id.detailCocktailInstuction)
                val ingredients = findViewById<ListView>(R.id.detailCocktailList)
                val verre =findViewById<TextView>(R.id.detailCocktailVerre)
                //on update l'ui avec les données de l'API pour le cocktail selectionée
                runOnUiThread {


                    //on remplis le verre :
                    verre.append(" "+objectDetailJSON.getString("strGlass"))

                    //on voit si un nom alternatif existe si oui on le rajoute au titre sinon on met juste le nom
                    if(objectDetailJSON.getString("strDrinkAlternate") != "null"){
                        titre.text = objectDetailJSON.getString("strDrink") + " or " + objectDetailJSON.getString("strDrinkAlternate")
                    }else{
                        titre.text = objectDetailJSON.getString("strDrink")
                    }

                    //on regarde si la boisson est alcoolisée
                    if(objectDetailJSON.getString("strAlcoholic")=="Alcoholic"){
                        alcoolique.append(" yes")
                    }else{
                        alcoolique.append( "no")
                    }
                    categories.append(" "+objectDetailJSON.getString("strCategory"))


                    if (objectDetailJSON.getString("strInstructionsFR") != "null") {
                        instructions.append(" "+objectDetailJSON.getString("strInstructionsFR"))
                    } else {
                        instructions.append(" "+objectDetailJSON.getString("strInstructions"))
                    }
                    for (i in 1..15) {
                        val ingredient = objectDetailJSON.getString("strIngredient$i") + " "
                        val mesure = objectDetailJSON.getString("strMeasure$i")

                        if (mesure != "null " && ingredient != "null ") {
                            Log.d("INGREDIENT1", ingredient)
                            listIng.add(ingredient + mesure)
                        } else if (ingredient != "null ") {
                            Log.d("INGREDIENT2", ingredient)
                            listIng.add(ingredient)
                        }
//                        listIng.add(ingredient + mesure)

                        }




                    // on below line we are initializing adapter for our list view.
                    val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
                        this@CocktailDetailsPage,
                        android.R.layout.simple_list_item_1,
                        listIng as List<String?>
                    )

                    // on below line we are setting adapter for our list view.
                    ingredients.adapter = adapter







                    //fonction qui permet de load l'image à partir d'un lien
                    val uiHandler = Handler(Looper.getMainLooper())
                    uiHandler.post {
                        Picasso.get().load(objectDetailJSON.getString("strDrinkThumb"))
                            .into(imageView)
                    }

                }
            }
        })
    }


}