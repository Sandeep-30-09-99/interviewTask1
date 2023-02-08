package com.example.interviewtask.ui.template

import androidx.lifecycle.ViewModel
import com.example.interviewtask.model.ApiData
import com.example.interviewtask.model.Data
import com.example.interviewtask.network.ApiHelper
import com.example.interviewtask.util.SingleLiveEvent
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TemplateVM @Inject constructor(val apiService: ApiHelper) : ViewModel() {


    var apiData = SingleLiveEvent<ArrayList<Data>>()
    fun getApiData() {
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

        })
    }

}