package com.example.interviewtask.ui.view_photo


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivityCreateProductBinding
import com.example.interviewtask.databinding.ActivityViewPhotoBinding
import com.example.interviewtask.databinding.LayoutOkBinding
import com.example.interviewtask.local_storage.ProductDao
import com.example.interviewtask.local_storage.ProductDatabase
import com.example.interviewtask.model.Product
import com.example.interviewtask.util.Constant
import com.example.interviewtask.util.Coroutine
import com.example.interviewtask.util.dialog.BaseCustomDialog
import com.example.interviewtask.util.hasPermissions
import com.example.interviewtask.util.showInfoToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.ArrayList


@AndroidEntryPoint
class ViewPhotoActivity : AppCompatActivity() {

    companion object {
        fun intent(activity: Activity, photo: String? = null): Intent {
            val intent = Intent(activity, ViewPhotoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.Photo, photo)
            return intent
        }
    }


    private var photo: String? = null
    private lateinit var binding: ActivityViewPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        binding = ActivityViewPhotoBinding.inflate(layoutInflater)
        intent?.getStringExtra(Constant.PRODUCT)?.let {
            photo = it
            binding.sivProfile.setImageURI(photo?.toUri())
        }
        binding.header.title.text = "Image"
    }


}