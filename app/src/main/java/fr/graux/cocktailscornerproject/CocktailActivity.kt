package fr.graux.cocktailscornerproject

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class CocktailActivity : AppCompatActivity()  {

    lateinit var detailsListView: ListView
    var detailsArrayList:ArrayList<CocktailObject> = ArrayList()
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail)
        detailsListView = findViewById(R.id.listCocktail)
        run()
    }

    private fun run() {
        val url = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val reponseString = response.body()!!.string()
                //creating json object
                val contactJSON= JSONObject(reponseString)
                //creating json array
                val infoJSONArray: JSONArray = contactJSON.getJSONArray("drinks")
                val size:Int = infoJSONArray.length()
                detailsArrayList= ArrayList()
                for (i in 0 until size) {
                    val objectDetailJSON: JSONObject =infoJSONArray.getJSONObject(i)
                    val cocktail= CocktailObject()
                    cocktail.nom=objectDetailJSON.getString("strDrink")
                    cocktail.imageUrl=objectDetailJSON.getString("strDrinkThumb")
                    detailsArrayList.add(cocktail)
                }

                runOnUiThread {
                    //stuff that updates ui
                    val objectAdapter =
                        CocktailAdpater(applicationContext,detailsArrayList)
                    detailsListView.adapter=objectAdapter
                }
            }
        })
    }
}