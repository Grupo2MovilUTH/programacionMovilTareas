package com.example.a12_kotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList

class Transacciones(context: Context) {

    private val conexion: SQLiteDatabase
    private val lista: ArrayList<Personas> = ArrayList()
    private val DBname = "BDPersonas"
    private val tabla = """CREATE TABLE IF NOT EXISTS personas (
        id INTEGER PRIMARY KEY AUTOINCREMENT, 
        nombre TEXT, 
        apellido TEXT, 
        edad INTEGER, 
        telefono INTEGER, 
        correo TEXT, 
        direccion TEXT)"""

    init {
        conexion = context.openOrCreateDatabase(DBname, Context.MODE_PRIVATE, null)
        conexion.execSQL(tabla)
    }

    fun insertar(personas: Personas): Boolean {
        val contenedor = ContentValues().apply {
            put("nombre", personas.nombre)
            put("apellido", personas.apellido)
            put("edad", personas.edad)
            put("telefono", personas.telefono)
            put("correo", personas.correo)
            put("direccion", personas.direccion)
        }
        return conexion.insert("personas", null, contenedor) > 0
    }

    fun eliminar(id: Int): Boolean {
        return conexion.delete("personas", "id=$id", null) > 0
    }

    fun editar(personas: Personas): Boolean {
        val contenedor = ContentValues().apply {
            put("nombre", personas.nombre)
            put("apellido", personas.apellido)
            put("edad", personas.edad)
            put("telefono", personas.telefono)
            put("correo", personas.correo)
            put("direccion", personas.direccion)
        }
        return conexion.update("personas", contenedor, "id=${personas.id}", null) > 0
    }

    fun verTodos(): ArrayList<Personas> {
        lista.clear()
        val cursor: Cursor = conexion.rawQuery("SELECT * FROM personas", null)
        cursor.use {
            if (it != null && it.count > 0) {
                it.moveToFirst()
                do {
                    lista.add(Personas(
                        it.getInt(0),
                        it.getString(1),
                        it.getString(2),
                        it.getInt(3),
                        it.getInt(4),
                        it.getString(5),
                        it.getString(6)
                    ))
                } while (it.moveToNext())
            }
        }
        return lista
    }

    fun verUno(posicion: Int): Personas {
        val cursor: Cursor = conexion.rawQuery("SELECT * FROM personas", null)
        cursor.use {
            it.moveToPosition(posicion)
            return Personas(
                it.getInt(0),
                it.getString(1),
                it.getString(2),
                it.getInt(3),
                it.getInt(4),
                it.getString(5),
                it.getString(6)
            )
        }
    }
}
