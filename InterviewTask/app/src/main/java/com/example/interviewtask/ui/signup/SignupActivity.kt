package com.example.interviewtask.ui.signup


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivitySigninBinding
import com.example.interviewtask.databinding.ActivitySplashBinding
import com.example.interviewtask.databinding.LayoutOkBinding
import com.example.interviewtask.ui.login.LoginActivity
import com.example.interviewtask.ui.verify.VerifyActivity
import com.example.interviewtask.util.Constant
import com.example.interviewtask.util.dialog.BaseCustomDialog
import com.example.interviewtask.util.hasPermissions
import com.example.interviewtask.util.showInfoToast
import com.example.interviewtask.util.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.File
import java.util.ArrayList


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
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
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
        if (TextUtils.isEmpty(binding.etProductName.text.toString().trim())) {
            binding.etProductName.error = "Please Enter Product Name"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.scrollView.scrollToDescendant(binding.etProductName)
            }
            return false
        }
        if (TextUtils.isEmpty(binding.etProductDescription.text.toString().trim())) {
            binding.etProductDescription.error = "Please Enter Description"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.scrollView.scrollToDescendant(binding.etProductDescription)
            }
            return false
        }
        if (TextUtils.isEmpty(binding.etNormalPrice.text.toString().trim())) {
            binding.etNormalPrice.error = "Please Enter Price"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.scrollView.scrollToDescendant(binding.etNormalPrice)
            }
            return false
        }

        if (TextUtils.isEmpty(binding.etSalePrice.text.toString().trim())) {
            binding.etSalePrice.error = "Please Enter Sale Price"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.scrollView.scrollToDescendant(binding.etSalePrice)
            }
            return false
        }

        return true
    }

    private fun showPopUp() {
        if (!hasPermissions(this, permissions)) {
            permissionResultLauncher.launch(permissions)
        } else {
            val popupMenu = PopupMenu(this, binding.ivEdit)
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
        Manifest.permission.CAMERA
    )


    private lateinit var allGranted: ArrayList<Boolean>


    private val permissionResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
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
        permissionDeniedDialog?.binding?.tvTitle?.text =
            getString(R.string.neccessary_permission)
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
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
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
            }
        }


    private fun getFileUri(): Uri? {
        val file =
            File(
                Environment.getExternalStorageDirectory(),
                "InterviewTask${System.currentTimeMillis()}.jpg"
            )
        return Uri.fromFile(file)
    }


}