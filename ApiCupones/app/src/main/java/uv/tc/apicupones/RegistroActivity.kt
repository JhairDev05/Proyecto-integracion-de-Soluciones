package uv.tc.apicupones

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.apicupones.databinding.ActivityRegistroBinding
import uv.tc.apicupones.pojo.Cliente
import uv.tc.apicupones.pojo.Mensaje
import uv.tc.apicupones.util.Constantes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistroActivity : AppCompatActivity() {

    private lateinit var nombre :EditText
    private lateinit var apellidoPaterno:EditText
    private lateinit var apellidoMaterno:EditText
    private lateinit var telefono: EditText
    private lateinit var correo: EditText
    private lateinit var direccion:EditText
    private lateinit var password:EditText
    private lateinit var edFechaNacimiento: EditText
    private lateinit var btnFechaNacimiento: Button
    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        nombre=binding.edNombre
        apellidoPaterno=binding.edApellidoPaterno
        apellidoMaterno=binding.edApellidoMaterno
        telefono=binding.edTelefono
        correo=binding.edEmail
        direccion=binding.edDireccion
        password= binding.edPassword
        edFechaNacimiento = binding.edFechaNacimiento
        btnFechaNacimiento = binding.btnFechaNacimiento

        btnFechaNacimiento.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnRegistrar.setOnClickListener {
            if (validarCamposRegistro()) {
                val cliente = obtenerDatosRegistro()
                registrarCliente(cliente)
            }
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

    private fun validarCamposRegistro(): Boolean {
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


    private fun obtenerDatosRegistro(): Cliente {
        val cliente = Cliente()
        cliente.nombre = nombre.text.toString()
        cliente.apellidoPaterno = apellidoPaterno.text.toString()
        cliente.apellidoMaterno = apellidoMaterno.text.toString()
        cliente.telefono = telefono.text.toString()
        cliente.correo = correo.text.toString()
        cliente.direccion = direccion.text.toString()
        cliente.password = password.text.toString()
        val fechaNacimientoText = edFechaNacimiento.text.toString()
        cliente.fechaNacimiento = convertirFormatoFecha(fechaNacimientoText)

        return cliente
    }

    private fun convertirFormatoFecha(fecha: String): String {
        try {
            val formatoEntrada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaDate = formatoEntrada.parse(fecha)

            val formatoSalida = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
            return formatoSalida.format(fechaDate)
        } catch (e: Exception) {
            e.printStackTrace()
            return fecha
        }
    }

    private fun registrarCliente(cliente: Cliente) {
        val gson = Gson()
        val jsonCliente = gson.toJson(cliente)
        Ion.with(this@RegistroActivity)
            .load("POST", "${Constantes.URL_WS}cliente/registrar")
            .setHeader("Content-Type", "application/json")
            .setStringBody(jsonCliente)
            .asString()
            .setCallback { e, result ->
                if (e == null && result != null) {
                    verificarResultadoRegistro(result)
                } else {
                    Toast.makeText(
                        this@RegistroActivity,
                        "Error en la solicitud para registrar el cliente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun verificarResultadoRegistro(resultado: String) {
        val gson = Gson()
        val mensaje: Mensaje = gson.fromJson(resultado, Mensaje::class.java)
        Toast.makeText(this@RegistroActivity, mensaje.mensaje, Toast.LENGTH_LONG).show()
        if (!mensaje.error) {
            val intent = Intent(this@RegistroActivity, LoginActivity::class.java)
            Toast.makeText(this@RegistroActivity, "Se cuenta se ha registrado correctamente volviendo al login", Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()
        }
    }
}
