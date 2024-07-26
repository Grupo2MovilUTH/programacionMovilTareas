package com.example.a11_kotlin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class TwoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)

        // Lugar donde se colocará el dato
        val verResultado: TextView = findViewById(R.id.lblResultado)

        // Formato
        val df = DecimalFormat("0.00")

        // Armar una etiqueta
        val mostrar = "El resultado de la operación es: ${df.format(Operaciones.resultado)}" // Redondear la respuesta

        // Cargar el mensaje
        verResultado.text = mostrar
    }
}
