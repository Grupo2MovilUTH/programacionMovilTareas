package com.example.a12_kotlin

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Adaptador(
    private val activity: Activity,
    private var lista: ArrayList<Personas>,
    private val transacciones: Transacciones
) : BaseAdapter() {

    private var id: Int = 0

    override fun getCount(): Int = lista.size

    override fun getItem(position: Int): Personas = lista[position]

    override fun getItemId(position: Int): Long = lista[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(activity).inflate(R.layout.items, parent, false)

        val personas = lista[position]

        val nombre: TextView = view.findViewById(R.id.t_nombre)
        val apellido: TextView = view.findViewById(R.id.t_apellido)
        val edad: TextView = view.findViewById(R.id.t_edad)
        val telefono: TextView = view.findViewById(R.id.t_telefono)
        val correo: TextView = view.findViewById(R.id.t_correo)
        val direccion: TextView = view.findViewById(R.id.t_direccion)

        val editar: FloatingActionButton = view.findViewById(R.id.btneditar)
        val eliminar: FloatingActionButton = view.findViewById(R.id.btneliminar)

        nombre.text = personas.nombre
        apellido.text = personas.apellido
        edad.text = personas.edad.toString()
        telefono.text = personas.telefono.toString()
        correo.text = personas.correo
        direccion.text = personas.direccion

        editar.tag = position
        eliminar.tag = position

        editar.setOnClickListener {
            val pos = editar.tag.toString().toInt()

            val dialogo = Dialog(activity).apply {
                setTitle("Editar registro")
                setCancelable(true)
                setContentView(R.layout.agregar)
                show()
            }

            val nombreField: EditText = dialogo.findViewById(R.id.nombre)
            val apellidoField: EditText = dialogo.findViewById(R.id.apellido)
            val edadField: EditText = dialogo.findViewById(R.id.edad)
            val telefonoField: EditText = dialogo.findViewById(R.id.telefono)
            val correoField: EditText = dialogo.findViewById(R.id.correo)
            val direccionField: EditText = dialogo.findViewById(R.id.direccion)

            val guardar: FloatingActionButton = dialogo.findViewById(R.id.btnguardar)
            val cancelar: FloatingActionButton = dialogo.findViewById(R.id.btncancelar)

            val persona = lista[pos]
            id = persona.id

            nombreField.setText(persona.nombre)
            apellidoField.setText(persona.apellido)
            edadField.setText(persona.edad.toString())
            telefonoField.setText(persona.telefono.toString())
            correoField.setText(persona.correo)
            direccionField.setText(persona.direccion)

            guardar.setOnClickListener {
                try {
                    val updatedPerson = Personas(
                        id,
                        nombreField.text.toString(),
                        apellidoField.text.toString(),
                        edadField.text.toString().toInt(),
                        telefonoField.text.toString().toInt(),
                        correoField.text.toString(),
                        direccionField.text.toString()
                    )
                    transacciones.editar(updatedPerson)
                    lista = transacciones.verTodos()
                    notifyDataSetChanged()
                    dialogo.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }

            cancelar.setOnClickListener {
                dialogo.dismiss()
            }
        }

        eliminar.setOnClickListener {
            val pos = eliminar.tag.toString().toInt()
            val persona = lista[pos]
            id = persona.id

            AlertDialog.Builder(activity).apply {
                setMessage("¿Estas seguro de eliminar el contacto?")
                setCancelable(false)
                setPositiveButton("Sí") { _, _ ->
                    transacciones.eliminar(id)
                    lista = transacciones.verTodos()
                    notifyDataSetChanged()
                }
                setNegativeButton("No", null)
                show()
            }
        }

        return view
    }
}
