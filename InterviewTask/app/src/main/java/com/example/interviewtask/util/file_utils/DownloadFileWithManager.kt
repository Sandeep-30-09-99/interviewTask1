package com.rk.obedient_structure.utils.file_utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

object DownloadFileWithManager {

    private var destinationFile: File? = null
    private var downloadId: Long = -1

     fun downloadMedia(baseContext: Context, mediaUrl: String, fileType: String): Long {
        val mdDownloadManager =
            baseContext.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(mediaUrl))
        File(baseContext.getExternalFilesDir(null), "Structure Media")
        val directory = File(baseContext.getExternalFilesDir(null), "Structure Media")
        destinationFile = if (directory.isDirectory) {
            File(directory.path, getFileName(fileType))
        } else {
            if (directory.mkdir()) {
                File(directory.path, getFileName(fileType))
            } else {
                Log.i("DownLoadFile Error", ":::::File Could not be created")
                Toast.makeText(
                    baseContext,
                    "File Could not be created",
                    Toast.LENGTH_SHORT
                ).show()
                return downloadId
            }
        }
        request.setDescription("Downloading ...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(Uri.fromFile(destinationFile))
        request.setVisibleInDownloadsUi(true)
        downloadId = mdDownloadManager.enqueue(request)
        return downloadId
    }

    private fun getFileName(type: String): String {
        var fName = "StructureMedia_${System.currentTimeMillis()}"
       /* when (type) {
            SendBirdFileMessageType.image.name -> {
                fName += ".jpg"
            }
            SendBirdFileMessageType.video.name -> {
                fName += ".mp4"
            }
            SendBirdFileMessageType.audio.name -> {
                fName += ".m4a"
            }
        }*/
        return fName
    }
}