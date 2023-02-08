package com.example.interviewtask.local_storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.interviewtask.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun noteDao(): ProductDao

    companion object {
        private var instance: ProductDatabase? = null

        const val PRODUCT_DATABASE="Product_Database"


        @Synchronized
        fun getInstance(ctx: Context): ProductDatabase {
            if (instance == null){
             instance = Room.databaseBuilder(
                    ctx.applicationContext, ProductDatabase::class.java,
                    PRODUCT_DATABASE
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            }
            return instance!!

        }
        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: ProductDatabase) {
            val noteDao = db.noteDao()
            CoroutineScope(Dispatchers.IO).launch {
                noteDao.insert(Product(1, "Coffee", "Drinkable Product", 15.20F, 10F, null, null))
                noteDao.insert(Product(2, "Tea", "Drinkable Product", 15.20F, 10F, null, null))
            }
        }
    }


}