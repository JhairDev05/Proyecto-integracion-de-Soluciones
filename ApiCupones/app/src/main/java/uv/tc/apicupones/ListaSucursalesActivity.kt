package uv.tc.apicupones

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion
import uv.tc.apicupones.databinding.ActivityListaSucursalesBinding
import uv.tc.apicupones.Interfaces.NotificarClic
import uv.tc.apicupones.Interfaces.NotificarClic2
import uv.tc.apicupones.pojo.Promocion
import uv.tc.apicupones.pojo.Sucursal
import uv.tc.apicupones.util.Constantes

class ListaSucursalesActivity : AppCompatActivity(), NotificarClic2 {
    private lateinit var binding: ActivityListaSucursalesBinding
    private var listaSucursales: ArrayList<Sucursal> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaSucursalesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val idSucursal = intent.getIntExtra("idSucursal", 0)

        consultarInfoSucursales(idSucursal)
    }

    fun consultarInfoSucursales(idSucursal: Int) {
        Ion.with(this@ListaSucursalesActivity)
            .load("GET", "${Constantes.URL_WS}promocion/obtenerDetallesSucursales/$idSucursal")
            .asString()
            .setCallback { e, result ->
                if (e == null && result != null) {
                    Log.d("DEBUG", "Respuesta del servidor: $result")
                    Log.d("Debig", "Id sucursal: $idSucursal")
                    serializarInfoSucursales(result)
                    cargarInfoRecycler()
                } else {
                    Log.e("ERROR", "Error al obtener sucursales: $e")
                }
            }
    }

    fun serializarInfoSucursales(json: String) {
        Log.d("DEBUG", "JSON de sucursales: $json")
        val gson = Gson()
        val sucursalType = object : TypeToken<ArrayList<Sucursal>>() {}.type
        val listaSucursalesNuevas: ArrayList<Sucursal> = gson.fromJson(json, sucursalType)
        listaSucursales.clear()
        listaSucursales.addAll(listaSucursalesNuevas)

        Toast.makeText(this@ListaSucursalesActivity, "Sucursales: ${listaSucursales.size}", Toast.LENGTH_LONG).show()
    }

    fun cargarInfoRecycler() {
        binding.recyclerSucursales.layoutManager = LinearLayoutManager(this@ListaSucursalesActivity)
        binding.recyclerSucursales.setHasFixedSize(true)
        if (listaSucursales.size > 0) {
            binding.tvSinSucursales.visibility = View.GONE
            binding.recyclerSucursales.adapter = SucursalesAdapter(listaSucursales, this)
        }
    }

    override fun seleccionarItem(posicion: Int, sucursal: Sucursal) {
        Toast.makeText(this@ListaSucursalesActivity, "Posición: $posicion", Toast.LENGTH_LONG).show()
    }

    fun mostrarInfoSucursal(sucursal: Sucursal) {
        val alert = AlertDialog.Builder(this@ListaSucursalesActivity)
        alert.setTitle("Sucursal seleccionada")
        alert.setMessage("${sucursal.nombreSucursal}")
        alert.setPositiveButton("Ver detalle", DialogInterface.OnClickListener { dialogInterface, i ->
            TODO()
        })
        alert.setNegativeButton("Cerrar", null)
        val dialogo = alert.create()
        dialogo.show()
    }
}