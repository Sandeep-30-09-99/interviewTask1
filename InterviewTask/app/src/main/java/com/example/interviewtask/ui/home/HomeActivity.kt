package com.example.interviewtask.ui.home


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
import com.example.interviewtask.ui.ListAdapter
import com.example.interviewtask.databinding.ActivityMainBinding
import com.example.interviewtask.model.Data
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID === id) {
                Toast.makeText(this@HomeActivity, "Downloaded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@HomeActivity, "File not fount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val viewModel: HomeVM by viewModels()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initRV()
        registerReceiver(
            onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        requestPermission()
        setObserver()
        getApiData()
        val v = 2 / 0
        print("" + v)
    }


    override fun onDestroy() {
        unregisterReceiver(onDownloadComplete)
        super.onDestroy()
    }

    private var adapter: ListAdapter? = null
    private fun initRV() {
        adapter = ListAdapter(this, object : AdapterCallback {
            override fun onViewClick(v: View, bean: Data) {
                Log.i("abc", bean.path)
                when (v.id) {
                    R.id.ivDownload -> {
                        downloadFile(bean.name, "File", bean.path)
                    }
                }
            }
        })
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL
            )
        )
        binding.rv.adapter = adapter
    }

    private fun setObserver() {
        viewModel.apiData.observe(this) {
            adapter?.setList(it)

        }
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
            this@HomeActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 12
        )
    }

    private fun getApiData() {
        viewModel.getApiData()
    }


}