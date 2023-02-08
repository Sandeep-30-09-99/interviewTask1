package com.example.interviewtask.ui.home


import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivityHomeBinding
import com.example.interviewtask.ui.AdapterCallback
import com.example.interviewtask.ui.ProductAdapter

import com.example.interviewtask.model.Product
import com.example.interviewtask.ui.create_product.CreateProductActivity
import com.example.interviewtask.ui.show_product.ShowProductActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {


    private val viewModel: HomeVM by viewModels()

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.vm = viewModel
        setContentView(binding.root)
        listenClicks()
    }

    private fun listenClicks() {
        viewModel.onClick.observe(this, Observer {
            when (it.id) {
                R.id.show_product -> {
                    startActivity(ShowProductActivity.intent(this))
                }
                R.id.create_product -> {
                    startActivity(CreateProductActivity.intent(this))

                }
            }
        })
    }


}