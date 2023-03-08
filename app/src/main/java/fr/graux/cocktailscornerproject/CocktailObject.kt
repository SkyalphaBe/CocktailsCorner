package fr.graux.cocktailscornerproject

public class CocktailObject{

    lateinit var nom : String
    lateinit var imageUrl : String
    lateinit var ingredients : String

    constructor(nom: String, imageUrl: String, ingredients : String) {
        this.nom = nom
        this.imageUrl = imageUrl
        this.ingredients = ingredients
    }

    constructor()
}