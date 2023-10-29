package com.example.apptaskwise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.content.Intent

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

       val olvidarContraseñaTextView = findViewById<TextView>(R.id.olvidarContra)
        olvidarContraseñaTextView.setOnClickListener{
            val intent = Intent(this, RecuperarContra::class.java)
            startActivity(intent)
        }

        val RegistrarInicioTextView = findViewById<TextView>(R.id.textView4)
        RegistrarInicioTextView.setOnClickListener {
            val intent = Intent(this, RegistrarInicio::class.java)
            startActivity(intent)
        }

    }
}