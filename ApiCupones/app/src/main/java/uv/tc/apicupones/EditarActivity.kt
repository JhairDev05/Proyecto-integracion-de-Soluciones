package uv.tc.apicupones

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.apicupones.databinding.ActivityEditarBinding
import uv.tc.apicupones.pojo.Cliente
import uv.tc.apicupones.pojo.Mensaje
import uv.tc.apicupones.util.Constantes
import java.util.Calendar

class EditarActivity : AppCompatActivity() {
    private var cliente = Cliente()
    private var isEdicion: Boolean=false
    private lateinit var binding: ActivityEditarBinding
    private lateinit var nombre : EditText
    private lateinit var apellidoPaterno: EditText
    private lateinit var apellidoMaterno: EditText
    private lateinit var telefono: EditText
    private lateinit var correo: EditText
    private lateinit var direccion: EditText
    private lateinit var password: EditText
    private lateinit var edFechaNacimiento: EditText
    private lateinit var btnFechaNacimiento: Button
    private lateinit var ivRegresar: ImageView
    private lateinit var btnCancelar: Button
    private lateinit var btnEditar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        obtenerDatosCliente()
        if(cliente!=null){
            cargarInformacionCliente()
        }
        nombre=binding.edNombre
        apellidoPaterno=binding.edApellidoPaterno
        apellidoMaterno=binding.edApellidoMaterno
        telefono=binding.edTelefono
        correo=binding.edEmail
        direccion=binding.edDireccion
        password= binding.edPassword
        edFechaNacimiento = binding.edFechaNacimiento
        btnFechaNacimiento = binding.btnFechaNacimiento
        btnEditar=binding.btnEditar
        ivRegresar=binding.ivRegresar
        btnCancelar=binding.btnCancelar


        if (cliente!=null) {
            mostrarDatosEnFormulario(cliente)
        }else{
            Toast.makeText(this, "Error al recibir los datos del cliente", Toast.LENGTH_SHORT)
                .show()
            finish()
        }

        btnFechaNacimiento.setOnClickListener {
            showDatePickerDialog()
        }

       binding.ivRegresar.setOnClickListener {
           val gson = Gson()
           val intent = Intent()
           intent.putExtra("cliente", gson.toJson(cliente))
           setResult(RESULT_OK, intent)
           finish()
       }

        binding.btnCancelar.setOnClickListener {
            val gson = Gson()
            val intent = Intent()
            intent.putExtra("cliente", gson.toJson(cliente))
            setResult(RESULT_OK, intent)
            finish()
        }


    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                edFechaNacimiento.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    override fun onResume() {
        super.onResume()
        binding.btnEditar.setOnClickListener {
            if(validarCamposEdicion()){
                cliente.nombre=binding.edNombre.text.toString()
                cliente.apellidoPaterno= binding.edApellidoPaterno.text.toString()
                cliente.apellidoMaterno= binding.edApellidoMaterno.text.toString()
                cliente.telefono= binding.edTelefono.text.toString()
                cliente.correo= binding.edEmail.text.toString()
                cliente.direccion= binding.edDireccion.text.toString()
                cliente.password= binding.edPassword.text.toString()
                cliente.fechaNacimiento= binding.edFechaNacimiento.text.toString()
                cliente.idCliente=cliente.idCliente
                enviarInformacionActualizacion(cliente)
            }
        }
    }


    private fun mostrarDatosEnFormulario(cliente: Cliente) {
        nombre.setText(cliente.nombre)
        apellidoPaterno.setText(cliente.apellidoPaterno)
        apellidoMaterno.setText(cliente.apellidoMaterno)
        telefono.setText(cliente.telefono)
        correo.setText(cliente.correo)
        direccion.setText(cliente.direccion)
        password.setText(cliente.password)
        edFechaNacimiento.setText(cliente.fechaNacimiento)
    }


    fun obtenerDatosCliente() {
        val jsonCliente = intent.getStringExtra("cliente")

        if (!jsonCliente.isNullOrEmpty()) {
            val gson = Gson()
            cliente = gson.fromJson(jsonCliente, Cliente::class.java)
        } else {
            Toast.makeText(this, "Error: Datos del cliente nulos o vacíos", Toast.LENGTH_SHORT).show()
            finish()
        }
    }




    fun cargarInformacionCliente(){
        binding.edNombre.setText(cliente.nombre)
        habilitarComponentesEdicion(isEdicion)
    }

    fun habilitarComponentesEdicion(isEdicion:Boolean) {
        if (isEdicion) {
            binding.btnEditar.visibility = View.VISIBLE
        } else {
            binding.btnEditar.visibility = View.VISIBLE
        }
    }


    private fun validarCamposEdicion(): Boolean {
        var camposValidos = true
        val nombreText = nombre.text.toString()
        val apellidoPaternoText = apellidoPaterno.text.toString()
        val apellidoMaternoText = apellidoMaterno.text.toString()
        val telefonoText = telefono.text.toString()
        val correoText = correo.text.toString()
        val direccionText = direccion.text.toString()
        val fechaNacimientoText = edFechaNacimiento.text.toString()
        val passwordText = password.text.toString()

        try {
            if (nombreText.isEmpty()) {
                camposValidos = false
                nombre.error = "Este campo es obligatorio"
            }
            if (apellidoPaternoText.isEmpty()) {
                camposValidos = false
                apellidoPaterno.error = "Este campo es obligatorio"
            }
            if (apellidoMaternoText.isEmpty()) {
                camposValidos = false
                apellidoMaterno.error = "Este campo es obligatorio"
            }
            if (telefonoText.isEmpty()) {
                camposValidos = false
                telefono.error = "Este campo es obligatorio"
            }
            if (correoText.isEmpty()) {
                camposValidos = false
                correo.error = "Este campo es obligatorio"
            }
            if (direccionText.isEmpty()) {
                camposValidos = false
                direccion.error = "Este campo es obligatorio"
            }
            if (fechaNacimientoText.isEmpty()) {
                camposValidos = false
                edFechaNacimiento.error = "Este campo es obligatorio"
            }
            if (passwordText.isEmpty()) {
                camposValidos = false
                password.error = "Este campo es obligatorio"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error(true)
        }

        return camposValidos
    }


    fun enviarInformacionActualizacion(clienteEdicion: Cliente) {
        Log.d("DATOS_CLIENTE", "ID del cliente a actualizar: ${clienteEdicion.idCliente}")
        val gson = Gson()
        val jsonCliente = gson.toJson(clienteEdicion)
        val url = "${Constantes.URL_WS}cliente/editar/${clienteEdicion.idCliente}" // Agrega el ID del cliente a la URL
        Ion.with(this@EditarActivity)
            .load("PUT", url)
            .setHeader("Content-Type", "application/json")
            .setStringBody(jsonCliente)
            .asString()
            .setCallback { e, result ->
                if (e == null && result != null) {
                    verificarResultado(result)
                } else {
                    Toast.makeText(this@EditarActivity, "Error en la solicitud para actualizar la informacion", Toast.LENGTH_LONG).show()
                }
            }
    }


    fun verificarResultado(resultado : String){
        Log.d("RESPUESTA_SERVIDOR", resultado)
        val gson = Gson()
        var msj : Mensaje = gson.fromJson(resultado, Mensaje::class.java)
        Toast.makeText(this@EditarActivity,msj.mensaje,Toast.LENGTH_LONG).show()
        if (msj.error){

        }
    }


}