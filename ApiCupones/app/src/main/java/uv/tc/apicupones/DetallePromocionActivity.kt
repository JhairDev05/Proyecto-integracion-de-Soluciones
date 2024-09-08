package uv.tc.apicupones

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import uv.tc.apicupones.databinding.ActivityDetallePromocionBinding
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class DetallePromocionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetallePromocionBinding
    private lateinit var nombrePromo: EditText
    private lateinit var descripcion: EditText
    private lateinit var fechaInicio: EditText
    private lateinit var fechaFin: EditText
    private lateinit var restriccion: EditText
    private lateinit var tipoPromo: EditText
    private lateinit var valor: EditText
    private lateinit var numeroCupones: EditText
    protected lateinit var codigo: EditText
    private lateinit var fotoImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePromocionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        fotoImageView = binding.ivFotoPromo
        val fotoBase64 = intent.getStringExtra("fotoBase64")

        descargarFotoBase64().execute(fotoBase64)

        val idPromocion = intent.getIntExtra("idPromocion", 0)
        val nombrePromo = intent.getStringExtra("nombrePromo") ?: ""
        val descripcionPromo = intent.getStringExtra("descripcion") ?: ""
        val fechaInicioPromo = intent.getStringExtra("fechaInicio") ?: ""
        val fechaFinPromo = intent.getStringExtra("fechaFin") ?: ""
        val restricciones = intent.getStringExtra("restricciones") ?: ""
        val nombrePromocion = intent.getStringExtra("nombrePromocion",) ?: ""
        val cantidadRebajada = intent.getFloatExtra("cantidadRebajada", 0.0f)
        val numeroCupones = intent.getIntExtra("numeroCupones", 0)
        val codigoPromocion = intent.getStringExtra("codigoPromocion") ?: ""


        binding.edNombre.setText(nombrePromo)
        binding.edDescripcion.setText(descripcionPromo)
        binding.edFechaInicio.setText(fechaInicioPromo)
        binding.edFechaFin.setText(fechaFinPromo)
        binding.edRestriccion.setText(restricciones)
        binding.edNombrePromocion.setText(nombrePromocion)
        binding.edValor.setText(cantidadRebajada.toString())
        binding.edNumeroCupones.setText(numeroCupones.toString())
        binding.edCodigo.setText(codigoPromocion)


        val idSucursal = intent.getIntExtra("idSucursal", 0)
        Log.d("DEBUG", "idSucursal recibido en DetallePromocionActivity: $idSucursal")
        val btnConsultarSucursal = findViewById<Button>(R.id.btnListaSucursales)

        btnConsultarSucursal.setOnClickListener {
            val intent = Intent(this, ListaSucursalesActivity::class.java)
            intent.putExtra("idSucursal", idSucursal)
            startActivity(intent)
        }

        val btnMostrarCodigo = findViewById<Button>(R.id.btnMostrarCodigo)
        btnMostrarCodigo.setOnClickListener {
            mostrarCodigoPromocion(codigoPromocion)
        }

}



    private inner class descargarFotoBase64 : AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            try {
                val fotoBase64 = params[0]
                val decodedBytes: ByteArray = Base64.decode(fotoBase64, Base64.DEFAULT)
                return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                fotoImageView.setImageBitmap(result)
            } else {
                fotoImageView.setImageResource(R.drawable.warning)
            }
        }
    }

    fun mostrarCodigoPromocion(codigo: String) {
        val alert = AlertDialog.Builder(this@DetallePromocionActivity)
        alert.setTitle("Código de Promoción")
        val textView = TextView(this@DetallePromocionActivity)
        textView.text = codigo
        textView.textSize = 20f // Tamaño del texto en sp (puedes ajustarlo según tus preferencias)
        textView.setPadding(16, 16, 16, 16) // Añadir un poco de relleno
        alert.setView(textView)
        alert.setPositiveButton("Copiar") { _, _ ->
            val clipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = android.content.ClipData.newPlainText("Código de Promoción", codigo)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this@DetallePromocionActivity,"Codigo copiado en el portapapeles",Toast.LENGTH_LONG).show()
        }

        alert.setNegativeButton("Cerrar", null)

        val dialogo = alert.create()
        dialogo.show()
    }

}
