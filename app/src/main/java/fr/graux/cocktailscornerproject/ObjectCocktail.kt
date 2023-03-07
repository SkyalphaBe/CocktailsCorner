package fr.graux.cocktailscornerproject

public class ObjectCocktail{

    lateinit var nom : String
    lateinit var imageUrl : String

    constructor(nom: String, imageUrl: String) {
        this.nom = nom
        this.imageUrl = imageUrl
    }

    constructor()
}