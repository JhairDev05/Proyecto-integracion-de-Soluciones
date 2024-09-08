package uv.tc.apicupones


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import uv.tc.apicupones.databinding.ActivityLoginBinding
import uv.tc.apicupones.pojo.Cliente
import uv.tc.apicupones.pojo.RespuestaLogin
import uv.tc.apicupones.util.Constantes


class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.activity_login)
        setContentView(view)
        binding.btnIngresar.setOnClickListener {
            if(validarCamposLogin()){
                verificarCredencialesPaciente(binding.edCorreo.text.toString(),binding.edPassword.text.toString())
            }

        }
        binding.tvRegistro.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistroActivity::class.java)
            startActivity(intent)
        }


    }
    fun validarCamposLogin(): Boolean{
        var esCorrecto = true
        val correo = binding.edCorreo.text.toString()
        val password = binding.edPassword.text.toString()
        try {
            if(correo.isEmpty()){
                esCorrecto=false
                binding.edCorreo.error="Correo obligatorio"
            }
            if(password.isEmpty()){
                esCorrecto=false
                binding.edPassword.error="Contraseña obligatoria"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error(true)
        }

        return esCorrecto
    }

    fun verificarCredencialesPaciente(correo: String, password:String){
        Ion.getDefault(this@LoginActivity).conscryptMiddleware.enable(false)
        Ion.with(this@LoginActivity).load("POST", Constantes.URL_WS+"autenticacion/loginApp").setHeader("Content-Type","application/x-www-form-urlencoded")
            .setBodyParameter("correo", correo)
            .setBodyParameter("password",password)
            .asString()
            .setCallback { e, result ->
                if (e==null && result!=null){
                    //200
                    serializarRespuestaLogin(result)
                }else{
                    //error
                    Toast.makeText(this@LoginActivity,"Error de conexion, intentalo mas tarde",Toast.LENGTH_LONG).show()
                }
            }
    }
    fun serializarRespuestaLogin(json:String){
        val gson = Gson()
        val respuestaLogin= gson.fromJson(json, RespuestaLogin::class.java)
        Toast.makeText(this@LoginActivity,respuestaLogin.mensaje,Toast.LENGTH_LONG).show()
        if (!respuestaLogin.error){
            irPantallaPrincipal(respuestaLogin.clienteSesion)
        }else{
            Toast.makeText(this@LoginActivity,"Error de conexion, intentalo mas tarde",Toast.LENGTH_LONG).show()
        }
    }

    fun irPantallaPrincipal(cliente: Cliente){
        val gson = Gson()
        val stringCliente =gson.toJson(cliente)
        val intent= Intent(this@LoginActivity,MainActivity::class.java)
        intent.putExtra("cliente",stringCliente)
        startActivity(intent)
        this.finish()
    }


}