package com.example.interviewtask.ui.template


import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.app.ProgressDialog
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
import androidx.activity.result.contract.ActivityResultContracts
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
import org.bytedeco.javacpp.avcodec
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.FrameGrabber
import java.io.File


@AndroidEntryPoint
class TemplateActivity : AppCompatActivity() {


    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            //Checking if the received broadcast is for our enqueued download by matching download id
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID === id) {
                Toast.makeText(this@TemplateActivity, "Downloaded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@TemplateActivity, "File not fount", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private val viewModel: TemplateVM by viewModels()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
            override fun uncaughtException(p0: Thread, p1: Throwable) {
                Toast.makeText(this@TemplateActivity, p1.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
        val v = 2 / 0
        print("" + v)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRV()
        registerReceiver(
            onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        requestPermission()
        setObserver()
        getApiData()
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

    private fun getAudio(): Intent {
        val intent = Intent()
        intent.type = "audio/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.action = Intent.ACTION_GET_CONTENT
        return intent
    }

    private fun mixImageAndAudio() {
        var dialog: ProgressDialog? = null
        var recorder: FFmpegFrameRecorder? = null

        //
        dialog = ProgressDialog(this@TemplateActivity)
        dialog.setMessage("Genrating video, Please wait.........")
        dialog.setCancelable(false)
        dialog.show()

        //
        val folder: File = Environment.getExternalStorageDirectory()
        val path: String = folder.absolutePath + "/DCIM/Camera"
        // ArrayList<String> paths = (ArrayList<String>) getListOfFiles(path, "jpg");
        val millis = System.currentTimeMillis()
        val videoPath = "$path/test_sham_$millis.3gp"
        try {


            //audio grabber
            val grabber2: FrameGrabber =
                FFmpegFrameGrabber(folder.absolutePath + "/Samsung/Music/Over_the_horizon.mp3")

            //video grabber
            val grabber1: FrameGrabber = FFmpegFrameGrabber("$path/20140527_133034.jpg")
            grabber1.start()
            grabber2.start()
            recorder = FFmpegFrameRecorder(
                path + "/" + "test_sham_" + millis + ".3gp",
                grabber1.imageWidth,
                grabber1.imageHeight,
                2
            )

            //recorder.setVideoCodec(5);
            recorder.videoCodec = avcodec.AV_CODEC_ID_MPEG4
            // recorder.setVideoCodec(avcodec.AV_CODEC_ID_MP4ALS);
            recorder.format = "3gp"
            //  recorder.setFormat("mp4");
        //    recorder.frameRate = frameRate
            recorder.sampleRate = grabber2.sampleRate
            recorder.videoBitrate = 30
            val startTime = System.currentTimeMillis()
            recorder.start()
            var frame1: Frame?
            var frame2: Frame? = null
            while ((grabber1.grabFrame().also { frame1 = it }) != null || (grabber2.grabFrame()
                    .also { frame2 = it }) != null
            ) {
                recorder.record(frame1)
                recorder.record(frame2)
            }
            recorder.stop()
            grabber1.stop()
            grabber2.stop()
            println("Total Time:- " + recorder!!.timestamp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //
        dialog!!.dismiss()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(videoPath), "video/3gp")
        startActivity(intent)
        Toast.makeText(this@TemplateActivity, "Done:::$videoPath", Toast.LENGTH_SHORT).show()
    }

    private val getAudioLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

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
            this@TemplateActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 12
        )
    }


    private fun getApiData() {
        viewModel.getApiData()
    }


}