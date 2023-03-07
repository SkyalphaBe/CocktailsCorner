package fr.graux.cocktailscornerproject

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//un object qui va nous aider à créer notre service avec retrofit et l'interface
object ServiceBuilder {
    //l'url de l'api
    private const val URL ="www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic"
    //on créer le client http
    private val okHttp =OkHttpClient.Builder()

    //le builder de retrofit
    private val builder =Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    //on créer l'instace de retrofit
    private val retrofit = builder.build()



    fun <T> buildService (serviceType :Class<T>):T{
        return retrofit.create(serviceType)
    }
}