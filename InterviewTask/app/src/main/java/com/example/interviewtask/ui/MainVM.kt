package com.example.interviewtask.ui

import androidx.lifecycle.ViewModel
import com.example.interviewtask.model.Data
import com.example.interviewtask.network.ApiHelper
import com.example.interviewtask.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(val apiService: ApiHelper) : ViewModel() {


    var apiData = SingleLiveEvent<ArrayList<Data>>()
    fun getApiData() {
       /*
               apiService.getData().enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                val l = (response.body() as JsonObject)
                val d = Gson().fromJson<ApiData>(l, ApiData::class.java)
                apiData.postValue(d.list as ArrayList<Data>)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t as HttpException
                // apiData.postValue(Response.error(t.code(), null))
            }

        })*/
    }

}