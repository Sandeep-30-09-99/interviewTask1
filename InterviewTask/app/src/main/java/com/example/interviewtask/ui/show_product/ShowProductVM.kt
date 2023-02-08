package com.example.interviewtask.ui.show_product


import androidx.lifecycle.LiveData
import com.example.interviewtask.local_storage.DatabaseRepoistry
import com.example.interviewtask.local_storage.ProductDao

import com.example.interviewtask.model.Product

import com.example.interviewtask.ui.base.BaseViewModel
import com.example.interviewtask.util.Coroutine
import com.example.interviewtask.util.SingleLiveEvent

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ShowProductVM @Inject constructor(/*val productDao: ProductDao*/) : BaseViewModel() {




/*
    @Inject
    lateinit var productDao: DatabaseRepoistry*//*


    fun getProductList(): LiveData<List<Product>> {
       // return productDao.getProduct()
    }

    fun deleteProduct(id: Int) {
        Coroutine.IO {
          //  productDao.deleteProductById(id)
        }
    }*/

}