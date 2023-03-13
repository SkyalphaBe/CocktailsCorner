package fr.graux.cocktailscornerproject


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import fr.graux.cocktailscornerproject.databinding.ActivityMainBinding
import fr.graux.cocktailscornerproject.databinding.DetailCocktailBinding
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class Cocktail_Page : Fragment(R.layout.fragment_cocktail__page){


    lateinit var detailsListView: ListView
    var detailsArrayList:ArrayList<CocktailObject> = ArrayList()
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_cocktail__page,container,false)
        detailsListView = view.findViewById(R.id.listCocktail)

        run(view.context)

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
                    detailsArrayList.add(cocktail)
                }

                //on update l'UI
                val objectAdapter =
                    CocktailAdpater( context,detailsArrayList)
                activity?.runOnUiThread {
                    detailsListView.adapter = objectAdapter
                }

            }
        })
    }


}
