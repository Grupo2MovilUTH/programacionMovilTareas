package com.example.a24_kotlin

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.a24_kotlin.transacciones.connection
import com.example.a24_kotlin.transacciones.transacciones_bd
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var signaturePad: Canva_FirmaDigital
    private lateinit var guardarFirmas: Button
    private lateinit var verFirmas: Button
    private lateinit var descripcion: TextView

    private var conexion: connection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Casteando los valores y colocando los eventos para botones
        guardarFirmas = findViewById(R.id.btnGuardar)
        verFirmas = findViewById(R.id.btnVerFirmas)
        descripcion = findViewById(R.id.txtDescripcionFirma)
        signaturePad = findViewById(R.id.firma_digital)

        guardarFirmas.setOnClickListener {
            if (validar()) {
                saveFirmas()
            } else {
                mensajesVacios()
            }
        }

        verFirmas.setOnClickListener {
            val listaFirmasIntent = Intent(applicationContext, ListaFirmas::class.java)
            startActivity(listaFirmasIntent)
        }
    }

    private fun validar(): Boolean {
        var retorna = true

        if (descripcion.text.toString().isEmpty()) {
            retorna = false
        }
        // Validar si la firma está vacía
        if (!Canva_FirmaDigital.isTouched) {
            retorna = false
        }
        return retorna
    }

    private fun mensajesVacios() {
        val builder = AlertDialog.Builder(this)
        if (descripcion.text.toString().isEmpty()) {
            builder.setTitle("Advertencia")
            builder.setMessage("Escriba su descripción por favor")
        }
        if (!Canva_FirmaDigital.isTouched) {
            builder.setTitle("Advertencia")
            builder.setMessage("Dibuje su firma digital")
        }
        builder.setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun saveFirmas() {
        try {
            conexion = connection(this, transacciones_bd.nombre_bd, null, 1)
            val db = conexion!!.writableDatabase
            val firmaData = obtenerFirma()

            val valores = ContentValues().apply {
                put(transacciones_bd.descripcion, descripcion.text.toString())
                put(transacciones_bd.firma, firmaData)
            }

            val result = db.insert(transacciones_bd.tabla, transacciones_bd.id, valores)
            message()
            db.close()
        } catch (exception: Exception) {
            Log.d("El error", exception.toString())
            error()
        }
    }

    private fun message() {
        Canva_FirmaDigital.isTouched = false
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro exitoso")
        builder.setMessage("La firma ha sido creada correctamente")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
            signaturePad.clearCanvas()
            descripcion.text = ""
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun error() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error al registrar")
        builder.setMessage("La firma no se ha podido guardar.")
        builder.setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun obtenerFirma(): ByteArray {
        val signatureBitmap = signaturePad.getSignatureBitmap()
        val byteArrayOutputStream = ByteArrayOutputStream()
        if (signatureBitmap != null) {
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        }
        return byteArrayOutputStream.toByteArray()
    }
}
