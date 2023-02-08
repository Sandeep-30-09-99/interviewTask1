package com.example.interviewtask.ui

import android.view.View
import com.example.interviewtask.model.Data

interface AdapterCallback {
    fun onViewClick(v: View, bean: Data)
}