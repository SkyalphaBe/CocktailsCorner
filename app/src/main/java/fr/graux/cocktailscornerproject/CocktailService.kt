package fr.graux.cocktailscornerproject.cocktailAPI

import fr.graux.cocktailscornerproject.ObjectCocktail
import retrofit2.Call
import retrofit2.http.GET

//une interface qui contient une fonction pour récupérer une liste de cocktail
interface cocktailService {
    @GET("cocktails")
    fun getCocktailsList () : Call<List<ObjectCocktail>>

}