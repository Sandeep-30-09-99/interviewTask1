package com.example.interviewtask.di


import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent



@Module
@InstallIn(SingletonComponent::class)
class ApplicatonModule {


   /* @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().build()
    }

    @Provides
    fun getRetrofit(): Retrofit {
        val gson = GsonBuilder().create()
        return Retrofit.Builder().baseUrl("https://fileupload.vinnisoft.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()

    }

    @Provides
    fun ApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideApiHelper2(apiHelper: ApiHelperImp): ApiHelper = apiHelper
*/

}