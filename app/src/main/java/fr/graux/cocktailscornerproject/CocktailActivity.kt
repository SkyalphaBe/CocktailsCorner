package fr.graux.cocktailscornerproject

import android.util.Log
import fr.graux.cocktailscornerproject.cocktailAPI.cocktailService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CocktailActivity {

    private fun loadCocktails() {
        //initiate the service
        val destinationService  = ServiceBuilder.buildService(cocktailService::class.java)
        val requestCall =destinationService.getCocktailsList()
        //make network call asynchronously
        requestCall.enqueue(object : Callback<List<ObjectCocktail>> {
            override fun onResponse(call: Call<List<ObjectCocktail>>, response: Response<List<ObjectCocktail>>) {
                Log.d("Response", "onResponse: ${response.body()}")
                if (response.isSuccessful){
                    val cocktailList= response.body()!!
                    Log.d("Response", "cocktail : ${cocktailList.size}")
//                    country_recycler.apply {
//                        setHasFixedSize(true)
//                        layoutManager = GridLayoutManager(this@CocktailActivity,2)
//                        adapter = cocktailAdpater(response.body()!!)
//                    }
                }else{
//                    Toast.makeText(this@MainActivity, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<ObjectCocktail>>, t: Throwable) {
//                Toast.makeText(this@MainActivity, "Something went wrong $t", Toast.LENGTH_SHORT).show()
            }
        })
    }
}