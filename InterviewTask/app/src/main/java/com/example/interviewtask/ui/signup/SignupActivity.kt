package com.example.interviewtask.ui.signup


import android.Manifest
import android.R.attr.bitmap
import android.R.attr.data
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivitySigninBinding
import com.example.interviewtask.databinding.LayoutOkBinding
import com.example.interviewtask.ui.login.LoginActivity
import com.example.interviewtask.util.dialog.BaseCustomDialog
import com.example.interviewtask.util.hasPermissions
import com.example.interviewtask.util.showInfoToast
import com.example.interviewtask.util.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.File


@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {


    companion object {
        fun intent(activity: Activity): Intent {
            val intent = Intent(activity, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            return intent
        }
    }

    private val viewModel: SignVM by viewModels()

    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
       /* val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())*/
        binding.vm = viewModel
        initPermissionRequiredForTaskDialog()
        listenClicks()
    }

    private fun listenClicks() {
        viewModel.onClick.observe(this, Observer {
            when (it.id) {
                R.id.tvCreate -> {
                    if (!isEmptyField()) {
                        showSuccessToast("Account Created")
                        goToLoginActivity()
                        finish()
                    }
                }
                R.id.ivBack -> {
                    goToLoginActivity()
                }
                R.id.sivProfile, R.id.ivEdit -> {
                    showPopUp()
                }
            }
        })
    }

    private fun goToLoginActivity() {
        startActivity(LoginActivity.intent(this))
    }

    private fun isEmptyField(): Boolean {
        if (TextUtils.isEmpty(binding.etName.text.toString().trim())) {
            binding.etName.error = "Please Enter Name"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.scrollView.scrollToDescendant(binding.etName)
            }
            return true
        }
        if (TextUtils.isEmpty(binding.etLastName.text.toString().trim())) {
            binding.etLastName.error = "Please Enter Last Name"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.scrollView.scrollToDescendant(binding.etLastName)
            }
            return true
        }
        if (TextUtils.isEmpty(binding.etPhoneNumber.text.toString().trim())) {
            binding.etPhoneNumber.error = "Please Enter Phone No"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.scrollView.scrollToDescendant(binding.etPhoneNumber)
            }
            return true
        }

        if (TextUtils.isEmpty(binding.etPostCode.text.toString().trim())) {
            binding.etPostCode.error = "Please Enter Post Price"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.scrollView.scrollToDescendant(binding.etPostCode)
            }
            return true
        }

        return false
    }

    private fun showPopUp() {
        if (!hasPermissions(this, permissions)) {
            permissionResultLauncher.launch(permissions)
        } else {
            val popupMenu = PopupMenu(this, binding.sivProfile)
            popupMenu.menu.add("Open Camera")
            popupMenu.menu.add("Open Gallery")
            popupMenu.menu.add("Cancel")
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem?.title.toString()) {
                    "Open Camera" -> {
                        openCamera()
                        popupMenu.dismiss()
                    }
                    "Open Gallery" -> {
                        openGallery()
                        popupMenu.dismiss()
                    }
                    "Cancel" -> {
                        popupMenu.dismiss()
                    }
                }
                true
            }
            popupMenu.show()
        }
    }


    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )


    private lateinit var allGranted: ArrayList<Boolean>


    private val permissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        allGranted = ArrayList<Boolean>()// Handle Permission granted/rejected
        permissions.entries.forEach {
            it.key
            val isGranted = it.value
            allGranted.add(isGranted)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(it.key)) {
                    permissionDeniedDialog?.show()
                } else if (!it.value) {
                    showInfoToast("Go To Setting to enable permission.")
                }
            } else {
                if (!it.value) {
                    showInfoToast("Go To Setting to enable permission.")
                }
            }
        }
        if (!allGranted.contains(false)) {
            showPopUp()
        }
    }


    private var permissionDeniedDialog: BaseCustomDialog<LayoutOkBinding>? = null
    private fun initPermissionRequiredForTaskDialog() {
        permissionDeniedDialog = BaseCustomDialog(this, R.layout.layout_ok) {
            when (it.id) {
                R.id.tvOk -> {
                    permissionDeniedDialog?.cancel()
                    permissionResultLauncher.launch(permissions)
                }
            }
        }
        permissionDeniedDialog?.binding?.tvTitle?.text = getString(R.string.neccessary_permission)
        permissionDeniedDialog?.setCancelable(true)
    }

    private var cameraImageUri: Uri? = null
    private fun openCamera() {
        cameraImageUri = getFileUri()
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
        launcher.launch(captureIntent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                cameraImageUri = if (it.data?.data != null) {
                    it.data?.data!!
                } else {
                    cameraImageUri
                }
                binding.sivProfile.setImageURI(cameraImageUri)
            } else {
                Log.i("slfs", cameraImageUri.toString())
                Glide.with(this@SignupActivity).load(imgFile).into(binding.sivProfile)
              /*  it.data?.extras?.let {b->
                    val bitmap= b["data"] as Bitmap
                    binding.sivProfile.setImageBitmap(bitmap)

                }*/

            }
        }


    private var imgFile: File? = null
    private fun getFileUri(): Uri? {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "InterviewTask${System.currentTimeMillis()}.jpg"
        )
        imgFile = file
        file.let {
            return FileProvider.getUriForFile(this, "com.example.interviewtask.provider", it)
        }
    }


}