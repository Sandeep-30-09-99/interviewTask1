package com.example.interviewtask.util

import com.example.interviewtask.model.Product
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutine {

    fun IO(tasK: suspend () -> Unit, handler: CoroutineExceptionHandler) {
        CoroutineScope(Dispatchers.IO + handler).launch {
            tasK()
        }
    }

    fun IO(tasK: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            tasK()
        }
    }

    fun Main(tasK: suspend () -> Unit, handler: CoroutineExceptionHandler) {
        CoroutineScope(Dispatchers.Main + handler).launch {
            tasK()
        }
    }

    fun Main(tasK: suspend () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            tasK()
        }
    }
}