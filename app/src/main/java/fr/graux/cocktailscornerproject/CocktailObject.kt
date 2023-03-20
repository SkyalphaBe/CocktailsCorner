package fr.graux.cocktailscornerproject

class CocktailObject {

    lateinit var nom : String
    lateinit var imageUrl : String
    lateinit var ingredients : String
    lateinit var id  : String
    var fav : Boolean = false


    override fun toString(): String {
        return "CocktailObject(nom='$nom', imageUrl='$imageUrl', ingredients='$ingredients', id='$id', fav=$fav)"
    }
}