package com.example.interviewtask.ui.show_product


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivityShowProductBinding
import com.example.interviewtask.local_storage.ProductDao
import com.example.interviewtask.local_storage.ProductDatabase
import com.example.interviewtask.ui.AdapterCallback
import com.example.interviewtask.ui.ProductAdapter
import com.example.interviewtask.model.Product
import com.example.interviewtask.ui.create_product.CreateProductActivity
import com.example.interviewtask.ui.view_photo.ViewPhotoActivity
import com.example.interviewtask.util.Coroutine
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShowProductActivity : AppCompatActivity() {

    companion object {
        fun intent(activity: Activity): Intent {
            val intent = Intent(activity, ShowProductActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            return intent
        }

    }


    lateinit var productDao: ProductDao
    private fun initDatabase() {
        productDao = ProductDatabase.getInstance(this).noteDao()
    }

    private val viewModel: ShowProductVM by viewModels()

    private lateinit var binding: ActivityShowProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        binding = ActivityShowProductBinding.inflate(layoutInflater)
        binding.header.title.text = "Product List"
        initDatabase()
        setContentView(binding.root)
        binding.vm = viewModel
        initRV()
        listenClicks()
        setObserver()
        //viewModel.getProductList()
    }


    private var productAdapter: ProductAdapter? = null
    private fun initRV() {
        productAdapter = ProductAdapter(this, object : AdapterCallback {
            override fun onViewClick(v: View, pos: Int, bean: Product) {
                when (v.id) {
                    R.id.ivDelete -> {
                        Coroutine.IO {
                            productDao.delete(bean)
                        }
                        productAdapter?.removeAt(pos)
                    }
                    R.id.ivEdit -> {
                        startActivity(CreateProductActivity.intent(this@ShowProductActivity, bean))

                    }
                    R.id.sivProfile -> {
                        startActivity(
                            ViewPhotoActivity.intent(
                                this@ShowProductActivity,
                                bean.product_photo
                            )
                        )
                    }
                }
            }
        })
        binding.rvProduct.adapter = productAdapter

    }


    private fun listenClicks() {
        viewModel.onClick.observe(this, Observer {
            when (it.id) {
                R.id.iv -> {
                }
            }
        })

    }

    private fun setObserver() {
        productDao.getProductList().observe(this) {
            if (it.size > 0) {
                productAdapter?.setList(
                    it as ArrayList<Product>

                )
            }
        }

    }


}