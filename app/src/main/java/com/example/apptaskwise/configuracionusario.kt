package com.example.apptaskwise

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class configuracionusario : AppCompatActivity() {
    private lateinit var textView19: TextView
    private lateinit var textView20: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var nightMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracionusario)

        supportActionBar?.hide()

        textView19 = findViewById(R.id.textView19)
        textView20 = findViewById(R.id.textView20)

        // Inicializa las preferencias compartidas
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        // Obtiene el valor actual de nightMode
        nightMode = sharedPreferences.getBoolean("nightMode", false)
        updateTextAndTheme()

        textView19.setOnClickListener {
            nightMode = true
            updateTextAndTheme()
        }

        textView20.setOnClickListener {
            nightMode = false
            updateTextAndTheme()
        }

        val RegistrarInicioTextView = findViewById<TextView>(R.id.Lenguaje)
        RegistrarInicioTextView.setOnClickListener {
            val intent = Intent(this, lenguajetext::class.java)
            startActivity(intent)
        }

        val privacidadmove = findViewById<TextView>(R.id.textView25)
        privacidadmove.setOnClickListener{
            val intent = Intent(this, privacidad::class.java)
            startActivity(intent)
        }

        val terminosLicens = findViewById<TextView>(R.id.textView26)
        terminosLicens.setOnClickListener{
            val intent = Intent(this, terminosLicencia::class.java)
            startActivity(intent)
        }



    }

    private fun updateTextAndTheme() {

        textView19.text = if (nightMode) "Oscuro (Actual)" else "Oscuro"
        textView20.text = if (!nightMode) "Claro (Actual)" else "Claro"


        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        editor.putBoolean("nightMode", nightMode)
        editor.apply()
    }
}
