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
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.interviewtask.R
import com.example.interviewtask.ui.AdapterCallback
import com.example.interviewtask.databinding.ActivityMainBinding
import com.example.interviewtask.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashVM by viewModels()

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeLayout()

        viewModel.onClick.observe(this, Observer {
            when (it.id) {
                R.id.tvGetStarted -> {
                }
            }
        })
    }


    private fun changeLayout() {
        Handler(Looper.getMainLooper()).postDelayed({
            second.visibility = View.VISIBLE
            first.visibility = View.GONE
        }, 1500)
    }
}