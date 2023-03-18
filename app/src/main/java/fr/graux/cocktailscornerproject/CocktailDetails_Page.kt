package fr.graux.cocktailscornerproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import fr.graux.cocktailscornerproject.databinding.DetailCocktailBinding
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class CocktailDetails_Page : AppCompatActivity() {

    lateinit var detailsListView: ListView
    var detailsArrayList: ArrayList<CocktailObject> = ArrayList()
    private val client = OkHttpClient()

    private lateinit var binding: DetailCocktailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DetailCocktailBinding.inflate(layoutInflater)
        val view = binding.root

        val extras = intent.extras
        val id: String? = extras?.getString("id")


        run(view.context, id)

        setContentView(view)

        binding.btnBack.setOnClickListener {
            val intent = Intent(view.context, Cocktail_Page::class.java)
            ContextCompat.startActivity(view.context, intent, null)
        }

    }

    private fun run(context: Context, id: String?) {

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
                val size: Int = infoJSONArray.length()



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

                //on update l'ui avec les données de l'API pour le cocktail selectionée
                runOnUiThread {


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
                    categories.append(" ")
                    categories.append(objectDetailJSON.getString("strCategory"))


                    if (objectDetailJSON.getString("strInstructionsFR") != "null") {
                        instructions.append(" ")
                        instructions.append(objectDetailJSON.getString("strInstructionsFR"))
                    } else {
                        instructions.append(" ")
                        instructions.append(objectDetailJSON.getString("strInstructions"))
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
                        this@CocktailDetails_Page,
                        android.R.layout.simple_list_item_1,
                        listIng as List<String?>
                    )

                    // on below line we are setting adapter for our list view.
                    ingredients.adapter = adapter







                    //fonction qui permet de load l'image à partir d'un lien
                    val uiHandler = Handler(Looper.getMainLooper())
                    uiHandler.post(Runnable {
                        Picasso.get().load(objectDetailJSON.getString("strDrinkThumb"))
                            .into(imageView)
                    })

                }
            }
        })
    }


}