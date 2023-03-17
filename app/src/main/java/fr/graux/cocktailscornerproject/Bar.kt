package fr.graux.cocktailscornerproject

class Bar() {
    var lat:Double = 0.0
    var lng:Double = 0.0
    lateinit var name:String
    var favorite:Boolean=false

    override fun toString(): String {
        return "Bar(lat=$lat, lng=$lng, name='$name', favorite=$favorite)"
    }


}