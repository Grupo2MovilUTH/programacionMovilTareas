package com.example.a23_kotlin

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.a23_kotlin.Modelo.Photograph
import com.example.a23_kotlin.transacciones.transacciones_bd
import com.example.a23_kotlin.transacciones.connection
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class LisAdapter(
    @NonNull context: Context,
    private val resource: Int,
    private val objects: List<Photograph>
) : ArrayAdapter<Photograph>(context, resource, objects) {

    private val mContext: Context = context
    private val mList: List<Photograph> = objects
    private var connection: connection? = null

    /*Vista inflada de cada elemento que viene en el listview*/
    @NonNull
    override fun getView(position: Int, @Nullable convertView: View?, @NonNull parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            /*Que cree el row con el layout*/
            view = LayoutInflater.from(mContext).inflate(resource, null)

            val modelo = mList[position]
            val imagen = view.findViewById<ImageView>(R.id.imagen)
            imagen.setImageBitmap(obtenerImagen("1"))
            // imagen.setImageResource(modelo.imagen)

            val descripcion = view.findViewById<TextView>(R.id.textView)
            descripcion.text = modelo.descripcion
        }
        return view!!
    }

    fun obtenerImagen(id: String): Bitmap? {
        connection = connection(mContext, transacciones_bd.nombre_bd, null, 1)
        val db: SQLiteDatabase = connection!!.readableDatabase
        var bitmap: Bitmap? = null
        var imagen: ByteArray? = null
        val selectQuery = "SELECT ${transacciones_bd.imagen} FROM ${transacciones_bd.tabla} WHERE id = ?"

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, arrayOf(id))
            if (cursor.moveToNext()) {
                imagen = cursor.getBlob(0)
                val size = imagen.size
                Log.e("tamaño", "$size")
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        if (imagen != null) {
            try {
                val metodoCovertir = VerImage()
                val imageFile = metodoCovertir.convertByteArrToFile(mContext, imagen)
                bitmap = reducirCalidadImagen(imageFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

    private fun reducirCalidadImagen(imageFile: File): Bitmap {
        val quality = 90
        val bitmap: Bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            out.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            out?.close()
        }
        return bitmap
    }
}
