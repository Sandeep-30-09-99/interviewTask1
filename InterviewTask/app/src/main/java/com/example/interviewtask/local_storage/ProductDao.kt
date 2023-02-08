package com.example.interviewtask.local_storage

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.interviewtask.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun getProductList(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product)


    @Query("DELETE FROM Product WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Delete
    suspend fun delete(product: Product)

    @Update
    suspend fun update(product: Product)
}

