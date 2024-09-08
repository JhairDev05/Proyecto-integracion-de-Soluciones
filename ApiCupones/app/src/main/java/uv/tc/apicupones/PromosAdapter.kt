package uv.tc.apicupones

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uv.tc.apicupones.pojo.Promocion

class PromosAdapter(val promos: ArrayList<Promocion>, val observador: ListaPromosActivity): RecyclerView.Adapter<PromosAdapter.ViewHolderPromos>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPromos {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_promos,parent,false)
        return ViewHolderPromos(itemView)
    }

    override fun getItemCount(): Int {
        return promos.size
    }

    override fun onBindViewHolder(holder: ViewHolderPromos, position: Int) {
        val promos = promos[position]
        holder.tvPromo.text="Promocion: ${promos.nombrePromo}"
        holder.tvEmpresa.text="Empresa: ${promos.nombreEmpresa}"
        holder.tvTipo.text="Tipo de promo: ${promos.nombrePromocion}"
        holder.tvValor.text="Valor del descuento: ${promos.cantidadRebajada}"
        holder.tvCantidad.text="Cantidad: ${promos.numeroCupones}"
        holder.tvVigencia.text="Valido hasta: ${promos.fechaFinPromo}"
        holder.itemView.setOnClickListener {
            observador.seleccionarItem(position, promos)
        }

    }

    class ViewHolderPromos(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvPromo : TextView= itemView.findViewById(R.id.tv_promo)
        val tvTipo : TextView=itemView.findViewById(R.id.tv_tipo)
        val tvEmpresa: TextView=itemView.findViewById(R.id.tv_empresa)
        val tvValor: TextView=itemView.findViewById(R.id.tv_valor)
        val tvCantidad: TextView=itemView.findViewById(R.id.tv_cantidad)
        val tvVigencia: TextView=itemView.findViewById(R.id.tv_vigencia)
    }

}

