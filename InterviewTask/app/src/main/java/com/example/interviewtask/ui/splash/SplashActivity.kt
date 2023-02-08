package com.example.interviewtask.ui.splash


import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.interviewtask.R
import com.example.interviewtask.ui.AdapterCallback
import com.example.interviewtask.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID === id) {
                Toast.makeText(this@SplashActivity, "Downloaded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@SplashActivity, "File not fount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val viewModel: SplashVM by viewModels()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val v = 2 / 0
        print("" + v)
    }


    override fun onDestroy() {
        unregisterReceiver(onDownloadComplete)
        super.onDestroy()
    }



    private var downloadID: Long? = null
    private fun downloadFile(fileName: String, desc: String, url: String) {
        // fileName -> fileName with extension
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName).setDescription(desc)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true).setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@SplashActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 12
        )
    }

    private fun getApiData() {
        viewModel.getApiData()
    }


}