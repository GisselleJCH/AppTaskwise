package com.example.apptaskwise

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

data class Lista(val nombre: String) // Clase Lista

class Home : AppCompatActivity() {

    private val listas = mutableListOf<Lista>() // Lista de listas
    private lateinit var contenedorListas: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        contenedorListas = findViewById(R.id.contenedorListas)

        val botonLista = findViewById<TextView>(R.id.botonlista)

        // Manejar clic en "Nueva Lista"
        botonLista.setOnClickListener {
            mostrarDialogoNuevaLista()
        }
    }

    // Función para mostrar un cuadro de diálogo para ingresar el nombre de la nueva lista
    private fun mostrarDialogoNuevaLista() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nueva Lista")

        // Configurar el cuadro de diálogo para ingresar el nombre de la lista
        val input = EditText(this)
        input.hint = "Ingresa el nombre de la lista"
        builder.setView(input)

        // Configurar los botones del cuadro de diálogo
        builder.setPositiveButton("Crear") { _, _ ->
            val nombreLista = input.text.toString()
            if (nombreLista.isNotEmpty()) {
                val nuevaLista = Lista(nombreLista)
                listas.add(nuevaLista)

                // Log para verificar si la lista se agregó correctamente
                Log.d("Home", "Lista agregada: ${nuevaLista.nombre}")

                // Actualizar la interfaz de usuario para mostrar las listas
                mostrarListas()
            }
        }

        builder.setNegativeButton("Cancelar") { _, _ ->
            // No es necesario hacer nada aquí, el cuadro de diálogo se cerrará automáticamente
        }

        // Mostrar el cuadro de diálogo
        builder.show()
    }

    // Función para actualizar la interfaz de usuario y mostrar las listas
    private fun mostrarListas() {
        // Limpiar cualquier contenido anterior
        contenedorListas.removeAllViews()

        // Mostrar las listas en el LinearLayout y el nombre de cada lista
        for (i in listas.indices) {
            val lista = listas[i]

            val listaView = TextView(this)
            listaView.text = "( ${i + 1} )  ${lista.nombre}"
            listaView.textSize = 18f
            listaView.setTextColor(ContextCompat.getColor(this, R.color.black))
            listaView.background = getDrawable(R.drawable.background_rounded_edittext)

            // Agregar el nombre de la lista al LinearLayout
            contenedorListas.addView(listaView)
        }
    }

}
