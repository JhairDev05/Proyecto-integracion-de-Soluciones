package uv.tc.apicupones

import android.R
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import uv.tc.apicupones.databinding.ActivityListaPromosBinding
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion
import uv.tc.apicupones.pojo.Promocion
import uv.tc.apicupones.util.Constantes
import uv.tc.apicupones.Interfaces.NotificarClic
import uv.tc.apicupones.pojo.Cliente

class ListaPromosActivity : AppCompatActivity() ,NotificarClic {
    private lateinit var binding: ActivityListaPromosBinding
    private var listaPromociones: ArrayList<Promocion> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityListaPromosBinding.inflate(layoutInflater)
        val view=binding.root
        val categorias = arrayOf("Todas", "LineaBlanca", "Electronica", "Deportes","Bebidas")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategorias.adapter = adapter
        binding.spinnerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val categoriaSeleccionada = categorias[position]
                consultarInfoPromos(categoriaSeleccionada)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

         //svBusqueda = findViewById<SearchView>(R.id.sv)
        binding.svBusqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                consultarInfoPromos(null, query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        setContentView(view)
        consultarInfoPromos()

    }

    private fun consultarInfoPromos(categoria: String? = null, busqueda: String? = null) {
        val url = when {
            categoria != null && categoria != "Todas" ->
                "${Constantes.URL_WS}promocion/obtenerListaPromociones?categoria=$categoria"
            busqueda != null && busqueda.isNotEmpty() ->
                "${Constantes.URL_WS}promocion/buscarPromociones?busqueda=$busqueda"
            else ->
                "${Constantes.URL_WS}promocion/obtenerListaPromociones"
        }

        Ion.with(this@ListaPromosActivity)
            .load("GET", url)
            .asString()
            .setCallback { e, result ->
                if (e == null && result != null) {
                    serializarInfoElementos(result)
                    cargarInfoRecycler()
                } else {
                    Toast.makeText(
                        this@ListaPromosActivity,
                        "Por el momento no se puede cargar la informacion, intentelo mas tarde",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }



    fun serializarInfoElementos(json: String) {
        val gson = Gson()
        val typeList = object : TypeToken<ArrayList<Promocion>>() {}.type
        listaPromociones = gson.fromJson(json, typeList)
        Toast.makeText(this@ListaPromosActivity, "Promociones: ${listaPromociones.size}", Toast.LENGTH_LONG).show()
    }

    fun cargarInfoRecycler() {
        binding.recyclerPromos.layoutManager = LinearLayoutManager(this@ListaPromosActivity)
        binding.recyclerPromos.setHasFixedSize(true)
        if (listaPromociones.size > 0) {
            binding.tvSinPromos.visibility = View.GONE
            binding.recyclerPromos.adapter = PromosAdapter(listaPromociones, this)
        }
    }

    override fun seleccionarItem(posicion: Int, promocion: Promocion) {
        mostrarInfoPromos(promocion)

    }


    fun mostrarInfoPromos(promocion:Promocion){
        val alert = AlertDialog.Builder(this@ListaPromosActivity)
        alert.setTitle("Promocion seleccionada")
        alert.setMessage("${promocion.nombrePromo} por parte de:  ${promocion.nombreEmpresa}")
        alert.setPositiveButton("Ver detalle", DialogInterface.OnClickListener { dialogInterface,i ->
            val intent = Intent(this@ListaPromosActivity, DetallePromocionActivity::class.java)
            intent.putExtra("idPromocion", promocion.idPromocion)
            intent.putExtra("nombrePromo", promocion.nombrePromo)
            intent.putExtra("descripcion", promocion.descripcion)
            intent.putExtra("fechaInicio", promocion.fechaInicioPromo)
            intent.putExtra("fechaFin", promocion.fechaFinPromo)
            intent.putExtra("restricciones", promocion.restricciones)
            intent.putExtra("nombrePromocion", promocion.nombrePromocion)
            intent.putExtra("cantidadRebajada", promocion.cantidadRebajada)
            intent.putExtra("numeroCupones", promocion.numeroCupones)
            intent.putExtra("codigoPromocion", promocion.codigoPromocion)
            intent.putExtra("fotoBase64",promocion.fotoBase64)
            intent.putExtra("idSucursal",promocion.idSucursal)
            Log.d("DEBUG", "idSucursal enviado desde ListaPromosActivity: $promocion.idSucursal")
            startActivity(intent)
        })
        alert.setNegativeButton("Cerrar",null)
        val dialogo= alert.create()
        dialogo.show()
    }



}