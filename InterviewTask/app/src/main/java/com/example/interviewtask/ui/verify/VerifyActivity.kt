package com.example.interviewtask.ui.verify


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ActivitySplashBinding
import com.example.interviewtask.databinding.LayoutVerifyBinding
import com.example.interviewtask.model.Product
import com.example.interviewtask.ui.create_product.CreateProductActivity
import com.example.interviewtask.ui.signup.SignupActivity
import com.example.interviewtask.util.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*


@AndroidEntryPoint
class VerifyActivity : AppCompatActivity() {

    private var code: String? = null
    private val viewModel: VerifyVM by viewModels()

    companion object {
        fun intent(activity: Activity, code: String, phoneNo: String): Intent {
            val intent = Intent(activity, VerifyActivity::class.java)
            intent.putExtra(Constant.PHONE, phoneNo)
            intent.putExtra(Constant.CODE, code)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            return intent
        }
    }


    private lateinit var binding: LayoutVerifyBinding
    private var phoneNo: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentExtras()
        listenClick()
    }

    private fun getIntentExtras() {
        intent?.getStringExtra(Constant.PHONE)?.let {
            phoneNo = it
        }
        intent?.getStringExtra(Constant.CODE)?.let {
            code = it
            binding.tvPhoneNo.text = "+$code $phoneNo"
        }
    }

    private fun listenClick() {
        viewModel.onClick.observe(this, Observer {
            when (it.id) {
                R.id.tvVerify -> {
                    verifyCode()
                }
            }
        })
    }

    private fun verifyCode() {
        phoneNo?.let {
            if (binding.otpView.text.toString() == getCodeFromPhoneNo(it)) {
                startActivity(SignupActivity.intent(this))
            } else {
                binding.otpView.error = "Wrong Otp"
            }
        }

    }

    private fun getCodeFromPhoneNo(number: String): String {
        if (number.length == 10) {
            return "${number[0]}${number[1]}${number[8]}${number[9]}"
        }
        return ""
    }
}


