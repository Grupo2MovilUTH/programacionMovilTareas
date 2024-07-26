package com.example.a23_kotlin.transacciones

object transacciones_bd {

    // Nombre de la BD
    const val nombre_bd = "bd_fotos"

    // Tablas de la BD
    const val tabla = "fotos"

    // Campos de la tabla
    const val id = "id"
    const val imagen = "imagen"
    const val descripcion = "descripcion"

    // Consultas de base de datos DDL
    // Creando la tabla con sus campos
    const val CreateTablePhotos = """
        CREATE TABLE $tabla (
            $id INTEGER PRIMARY KEY AUTOINCREMENT,
            $imagen BLOB,
            $descripcion TEXT
        )
    """

    // Consultas de base de datos DML
    const val SelectTableFotos = "SELECT $imagen, $descripcion FROM $tabla"
}
