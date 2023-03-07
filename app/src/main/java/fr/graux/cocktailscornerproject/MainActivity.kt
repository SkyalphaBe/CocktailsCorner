package fr.graux.cocktailscornerproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.widget.ListView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject

import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var listView_details: ListView
    var arrayList_details:ArrayList<ObjectCocktail> = ArrayList();
    val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail)

        listView_details = findViewById<ListView>(R.id.listCocktail) as ListView
        run("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=mojito")






    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                var str_response = response.body()!!.string()
                //creating json object
                val json_contact: JSONObject = JSONObject(str_response)
                //creating json array
                var jsonarray_info: JSONArray = json_contact.getJSONArray("drinks")
                var i:Int = 0
                var size:Int = jsonarray_info.length()
                arrayList_details= ArrayList();
                for (i in 0.. size-1) {
                    var json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)
                    var cocktail:ObjectCocktail= ObjectCocktail();
                    cocktail.nom=json_objectdetail.getString("strDrink")
                    cocktail.imageUrl=json_objectdetail.getString("strDrinkThumb")
                    arrayList_details.add(cocktail)
                }

                runOnUiThread {
                    //stuff that updates ui
                    val obj_adapter : cocktailAdpater
                    obj_adapter = cocktailAdpater(applicationContext,arrayList_details)
                    listView_details.adapter=obj_adapter
                }
            }
        })
    }


}