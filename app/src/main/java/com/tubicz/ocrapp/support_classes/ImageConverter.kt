package com.tubicz.ocrapp.support_classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.IOException
import java.io.InputStream

class ImageConverter {
    fun uriToBitmap(uri: Uri?, context: Context): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun fileToBitmap(filename: String, context: Context): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val fileInputStream: InputStream = context.openFileInput(filename)
            bitmap = BitmapFactory.decodeStream(fileInputStream)
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }
}