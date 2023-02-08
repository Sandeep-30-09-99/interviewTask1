package com.example.interviewtask.ui.create_product


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
class CreateProductActivity : AppCompatActivity() {

    companion object {
        fun intent(activity: Activity, product: Product? = null): Intent {
            val intent = Intent(activity, CreateProductActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.PRODUCT, product)
            return intent
        }
    }

    lateinit var productDao: ProductDao
    private fun initDatabase() {
        productDao = ProductDatabase.getInstance(this).noteDao()
    }


    private val viewModel: CreateProductVM by viewModels()
    private var product: Product? = null
    private lateinit var binding: ActivityCreateProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        binding = ActivityCreateProductBinding.inflate(layoutInflater)

        if (Build.VERSION.SDK_INT >= 33) {
            intent?.getParcelableExtra<Product>(Constant.PRODUCT, Product::class.java)?.let {
                product = it
                setDetails(it)
            }
        } else {
            intent?.getParcelableExtra<Product>(Constant.PRODUCT)?.let {
                product = it

                setDetails(it)
            }
        }
        setHeader()
        initDatabase()
        setContentView(binding.root)
        binding.vm = viewModel
        initPermissionRequiredForTaskDialog()
        listenClicks()
    }

    private fun setDetails(product: Product?) {
        product?.let {
            binding.bean = it
            if (it.product_photo != null) {
                binding.sivProfile.setImageURI(it.product_photo.toUri())
            }
        }
    }

    private fun setHeader() {
        if (product == null) {
            binding.header.title.text = "Create New Product"
            binding.tvCreate.text = "Create"

        } else {
            binding.header.title.text = "Update Product"
            binding.tvCreate.text = "Update"

        }
    }

    private fun listenClicks() {
        viewModel.onClick.observe(this) {
            when (it.id) {
                R.id.sivProfile -> {
                    showPopUp()
                }
                R.id.tvCreate -> {
                    if (product == null)
                        createProduct()
                    else
                        updateProduct()
                }
            }
        }

    }

    private fun updateProduct() {
        if (isEmptyField()) {
            val p = Product(
                name = binding.etProductName.text.toString().trim(),
                description = binding.etProductDescription.text.toString().trim(),
                sale_price = binding.etSalePrice.text.toString().toFloat(),
                regular_price = binding.etNormalPrice.text.toString().toFloat(),
                product_photo = cameraImageUri.toString(),
                colors = null
            )
            Coroutine.IO {
                productDao.update(p)
            }
        }
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


    private fun createProduct() {
        if (isEmptyField()) {
            val p = Product(
                name = binding.etProductName.text.toString().trim(),
                description = binding.etProductDescription.text.toString().trim(),
                sale_price = binding.etSalePrice.text.toString().toFloat(),
                regular_price = binding.etNormalPrice.text.toString().toFloat(),
                product_photo = cameraImageUri.toString(),
                colors = null
            )
            Coroutine.IO {
                productDao.insert(p)
            }
        }
    }




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


    private fun getFileUri(): Uri? {
        val file =
            File(
                Environment.getExternalStorageDirectory(),
                "InterviewTask${System.currentTimeMillis()}.jpg"
            )
        return Uri.fromFile(file)
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

}