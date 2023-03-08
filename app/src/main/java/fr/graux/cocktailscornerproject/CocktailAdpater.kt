package fr.graux.cocktailscornerproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class cocktailAdpater(context: Context, arrayListDetails:ArrayList<CocktailObject> ): BaseAdapter() {

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
        return arrayListDetails.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val listRowHolder: ListRowHolder
        if (convertView == null) {
            view = this.layoutInflater.inflate(R.layout.cocktailitem, parent, false)
            listRowHolder = ListRowHolder(view)
            view.tag = listRowHolder
        } else {
            view = convertView
            listRowHolder = view.tag as ListRowHolder
        }

        listRowHolder.cocktailNom.text = arrayListDetails.get(position).nom
        Picasso.get().load(arrayListDetails.get(position).imageUrl).into(listRowHolder.cocktailImage)


        return view
    }

    private class ListRowHolder(row: View?) {
        public val cocktailNom: TextView
        public val cocktailImage: ImageView

        init {
            this.cocktailNom = row?.findViewById<TextView>(R.id.cocktailNom) as TextView
            this.cocktailImage = row?.findViewById<ImageView>(R.id.cocktailImage) as ImageView
        }
    }
}
