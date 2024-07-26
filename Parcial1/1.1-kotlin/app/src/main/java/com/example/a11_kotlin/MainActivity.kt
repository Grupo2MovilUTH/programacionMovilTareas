package com.example.a11_kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var numberOne: EditText
    private lateinit var numberTwo: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnSubtract: Button
    private lateinit var btnMultiply: Button
    private lateinit var btnSplit: Button

    private var typeOperation: Int = 0
    private var number1: Double = 0.0
    private var number2: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberOne = findViewById(R.id.txtNumero1)
        numberTwo = findViewById(R.id.txtNumero2)
        btnAdd = findViewById(R.id.btnSum)
        btnSubtract = findViewById(R.id.btnRes)
        btnMultiply = findViewById(R.id.btnMulti)
        btnSplit = findViewById(R.id.btnDiv)

        btnAdd.setOnClickListener {
            onOperationClick(1)
        }

        btnSubtract.setOnClickListener {
            onOperationClick(2)
        }

        btnMultiply.setOnClickListener {
            onOperationClick(3)
        }

        btnSplit.setOnClickListener {
            if (validarDivisionInvalida() && validarPuntos() && validar()) {
                typeOperation = 4
                performOperation()
            }
        }
    }

    private fun onOperationClick(operationType: Int) {
        if (validar() && validarPuntos()) {
            typeOperation = operationType
            performOperation()
        }
    }

    private fun performOperation() {
        llamarDeclaracion()
        Operaciones(number1, number2, typeOperation)
        val intent = Intent(getApplicationContext(), TwoActivity::class.java)
        startActivity(intent)
    }

    private fun validar(): Boolean {
        var isValid = true

        if (numberOne.text.toString().isEmpty()) {
            numberOne.error = "No se permiten campos vacíos"
            isValid = false
        }
        if (numberTwo.text.toString().isEmpty()) {
            numberTwo.error = "No se permiten campos vacíos"
            isValid = false
        }
        return isValid
    }

    private fun validarDivisionInvalida(): Boolean {
        var isValid = true

        if (numberTwo.text.toString() == "0" || number2 == 0.0) {
            Log.d("Mostrar", "$number2")
            numberTwo.error = "No se permite la división entre 0"
            isValid = false
        }
        Log.d("Mensaje", isValid.toString())
        return isValid
    }

    private fun validarPuntos(): Boolean {
        var isValid = true
        val numberOneText = numberOne.text.toString()
        val numberTwoText = numberTwo.text.toString()

        if (numberOneText.startsWith(".") || numberOneText.endsWith(".")) {
            numberOne.error = "Formato Incorrecto y/o Incompleto"
            isValid = false
        }
        if (numberTwoText.startsWith(".") || numberTwoText.endsWith(".")) {
            numberTwo.error = "Formato Incorrecto y/o Incompleto"
            isValid = false
        }
        return isValid
    }

    private fun llamarDeclaracion() {
        number1 = numberOne.text.toString().toDouble()
        number2 = numberTwo.text.toString().toDouble()
    }
}
