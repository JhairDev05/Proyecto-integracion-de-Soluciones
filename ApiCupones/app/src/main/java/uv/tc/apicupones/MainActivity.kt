package uv.tc.apicupones;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import uv.tc.apicupones.databinding.ActivityMainBinding
import uv.tc.apicupones.pojo.Cliente

class MainActivity : AppCompatActivity() {
    private var cliente = Cliente()
    private lateinit var binding: ActivityMainBinding
    private var clienteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val jsonCliente = intent.getStringExtra("cliente")
        val gson = Gson()

        if (!jsonCliente.isNullOrEmpty()) {
            cliente = gson.fromJson(jsonCliente, Cliente::class.java)
            clienteId = cliente.idCliente
        } else {
            Log.e("MainActivity", "Error: Datos del cliente nulos o vacíos")

            finish()
        }

        val ivPerfil = findViewById<ImageView>(R.id.ivPerfil)
        ivPerfil.setOnClickListener {
            val intent = Intent(this@MainActivity, EditarActivity::class.java)
            intent.putExtra("clienteId", clienteId)
            intent.putExtra("cliente", gson.toJson(cliente))
            startActivity(intent)
        }

        binding.ivPromos.setOnClickListener {
            val intent = Intent(this@MainActivity, ListaPromosActivity::class.java)
            startActivity(intent)
        }
    }
}
