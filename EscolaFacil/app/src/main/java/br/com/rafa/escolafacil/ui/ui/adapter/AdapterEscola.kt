package br.com.rafa.escolafacil.ui.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import br.com.rafa.escolafacil.R
import br.com.rafa.escolafacil.ui.model.Escolas
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.content_item.view.*

class AdapterEscola(var list:ArrayList<Escolas>):RecyclerView.Adapter<AdapterEscola.ViewHolder>(){

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =LayoutInflater.from(parent?.context).inflate(R.layout.content_item,parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AdapterEscola.ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bindItems(data:Escolas){
            val name:TextView=itemView.findViewById(R.id.txtNome)
            val inep:TextView=itemView.findViewById(R.id.txtInep)
            val categoria:TextView=itemView.findViewById(R.id.txtCategoria)
            val thumbnail: ImageView =itemView.findViewById(R.id.thumbnail)
            val image2: ImageView =itemView.findViewById(R.id.imageView)

            name.text=data.name
            inep.text=data.inep.toString()
            categoria.text=data.categoria
            Glide.with(itemView.context).load(data.thumbnail).into(thumbnail)

            image2.setOnClickListener {
                Toast.makeText(itemView.context,"${data.name}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
