package fr.graux.cocktailscornerproject

class Bar {
    var lat:Double = 0.0
    var lng:Double = 0.0
    lateinit var name:String

    constructor(lat:Double,lng:Double,name:String) {
       this.lat=lat
       this.lng=lng
       this.name=name
    }
    constructor()
    override fun toString(): String {
        return "$lat,$lng,$name\n"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bar

        if (lat != other.lat) return false
        if (lng != other.lng) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lat.hashCode()
        result = 31 * result + lng.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }


}