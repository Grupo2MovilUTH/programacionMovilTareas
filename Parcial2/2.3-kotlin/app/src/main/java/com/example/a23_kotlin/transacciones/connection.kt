package com.example.a23_kotlin.transacciones

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/*Esta clase es para la creación y la actualización de la base de datos.*/
class connection(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    // Este es para crear los objetos de base de datos
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) { // se llama cuando la BD se crea por primera vez
        sqLiteDatabase.execSQL(transacciones_bd.CreateTablePhotos) // Creando la tabla como tal.
    }

    // Este es para actualizarlo o destruir los objetos
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Implementar lógica de actualización si es necesario
    }
}
