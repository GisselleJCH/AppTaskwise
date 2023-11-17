package com.example.apptaskwise

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskListActivity : AppCompatActivity() {

    private lateinit var nombreLista: String
    private val tareas = mutableListOf<String>() // Lista de tareas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        // Obtener el nombre de la lista de la actividad anterior
        nombreLista = intent.getStringExtra("nombreLista") ?: ""
        if (nombreLista.isEmpty()) {
            finish() // Finalizar actividad si no se proporciona el nombre de la lista
        }

        // Cargar las tareas guardadas al iniciar la actividad
        cargarTareas()

        val botonTarea = findViewById<TextView>(R.id.botonTarea)

        // Manejar clic en "Nueva Tarea"
        botonTarea.setOnClickListener {
            mostrarDialogoNuevaTarea()
        }

        // Actualizar la interfaz de usuario para mostrar las tareas
        mostrarTareas()
    }

    // Función para mostrar un cuadro de diálogo para ingresar el nombre de la nueva tarea
    private fun mostrarDialogoNuevaTarea() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nueva Tarea")

        // Configurar el cuadro de diálogo para ingresar el nombre de la tarea
        val input = EditText(this)
        input.hint = "Ingresa el nombre de la tarea"
        builder.setView(input)

        // Configurar los botones del cuadro de diálogo
        builder.setPositiveButton("Crear") { _, _ ->
            val nombreTarea = input.text.toString()
            if (nombreTarea.isNotEmpty()) {
                tareas.add(nombreTarea)

                // Log para verificar si la tarea se agregó correctamente
                Log.d("TaskListActivity", "Tarea agregada: $nombreTarea")

                // Guardar las tareas después de agregar una nueva tarea
                guardarTareas()

                // Actualizar la interfaz de usuario para mostrar las tareas
                mostrarTareas()
            }
        }

        builder.setNegativeButton("Cancelar") { _, _ ->
            // No es necesario hacer nada aquí, el cuadro de diálogo se cerrará automáticamente
        }

        // Mostrar el cuadro de diálogo
        builder.show()
    }

    // Función para actualizar la interfaz de usuario y mostrar las tareas
    private fun mostrarTareas() {
        // Limpiar cualquier contenido anterior
        val contenedorTareas = findViewById<LinearLayout>(R.id.contenedorTareas)
        contenedorTareas.removeAllViews()

        // Mostrar las tareas en el LinearLayout y el nombre de cada tarea
        for (i in tareas.indices) {
            val tarea = tareas[i]

            val tareaView = TextView(this)
            tareaView.text = " (${i + 1})  $tarea"
            tareaView.textSize = 18f
            tareaView.setTextColor(ContextCompat.getColor(this, R.color.black))
            tareaView.background = getDrawable(R.drawable.background_rounded_edittext)

            // Agregar un margen inferior a cada elemento de la tarea
            val marginParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            marginParams.setMargins(0, 0, 0, 16) // Ajusta el valor según sea necesario
            tareaView.layoutParams = marginParams

            // Agregar el nombre de la tarea al LinearLayout
            contenedorTareas.addView(tareaView)
        }
    }


    private fun eliminarTareasDeLista(lista: Lista) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Eliminar las tareas de SharedPreferences
        editor.remove("tareas_${lista.nombre}")
        editor.apply()
    }


    // Función para guardar las tareas en SharedPreferences
    private fun guardarTareas() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonTareas = gson.toJson(tareas)
        editor.putString("tareas_$nombreLista", jsonTareas)
        editor.apply()
    }

    // Función para cargar las tareas desde SharedPreferences
    private fun cargarTareas() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonTareas = sharedPreferences.getString("tareas_$nombreLista", null)
        val type = object : TypeToken<List<String>>() {}.type

        if (jsonTareas != null) {
            tareas.clear()
            tareas.addAll(gson.fromJson(jsonTareas, type))
        }
    }
}