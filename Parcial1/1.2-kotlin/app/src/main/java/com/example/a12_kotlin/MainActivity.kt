package com.example.a12_kotlin

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var transacciones: Transacciones
    private lateinit var adaptador: Adaptador
    private lateinit var lista: ArrayList<Personas>
    private lateinit var personas: Personas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transacciones = Transacciones(this)
        lista = transacciones.verTodos()
        adaptador = Adaptador(this, lista, transacciones)

        val list: ListView = findViewById(R.id.lista)
        val nuevo: FloatingActionButton = findViewById(R.id.btnnuevo)
        list.adapter = adaptador

        list.setOnItemClickListener { _, _, i, _ ->
            // LLAMA VISTA.XML
        }

        nuevo.setOnClickListener {
            // LLAMA AGREGAR.XML
            val dialogo = Dialog(this)
            dialogo.setTitle("Nuevo registro")
            dialogo.setCancelable(true)
            dialogo.setContentView(R.layout.agregar)
            dialogo.show()

            val nombre: EditText = dialogo.findViewById(R.id.nombre)
            val apellido: EditText = dialogo.findViewById(R.id.apellido)
            val edad: EditText = dialogo.findViewById(R.id.edad)
            val telefono: EditText = dialogo.findViewById(R.id.telefono)
            val correo: EditText = dialogo.findViewById(R.id.correo)
            val direccion: EditText = dialogo.findViewById(R.id.direccion)

            val guardar: FloatingActionButton = dialogo.findViewById(R.id.btnguardar)
            val cancelar: FloatingActionButton = dialogo.findViewById(R.id.btncancelar)

            guardar.setOnClickListener {
                try {
                    personas = Personas(
                        nombre.text.toString(),
                        apellido.text.toString(),
                        edad.text.toString().toInt(),
                        telefono.text.toString().toInt(),
                        correo.text.toString(),
                        direccion.text.toString()
                    )
                    transacciones.insertar(personas)
                    lista = transacciones.verTodos()
                    adaptador.notifyDataSetChanged()
                    dialogo.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }

            cancelar.setOnClickListener {
                dialogo.dismiss()
            }
        }
    }
}
