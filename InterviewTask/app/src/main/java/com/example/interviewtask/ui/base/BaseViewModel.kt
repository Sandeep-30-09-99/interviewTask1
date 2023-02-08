package com.example.interviewtask.ui.base

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.interviewtask.util.SingleLiveEvent


open class BaseViewModel : ViewModel() {

    val onClick: SingleLiveEvent<View> = SingleLiveEvent<View>()

    override fun onCleared() {
        super.onCleared()
    }

    open fun onClick(view: View) {
        onClick.value = view
    }
}