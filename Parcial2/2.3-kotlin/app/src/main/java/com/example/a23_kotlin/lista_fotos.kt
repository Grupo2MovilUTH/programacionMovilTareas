package com.example.a23_kotlin

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ListAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.a23_kotlin.Modelo.Photograph
import com.example.a23_kotlin.transacciones.connection
import com.example.a23_kotlin.transacciones.transacciones_bd

class ListaFotos : AppCompatActivity() {
    private lateinit var miListView: ListView
    private var miList: MutableList<Photograph> = mutableListOf()
    private lateinit var myAdapter: ListAdapter
    private lateinit var connection: connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_fotos)
        miListView = findViewById(R.id.listViewFotos)

        connection = connection(this, transacciones_bd.nombre_bd, null, 1)
        getDatos()
        // Obtén todos los modelos de la base de datos
        val modelos = getAllPhotos()

        // Usa un adaptador para mostrar los modelos en el ListView
        myAdapter = LisAdapter(this, R.layout.item_row, modelos)
        miListView.adapter = myAdapter
    }

    fun getAllPhotos(): List<Photograph> {
        val modeloList: MutableList<Photograph> = mutableListOf()

        // Selecciona todos los query
        val db = connection.readableDatabase
        val cursor: Cursor = db.rawQuery(transacciones_bd.SelectTableFotos, null)

        // Recorre todas las filas y añade a la lista
        if (cursor.moveToFirst()) {
            do {
                val columnIndexImagen = cursor.getColumnIndexOrThrow(transacciones_bd.imagen)
                val columnIndexDescripcion = cursor.getColumnIndexOrThrow(transacciones_bd.descripcion)

                val modelo = Photograph()
                modelo.imagen = cursor.getBlob(columnIndexImagen)
                modelo.descripcion = cursor.getString(columnIndexDescripcion)
                // Agrega el modelo a la lista
                modeloList.add(modelo)
            } while (cursor.moveToNext())
        }

        // Cierra el cursor y la base de datos
        cursor.close()
        db.close()

        // Retorna la lista de modelos
        return modeloList
    }

    private fun getDatos() {
        val db = connection.readableDatabase
        val cursor: Cursor = db.rawQuery(transacciones_bd.SelectTableFotos, null)
        while (cursor.moveToNext()) {
            val photograh = Photograph()
            photograh.descripcion = cursor.getString(1)
            miList.add(photograh)
        }
        cursor.close()
    }
}
