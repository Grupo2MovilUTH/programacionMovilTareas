package com.example.a11_kotlin

class Operaciones(private val numero1: Double, private val numero2: Double, oper: Int) {

    companion object {
        var resultado: Double = 0.0
    }

    init {
        resultado = when (oper) {
            1 -> Suma()
            2 -> Resta()
            3 -> Multiplicacion()
            4 -> Division()
            else -> 0.0
        }
    }

    private fun Suma(): Double {
        return numero1 + numero2
    }

    private fun Resta(): Double {
        return numero1 - numero2
    }

    private fun Multiplicacion(): Double {
        return numero1 * numero2
    }

    private fun Division(): Double {
        return numero1 / numero2
    }
}
