package com.example.apptaskwise

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate

class RegistrarInicio : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var themeChanged = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!themeChanged) { // Verifica si el tema ya ha sido cambiado
            changeTheme()
            themeChanged = true // Marca que el tema ha sido cambiado
        }
        setContentView(R.layout.activity_registrar_inicio)

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