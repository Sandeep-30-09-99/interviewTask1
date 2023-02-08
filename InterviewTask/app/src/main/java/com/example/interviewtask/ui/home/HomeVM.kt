package com.example.interviewtask.ui.home


import com.example.interviewtask.model.Data
import com.example.interviewtask.network.ApiHelper
import com.example.interviewtask.ui.base.BaseViewModel
import com.example.interviewtask.util.SingleLiveEvent

import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(val apiService: ApiHelper) : BaseViewModel() {

    var apiData = SingleLiveEvent<ArrayList<Data>>()
    fun getApiData() {

    }

}