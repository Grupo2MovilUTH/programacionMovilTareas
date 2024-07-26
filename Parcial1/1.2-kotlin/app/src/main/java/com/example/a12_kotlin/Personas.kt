package com.example.a12_kotlin

data class Personas(
    var id: Int = 0,
    var nombre: String = "",
    var apellido: String = "",
    var edad: Int = 0,
    var telefono: Int = 0,
    var correo: String = "",
    var direccion: String = ""
) {
    constructor(nombre: String, apellido: String, edad: Int, telefono: Int, correo: String, direccion: String) : this(
        id = 0,
        nombre = nombre,
        apellido = apellido,
        edad = edad,
        telefono = telefono,
        correo = correo,
        direccion = direccion
    )
}
