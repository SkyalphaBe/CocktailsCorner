package fr.graux.cocktailscornerproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso

//une classe qui permet de créer et remplir les objets de la liste de cocktail
class CocktailAdpater(context: Context, arrayListDetails:ArrayList<CocktailObject>, favorisArrayList:ArrayList<String> ): BaseAdapter() {

    //le layout inflater pour créer les objets de la liste
    private val layoutInflater: LayoutInflater
    //l'array list qui stocke le cocktail
    private val arrayListDetails:ArrayList<CocktailObject>
    //l'array list qui stocke les favoris
    private val favorisArrayList:ArrayList<String>
    init {
        this.layoutInflater = LayoutInflater.from(context)
        this.arrayListDetails=arrayListDetails
        this.favorisArrayList= favorisArrayList
    }

    override fun getCount(): Int {
        return arrayListDetails.size
    }

    override fun getItem(position: Int): Any {
        return arrayListDetails[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val listRowHolder: ListRowHolder
        if (convertView == null) {
            //on crée l'item
            view = this.layoutInflater.inflate(R.layout.cocktailitem, parent, false)
            //on utilise lisrowholder pour initialiser les deux champs
            listRowHolder = ListRowHolder(view)
            view.tag = listRowHolder
            //un double click listener pour chaque item
            view.setOnClickListener(object : DoubleClickListener() {
                override fun onDoubleClick(v: View) {
                    val viewId : TextView =v.findViewById(R.id.cocktailId)
                    val intent = Intent(view.context, CocktailDetails_Page::class.java)

                    val id : String = viewId.text as String
                    intent.putExtra("id", id)
                    startActivity(view.context, intent, null)
                }
            })




        } else {
            view = convertView
            listRowHolder = view.tag as ListRowHolder
        }

        //on remplit tous les champs
        listRowHolder.cocktailFav.visibility = View.INVISIBLE
        listRowHolder.cocktailNom.text = arrayListDetails[position].nom
        Picasso.get().load(arrayListDetails[position].imageUrl).into(listRowHolder.cocktailImage)
        listRowHolder.cocktailId.text = arrayListDetails[position].id
        //on vérifie si le cocktail est favoris
        if(arrayListDetails[position].fav){
            listRowHolder.cocktailFav.visibility = View.VISIBLE
        }

        //on met un long click listener pour l'ajout de favori
        view?.setOnLongClickListener {
            if(!arrayListDetails[position].fav){
                arrayListDetails[position].fav=true
                favorisArrayList.add(arrayListDetails[position].id)
                listRowHolder.cocktailFav.visibility = View.VISIBLE
            }else{
                arrayListDetails[position].fav=false

                val iterator = favorisArrayList.iterator()
                while(iterator.hasNext()){
                    val item = iterator.next()
                    if(item == arrayListDetails[position].id){
                        iterator.remove()
                    }
                }
                listRowHolder.cocktailFav.visibility = View.INVISIBLE
            }

            true
        }

        return view
    }

    //classe qui peremt d'initialiser les éléments d'un cocktailItem
    private class ListRowHolder(row: View?) {
         val cocktailNom: TextView
         val cocktailImage: ImageView
         val cocktailId : TextView
         val cocktailFav : ImageView

        init {
            this.cocktailNom = row?.findViewById(R.id.cocktailNom) as TextView
            this.cocktailImage = row.findViewById(R.id.cocktailImage) as ImageView
            this.cocktailId = row.findViewById(R.id.cocktailId) as TextView
            this.cocktailFav = row.findViewById(R.id.cocktailFav) as ImageView
        }
    }

    //classe qui permet de détecter le double click
    abstract class DoubleClickListener : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v)
            }
            lastClickTime = clickTime
        }

        abstract fun onDoubleClick(v: View)

        companion object {
            private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
        }
    }
}


