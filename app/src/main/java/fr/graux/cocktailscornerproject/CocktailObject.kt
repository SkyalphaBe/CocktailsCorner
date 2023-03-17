package fr.graux.cocktailscornerproject

 class CocktailObject{

    lateinit var nom : String
    lateinit var imageUrl : String
    lateinit var ingredients : String
    lateinit var id  : String

    constructor(id : String, nom: String, imageUrl: String, ingredients : String) {
        this.nom = nom
        this.imageUrl = imageUrl
        this.id= id
        this.ingredients = ingredients
    }

    constructor()
}