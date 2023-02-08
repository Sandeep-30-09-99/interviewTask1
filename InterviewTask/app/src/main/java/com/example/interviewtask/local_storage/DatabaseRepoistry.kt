package com.example.interviewtask.local_storage

import androidx.lifecycle.LiveData
import com.example.interviewtask.model.Product
import javax.inject.Inject

class DatabaseRepoistry
@Inject constructor(
    private val dao: ProductDao
) {

    suspend fun saveProduct(note: Product) = dao.insert(note)
    suspend fun updateProduct(note: Product) = dao.update(note)
    suspend fun deleteProduct(note: Product) = dao.delete(note)
    suspend fun deleteProductById(id: Int) = dao.deleteById(id)
    fun getProduct(): LiveData<List<Product>> = dao.getProductList()

}