package com.example.interviewtask.ui.create_product



import com.example.interviewtask.model.Product

import com.example.interviewtask.ui.base.BaseViewModel
import com.example.interviewtask.util.Coroutine


import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class CreateProductVM @Inject constructor(/*val productDao: ProductDao*/) : BaseViewModel() {


    fun addProduct(data: Product) {
        Coroutine.IO {
            //productDao.insert(data)
        }
    }

    fun updateProduct(data: Product) {
        Coroutine.IO {
          //  productDao.update(data)
        }
    }

}