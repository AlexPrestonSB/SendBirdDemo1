package com.sendbirdsampleapp.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

object FileUtil {

    val externalStorageDocument = "com.android.externalstorage.documents"
    val downloadDocument = "com.android.providers.downloads.documents"
    val mediaDocument = "com.android.providers.media.documents"
    val googlePhoto = "com.google.android.apps.photos.contentprovider"

    fun getFileInfo(context: Context, uri: Uri): Hashtable<String, Any>? {

        if (DocumentsContract.isDocumentUri(context, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            when {
                isExternalStorage(uri) -> {
                    val split = docId.split(":")
                    val type = docId[0]

                    if (type.equals("primary")) {
                        val result = Hashtable<String, Any>()
                        result["path"] = "%s,%s,%s".format(Environment.getExternalStorageDirectory(), "/", split[1])
                        result["size"] = File((result["path"] as String)).length().toInt()
                        result["mime"] = "application/octet-stream"

                        return result
                    }
                }
                isDownloadDocument(uri) -> {
                    //TODO FIX

                    val uriContent =
                        ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), docId.toLong())
                    return getDataColumn(context, uriContent, null, null)

                }
                isMediaDocument(uri) -> {

                }
            }
        } else if (uri.scheme.equals("content", true)) {
            if (isGooglePhoto(uri)) {
                val result = getDataColumn(context, uri, null, null)

                if (result?.contains("image/gif")!!) {
                    val input = context.contentResolver.openInputStream(uri)

                    val file = File.createTempFile("sendbird", ".gif")
                    val bytes = getBytes(input)

                    try {
                        val os = FileOutputStream(file)
                        os.write(bytes)
                        os.close()
                    } catch (e: Exception) {
                        Log.e("TAG", e.localizedMessage)
                    }



                    result.put("path", file.absolutePath)
                    result.put("size", file.length().toInt())

                } else {
                    val bitmap: Bitmap?
                    try {
                        val input = context.contentResolver.openInputStream(uri)
                        bitmap = BitmapFactory.decodeStream(input)
                        val file = File.createTempFile("sendbird", ".jpg")
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, BufferedOutputStream(FileOutputStream(file)))
                        result?.put("path", file.absolutePath)
                        result?.put("size", file.length().toInt())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                return result
            } else {
                return getDataColumn(context, uri, null, null)
            }

        } else if (uri.scheme.equals("file", true)) {
            val result = Hashtable<String, Any>()
            result["path"] = uri.path
            result["size"] = File(result["path"] as String).length().toInt()
            result["mime"] = "application/octet-stream"

            return result
        }
        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: ArrayList<String>?
    ): Hashtable<String, Any>? {

        var cursor: Cursor? = null
        val COLUMN_DATA = MediaStore.MediaColumns.DATA
        val COLUMN_SIZE = MediaStore.MediaColumns.SIZE
        var COLUMN_MIME = MediaStore.MediaColumns.MIME_TYPE

        val projection = mutableListOf(COLUMN_DATA, COLUMN_MIME, COLUMN_SIZE)
        try {
            try {
                cursor = context.contentResolver.query(
                    uri,
                    projection.toTypedArray(),
                    selection,
                    selectionArgs?.toTypedArray(),
                    null
                )
            } catch (e: IllegalArgumentException) {
                COLUMN_MIME = "mimetype"
                projection[1] = COLUMN_MIME
                cursor = context.contentResolver.query(
                    uri,
                    projection.toTypedArray(),
                    selection,
                    selectionArgs?.toTypedArray(),
                    null
                )
            }
            if (cursor != null && cursor.moveToFirst()) {
                var colIndex = cursor.getColumnIndexOrThrow(COLUMN_DATA)
                val path = cursor.getString(colIndex) ?: ""

                colIndex = cursor.getColumnIndexOrThrow(COLUMN_MIME)
                val mime = cursor.getString(colIndex) ?: "application/octet-stream"

                colIndex = cursor.getColumnIndexOrThrow(COLUMN_SIZE)
                val size = cursor.getInt(colIndex)

                val result = Hashtable<String, Any>()
                result.put("path", path)
                result.put("mime", mime)
                result.put("size", size)

                return result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return null
    }

    private fun getBytes(inputStream: InputStream) : ByteArray  {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        var len = -1
        while (inputStream.read(buffer).let { len = it; it != -1 }) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }


    private fun isExternalStorage(uri: Uri) = uri.authority == externalStorageDocument
    private fun isDownloadDocument(uri: Uri) = uri.authority == downloadDocument
    private fun isMediaDocument(uri: Uri) = uri.authority == mediaDocument
    private fun isGooglePhoto(uri: Uri) = uri.authority == googlePhoto

}