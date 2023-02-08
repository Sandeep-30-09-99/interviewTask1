package com.example.interviewtask.ui

import android.view.View
import com.example.interviewtask.model.Product

interface AdapterCallback {
    fun onViewClick(v: View, pos:Int,bean: Product)
}