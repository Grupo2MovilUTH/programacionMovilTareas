package com.example.a23_kotlin

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.a23_kotlin.R
import com.example.a23_kotlin.transacciones.connection
import com.example.a23_kotlin.transacciones.transacciones_bd
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    /*Declaracion de las variables*/
    private lateinit var guardarFotos: Button
    private lateinit var verFotosGuardadas: Button
    private lateinit var tomarFoto: Button
    private lateinit var descripcion: TextView

    companion object {
        const val REQUEST_IMAGE = 101
        const val ACCESS_CAMERA = 201
    }

    private lateinit var currentPhotoPath: String
    private lateinit var imageView: ImageView
    private lateinit var conexion: connection

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*Casteando los valores y colocando los eventos para botones*/
        guardarFotos = findViewById(R.id.btnGuardar)
        tomarFoto = findViewById(R.id.btnTomarFoto)
        verFotosGuardadas = findViewById(R.id.btnVerFotos)
        descripcion = findViewById(R.id.txtDescripcion)
        imageView = findViewById(R.id.photo)

        tomarFoto.setOnClickListener {
            /*Metódo de permisos de la camara*/
            permisosCamara()
        }

        guardarFotos.setOnClickListener {
            if (validar()) {
                savePhotos()
            } else {
                mensajesVacios()
            }
        }

        verFotosGuardadas.setOnClickListener {
            val listaFotos = Intent(applicationContext, ListaFotos::class.java)
            startActivity(listaFotos)
        }
    }

    private fun permisosCamara() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request a la API del sistema operativo, esta es la peticion del permiso a la API del sistema operativo
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), ACCESS_CAMERA)
        } else {
            // si ya tenemos el permiso y esta otorgado, entonces vamos a tener la fotografia
            // TomarFoto()
            dispatchTakePictureIntent()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == ACCESS_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
                // TomarFoto()
            } else {
                Toast.makeText(applicationContext, "se necesita el permiso de la camara", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        // Environment es para obtener variables de entorno del sistema//con esto podemos acceder a los directorios del celular
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES) // obtener directorio de las imagenes
        return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            // Log.d("El path",currentPhotoPath);
            // currentPhotoPath permite obtener la url donde está ubicada nuestra imagen
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) // para tomar la foto

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile() // cargamos la imagen directamente de de la url
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                // Obtener URL de nuestra imagen
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.a23_kotlin.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE)
            }
        }
    }

    // Capturar lo que viene desde el ActivityForResult.
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Obtener toda la informacion de la data, pueden ser imagenes, texto, vide etc.
        // viene como respuesta al callback de la respuesta de la API del sistema operativo.
        if (requestCode == REQUEST_IMAGE) {
            try {
                val foto = File(currentPhotoPath) // trae toda la url
                // mandar el objeto
                imageView.setImageURI(Uri.fromFile(foto))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun validar(): Boolean {
        var retorna = true
        if (descripcion.text.toString().isEmpty()) {
            retorna = false
        }
        if (imageView.drawable == null) {
            retorna = false
        }
        // validar si la foto esta vacia
        return retorna
    }

    private fun savePhotos() {
        try {
            conexion = connection(this, transacciones_bd.nombre_bd, null, 1)
            val db = conexion.writableDatabase
            val photoData = obtenerFoto()

            val valores = ContentValues().apply {
                put(transacciones_bd.descripcion, descripcion.text.toString())
                put(transacciones_bd.imagen, photoData)
            }

            val result = db.insert(transacciones_bd.tabla, transacciones_bd.id, valores)
            message()
            db.close()
        } catch (exception: Exception) {
            Log.d("El error", "" + exception)
            error()
        }
    }

    private fun message() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Registro exitoso")
        builder.setMessage("La fotografía ha sido creada correctamente")

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun error() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Error al registrar")
        builder.setMessage("La fotografía no se ha podido guardar.")

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun obtenerFoto(): ByteArray? {
        return try {
            val photoFile = File(currentPhotoPath)
            val file = FileInputStream(photoFile)
            val photoData = ByteArray(photoFile.length().toInt())
            file.read(photoData)
            file.close()
            photoData
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun mensajesVacios() {
        val builder = AlertDialog.Builder(this)
        if (descripcion.text.toString().isEmpty()) {
            builder.setTitle("Advertencia")
            builder.setMessage("Escriba su descripción por favor")
        }
        if (imageView.drawable == null) {
            builder.setTitle("Advertencia")
            builder.setMessage("Ingrese su imagen por favor")
        }
        builder.setPositiveButton("Close") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
