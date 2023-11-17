package com.example.apptaskwise

import android.content.Context
import android.content.Intent
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

data class Lista(var nombre: String, val tareas: MutableList<String> = mutableListOf()) // Clase Lista

class Home : AppCompatActivity() {

    private val listas = mutableMapOf<String, Lista>() // Mapa de listas por nombre
    private lateinit var contenedorListas: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        contenedorListas = findViewById(R.id.contenedorListas)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val botonLista = findViewById<TextView>(R.id.botonlista)

        // Cargar las listas guardadas al iniciar la aplicación
        cargarListas()

        // Manejar clic en "Nueva Lista"
        botonLista.setOnClickListener {
            mostrarDialogoNuevaLista()
        }

        // Actualizar la interfaz de usuario para mostrar las listas
        mostrarListas()
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
                // Verificar si ya existe una lista con ese nombre
                if (!listas.containsKey(nombreLista)) {
                    // Si no existe, crea una nueva lista
                    val nuevaLista = Lista(nombreLista)
                    listas[nuevaLista.nombre] = nuevaLista

                    // Log para verificar si la lista se agregó correctamente
                    Log.d("Home", "Lista agregada: ${nuevaLista.nombre}")

                    // Guardar las listas después de agregar una nueva lista
                    guardarListas()

                    // Actualizar la interfaz de usuario para mostrar las listas
                    mostrarListas()
                } else {
                    // Si ya existe, puedes mostrar un mensaje o simplemente no hacer nada
                    Log.d("Home", "Ya existe una lista con el nombre: $nombreLista")
                    // Puedes mostrar un mensaje indicando que la lista ya existe si lo deseas
                }
            }
        }

        builder.setNegativeButton("Cancelar") { _, _ ->
            // No es necesario hacer nada aquí, el cuadro de diálogo se cerrará automáticamente
        }

        // Mostrar el cuadro de diálogo
        builder.show()
    }

    // Función para mostrar un cuadro de diálogo para editar el nombre de la lista
    // Función para mostrar un cuadro de diálogo para editar el nombre de la lista
    private fun mostrarDialogoEditarLista(nombreLista: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Lista")

        val input = EditText(this)
        input.hint = "Ingresa el nuevo nombre de la lista"
        builder.setView(input)

        builder.setPositiveButton("Guardar") { _, _ ->
            val nuevoNombre = input.text.toString()
            if (nuevoNombre.isNotEmpty() && listas.containsKey(nombreLista)) {
                // Guardar la lista antes de editarla
                val lista = listas[nombreLista]!!

                // Crear una nueva lista con el nuevo nombre y las tareas existentes
                val nuevaLista = Lista(nuevoNombre, lista.tareas)
                listas.remove(nombreLista)
                listas[nuevoNombre] = nuevaLista

                // Actualizar las tareas en SharedPreferences
                guardarTareasParaLista(nuevaLista)

                // Guardar las listas después de editar una lista
                guardarListas()

                // Actualizar la interfaz de usuario para mostrar las listas
                mostrarListas()
            }
        }

        builder.setNegativeButton("Cancelar") { _, _ -> }

        builder.show()
    }


    // Función para guardar las tareas en SharedPreferences para una lista dada
    private fun guardarTareasParaLista(lista: Lista) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonTareas = gson.toJson(lista.tareas)
        editor.putString("tareas_${lista.nombre}", jsonTareas)
        editor.apply()
    }

    // Función para mostrar un cuadro de diálogo para eliminar la lista
    private fun mostrarDialogoEliminarLista(nombreLista: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Lista")

        builder.setMessage("¿Estás seguro de que quieres eliminar la lista \"$nombreLista\"?")

        builder.setPositiveButton("Eliminar") { _, _ ->
            // Obtener la lista antes de eliminarla
            val lista = listas[nombreLista]

            listas.remove(nombreLista)

            // Eliminar las tareas asociadas a la lista
            if (lista != null) {
                eliminarTareasDeLista(lista)
            }

            // Guardar las listas después de eliminar una lista
            guardarListas()

            // Actualizar la interfaz de usuario para mostrar las listas
            mostrarListas()
        }

        builder.setNegativeButton("Cancelar") { _, _ -> }

        builder.show()
    }

    // Función para eliminar las tareas asociadas a una lista
    private fun eliminarTareasDeLista(lista: Lista) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Eliminar las tareas de SharedPreferences
        editor.remove("tareas_${lista.nombre}")
        editor.apply()
    }


    // Función para mostrar las opciones de editar y eliminar al mantener presionado un elemento de la lista
    private fun mostrarOpcionesEditarEliminar(nombreLista: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Opciones")

        val opciones = arrayOf("Editar", "Eliminar")

        builder.setItems(opciones) { _, which ->
            when (which) {
                0 -> mostrarDialogoEditarLista(nombreLista)
                1 -> mostrarDialogoEliminarLista(nombreLista)
            }
        }

        builder.setNegativeButton("Cancelar") { _, _ -> }

        builder.show()
    }

    // Función para actualizar la interfaz de usuario y mostrar las listas
    private fun mostrarListas() {
        // Limpiar cualquier contenido anterior
        contenedorListas.removeAllViews()

        // Mostrar las listas en el LinearLayout y el nombre de cada lista
        for ((nombre, lista) in listas) {
            val listaView = TextView(this)
            listaView.text = "         $nombre  "
            listaView.textSize = 18f
            listaView.setTextColor(ContextCompat.getColor(this, R.color.black))
            listaView.background = getDrawable(R.drawable.background_rounded_edittext)

            // Agregar un margen inferior a cada elemento de la lista
            val marginParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            marginParams.setMargins(0, 0, 0, 16) // Ajusta el valor según sea necesario
            listaView.layoutParams = marginParams

            // Agregar el nombre de la lista al LinearLayout
            contenedorListas.addView(listaView)

            // Agregar un clic listener a cada elemento de la lista
            listaView.setOnClickListener {
                // Manejar clic en la lista
                abrirTaskListActivity(lista)
            }

            // Agregar un clic largo para editar y eliminar la lista
            listaView.setOnLongClickListener {
                mostrarOpcionesEditarEliminar(nombre)
                true
            }

            // Agregar un menú contextual para eliminar la lista
            registerForContextMenu(listaView)
        }
    }

    // Función para abrir la actividad de lista de tareas
    private fun abrirTaskListActivity(lista: Lista) {
        val intent = Intent(this, TaskListActivity::class.java)
        intent.putExtra("nombreLista", lista.nombre)
        startActivity(intent)
    }

    // Función para guardar las listas en SharedPreferences
    private fun guardarListas() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonListas = gson.toJson(listas.values.toList()) // Convertir solo las listas (sin claves) a lista
        editor.putString("listas", jsonListas)
        editor.apply()
    }

    // Función para cargar las listas desde SharedPreferences
    private fun cargarListas() {
        val gson = Gson()
        val jsonListas = sharedPreferences.getString("listas", null)
        val type = object : TypeToken<List<Lista>>() {}.type

        if (jsonListas != null) {
            val loadedListas = gson.fromJson<List<Lista>>(jsonListas, type)
            // Convertir la lista cargada a un mapa con nombres como claves
            listas.clear()
            for (loadedLista in loadedListas) {
                listas[loadedLista.nombre] = loadedLista
            }
        }
    }
}
