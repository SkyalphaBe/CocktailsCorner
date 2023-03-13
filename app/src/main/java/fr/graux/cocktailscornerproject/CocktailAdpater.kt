package fr.graux.cocktailscornerproject

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso


class CocktailAdpater(context: Context, arrayListDetails:ArrayList<CocktailObject> ): BaseAdapter() {

    private val layoutInflater: LayoutInflater
    private val arrayListDetails:ArrayList<CocktailObject>

    init {
        this.layoutInflater = LayoutInflater.from(context)
        this.arrayListDetails=arrayListDetails
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

        listRowHolder.cocktailNom.text = arrayListDetails[position].nom
        Picasso.get().load(arrayListDetails[position].imageUrl).into(listRowHolder.cocktailImage)
        listRowHolder.cocktailId.text = arrayListDetails[position].id


        return view
    }

    //classe qui peremt d'initialiser les éléments d'un cocktailItem
    private class ListRowHolder(row: View?) {
         val cocktailNom: TextView
         val cocktailImage: ImageView
         val cocktailId : TextView

        init {
            this.cocktailNom = row?.findViewById(R.id.cocktailNom) as TextView
            this.cocktailImage = row.findViewById(R.id.cocktailImage) as ImageView
            this.cocktailId = row.findViewById(R.id.cocktailId) as TextView
        }
    }

    //classe qui permet de détecter le double click
    abstract class DoubleClickListener : View.OnClickListener {
        var lastClickTime: Long = 0
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


