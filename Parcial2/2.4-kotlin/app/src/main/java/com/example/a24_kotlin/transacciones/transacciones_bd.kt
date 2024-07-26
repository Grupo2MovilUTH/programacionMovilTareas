package com.example.a24_kotlin.transacciones

object transacciones_bd {

    // Nombre de la BD
    const val nombre_bd = "bd_firmas"

    // Tablas de la BD
    const val tabla = "firmas"

    // Campos de la tabla
    const val id = "id"
    const val firma = "firma"
    const val descripcion = "descripcion"

    // Consultas de base de datos DDL
    // Creando la tabla con sus campos
    const val CreateTableFirmas = """
        CREATE TABLE firmas (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            firma BLOB,
            descripcion TEXT
        )
    """

    // Consultas de base de datos DML
    const val SelectTableFirmas = "SELECT firma, descripcion FROM $tabla"
}
