package uv.tc.apicupones

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uv.tc.apicupones.pojo.Sucursal

class SucursalesAdapter(val sucursales: ArrayList<Sucursal>, val observador: ListaSucursalesActivity): RecyclerView.Adapter<SucursalesAdapter.ViewHolderSucursales>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSucursales {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_sucursales, parent, false)
        return ViewHolderSucursales(itemView)
    }

    override fun getItemCount(): Int {
        return sucursales.size
    }

    override fun onBindViewHolder(holder: ViewHolderSucursales, position: Int) {
        val sucursales = sucursales[position]
        holder.tvNombreSucursal.text = "Nombre de la sucursal: ${sucursales.nombreSucursal}"
        holder.tvDireccion.text = "Dirección: ${sucursales.direccion}"
        holder.tvCodigoPostal.text="CodigoPostal: ${sucursales.codigoPostal}"
        holder.tvTelefono.text="Telefono: ${sucursales.telefono}"

        holder.itemView.setOnClickListener {
            observador.seleccionarItem(position, sucursales)
        }
    }

    class ViewHolderSucursales(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvNombreSucursal: TextView = itemView.findViewById(R.id.tv_nombreSucursal)
        val tvDireccion: TextView = itemView.findViewById(R.id.tv_direccion)
        val tvCodigoPostal: TextView= itemView.findViewById(R.id.tv_codigoPostal)
        val tvTelefono:TextView=itemView.findViewById(R.id.tv_telefono)
    }
}
