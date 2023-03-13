package fr.graux.cocktailscornerproject

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
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


    }

    private fun run(context: Context, id: String?) {

        val url = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=$id"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val reponseString = response.body()!!.string()
                //on crée l'object JSON
                val contactJSON = JSONObject(reponseString)
                //on crée le tableau JSON
                val infoJSONArray: JSONArray = contactJSON.getJSONArray("drinks")
                val size: Int = infoJSONArray.length()
                detailsArrayList = ArrayList()

                //on traite le JSON
                val objectDetailJSON: JSONObject = infoJSONArray.getJSONObject(0)

                //on charge tous les éléments de la view
                val imageView = findViewById<ImageView>(R.id.detailCocktailImage)
                val titre = findViewById<TextView>(R.id.detailCocktailNom)
                val categories = findViewById<TextView>(R.id.detailCocktailCat)
                val alcoolique = findViewById<TextView>(R.id.detailCocktailAlcoolique)
                val instructions = findViewById<TextView>(R.id.detailCocktailInstuction)
                val ingredients = findViewById<ListView>(R.id.detailCocktailList)


                runOnUiThread {
                    // Stuff that updates the UI

                    titre.text = objectDetailJSON.getString("strDrink")
                    alcoolique.text=objectDetailJSON.getString("strAlcoholic")
                    categories.append(objectDetailJSON.getString("strCategory"))


                    if (objectDetailJSON.getString("strInstructionsFR") != "null") {
                        instructions.append(objectDetailJSON.getString("strInstructionsFR"))
                    } else {
                        instructions.append(objectDetailJSON.getString("strInstructions"))
                    }








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