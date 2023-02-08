package com.example.interviewtask.ui


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.interviewtask.databinding.ActivitycCrashLayoutBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CrashReportActivity : AppCompatActivity() {


    private lateinit var binding: ActivitycCrashLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitycCrashLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
         intent?.getStringExtra("error")?.let {
             binding.tvHeader.text =it
             Log.i("Crash","$it")
        }


    }


}