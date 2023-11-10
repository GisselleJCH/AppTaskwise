package com.example.apptaskwise
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.ImageButton
import android.content.Intent
import android.content.SharedPreferences
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var themeChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!themeChanged) { // Verifica si el tema ya ha sido cambiado
            changeTheme()
            themeChanged = true // Marca que el tema ha sido cambiado
        }
        setContentView(R.layout.activity_login2)

        //----------------------------------------------

        val showPasswordButton = findViewById<ImageButton>(R.id.showPasswordButton)
        val passwordEditText = findViewById<EditText>(R.id.editTextContraseña)

        var passwordVisible = false // Variable para rastrear la visibilidad de la contraseña

        showPasswordButton.setOnClickListener {
            if (passwordVisible) {
                // Si la contraseña es visible, ocultarla y cambiar el icono
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                showPasswordButton.setImageResource(R.drawable.ojo)
            } else {
                // Si la contraseña está oculta, mostrarla y cambiar el icono
                passwordEditText.transformationMethod = null
                showPasswordButton.setImageResource(R.drawable.ojo)
            }
            // Cambiar el estado de visibilidad
            passwordVisible = !passwordVisible
        }
        //--------------------




        val validEmails = listOf<String>("fonsecakener@gmail.com","GisselleCalero@gmail.com","CelesteRojas@gmail.com")
        val validContra = listOf<String>("Carlitos1","Carlitos2","Carlitos3")

        val loginButton = findViewById<Button>(R.id.botonIniciarSesion)
        loginButton.setOnClickListener{
                val inputEmail = findViewById<EditText>(R.id.editTextCorreo).text.toString()
                val inputContra = findViewById<EditText>(R.id.editTextContraseña).text.toString()

            if (validEmails.contains(inputEmail)&& validContra[validEmails.indexOf(inputEmail)]==inputContra){
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Las credenciales no son validas",Toast.LENGTH_SHORT).show()
            }
        }

        val olvidarContraseñaTextView = findViewById<TextView>(R.id.olvidarContra)
        olvidarContraseñaTextView.setOnClickListener {
            val intent = Intent(this, RecuperarContra::class.java)
            startActivity(intent)
        }

        val RegistrarInicioTextView = findViewById<TextView>(R.id.textView4)
        RegistrarInicioTextView.setOnClickListener {
            val intent = Intent(this, RegistrarInicio::class.java)
            startActivity(intent)
        }

        val botonconfiImageButton = findViewById<ImageButton>(R.id.botonconfi)
        botonconfiImageButton.setOnClickListener {
            val intent = Intent(this, configuracionusario::class.java)
            startActivity(intent)
        }

    }

    private fun changeTheme() {
        // Inicializa las preferencias compartidas
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Obtiene la preferencia de tema actual
        val isNightMode = sharedPreferences.getBoolean("nightMode", false)

        // Cambia el tema en función de la preferencia
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
