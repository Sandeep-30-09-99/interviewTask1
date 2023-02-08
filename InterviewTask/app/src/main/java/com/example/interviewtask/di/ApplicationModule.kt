package com.example.interviewtask.di


import android.content.Context
import androidx.room.Room
import com.example.interviewtask.MyApp
import com.example.interviewtask.local_storage.ProductDao
import com.example.interviewtask.local_storage.ProductDatabase
import com.example.interviewtask.local_storage.ProductDatabase.Companion.PRODUCT_DATABASE
import com.example.interviewtask.model.Product
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {


/*
    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, ProductDatabase::class.java, PRODUCT_DATABASE
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    //This annotation marks the method provideDao as a provider of noteDoa.
    @Provides
    @Singleton
    fun provideDao(db: ProductDatabase) = db.noteDao()

    */


}