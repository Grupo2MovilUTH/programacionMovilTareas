package com.example.a24_kotlin

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a24_kotlin.Modelo.signaturess
import com.example.a24_kotlin.transacciones.connection
import com.example.a24_kotlin.transacciones.transacciones_bd

class ListaFirmas : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: CardView_CustomAdapter
    private val lista: MutableList<signaturess> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_firmas)

        // Casteando el RecyclerView
        recyclerView = findViewById(R.id.listaFirmas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        getDatos()
        // Obtén tus datos como lo hacías antes
        val dataList = getAllFirmas()

        // Configura el adaptador
        myAdapter = CardView_CustomAdapter(applicationContext, dataList)
        recyclerView.adapter = myAdapter
    }

    private fun getAllFirmas(): List<signaturess> {
        val firmas = mutableListOf<signaturess>()
        val con = connection(this, transacciones_bd.nombre_bd, null, 1)
        val db: SQLiteDatabase = con.readableDatabase
        val cursor: Cursor = db.rawQuery(transacciones_bd.SelectTableFirmas, null)

        if (cursor.moveToFirst()) {
            do {
                val columnIndexFirma = cursor.getColumnIndex(transacciones_bd.firma)
                val columnIndexDescripcion = cursor.getColumnIndex(transacciones_bd.descripcion)

                val modelo = signaturess().apply {
                    firma = cursor.getBlob(columnIndexFirma)
                    descripcion = cursor.getString(columnIndexDescripcion)
                }
                // Agrega el modelo a la lista
                firmas.add(modelo)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return firmas
    }

    private fun getDatos() {
        val con = connection(this, transacciones_bd.nombre_bd, null, 1)
        val db: SQLiteDatabase = con.readableDatabase
        val cursor: Cursor = db.rawQuery(transacciones_bd.SelectTableFirmas, null)

        while (cursor.moveToNext()) {
            val firmas = signaturess().apply {
                descripcion = cursor.getString(1)
            }
            lista.add(firmas)
        }
        cursor.close()
    }
}
