package com.example.a23_kotlin

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class VerImage {
    @Throws(IOException::class)
    fun convertByteArrToFile(context: Context, fileBytes: ByteArray): File {
        val tempFile = File.createTempFile("tempImage", null, context.cacheDir)
        tempFile.deleteOnExit()

        FileOutputStream(tempFile).use { fos ->
            fos.write(fileBytes)
        }

        return tempFile
    }
}
