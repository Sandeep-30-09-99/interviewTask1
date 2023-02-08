package com.example.interviewtask.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment


fun Fragment.showSuccessToast(message: String) {
    MyToast.success(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.showSuccessToast(message: String) {
    MyToast.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showInfoToast(message: String) {
    MyToast.info(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}
fun Fragment.showErrorToast(message: String) {
    MyToast.error(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}
fun Activity.showErrorToast(message: String) {
    MyToast.error(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.showInfoToast(message: String) {
    MyToast.info(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.successToast(message: String) {
    if (message.isNotEmpty())
        MyToast.success(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.successToast(message: String) {
    if (message.isNotEmpty())
        MyToast.success(this, message, Toast.LENGTH_SHORT, true).show()
}


fun Activity.hasPermissions(context: Context?, permissions: Array<String>?): Boolean {
    if (context != null && permissions != null) {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission!!
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
    }
    return true
}
