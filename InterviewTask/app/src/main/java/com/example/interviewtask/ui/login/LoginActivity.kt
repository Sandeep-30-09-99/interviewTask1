package com.example.interviewtask.ui.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.interviewtask.R
import com.example.interviewtask.databinding.LayoutCodeBinding
import com.example.interviewtask.databinding.LayoutLoginBinding
import com.example.interviewtask.ui.signup.SignupActivity
import com.example.interviewtask.ui.verify.VerifyActivity
import com.example.interviewtask.util.dialog.BaseCustomDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginVM by viewModels()

    companion object {
        fun intent(activity: Activity): Intent {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            return intent
        }
    }

    private lateinit var binding: LayoutLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = viewModel
        binding.ccp.overrideClickListener { }
        listenClick()
    }

    private fun listenClick() {
        viewModel.onClick.observe(this, Observer {
            when (it.id) {
                R.id.ivBack -> {
                    onBackPressedDispatcher.onBackPressed()
                }
                R.id.tvRequestOtp -> {
                    if (!isEmptyField()) {
                        initDialog(getCodeFromPhoneNo(binding.tvPhone.text.toString()))
                    }
                }
            }
        })
    }

    private var codeDialog: BaseCustomDialog<LayoutCodeBinding>? = null
    private fun initDialog(code: String) {
        if (codeDialog == null) {
            codeDialog = BaseCustomDialog(this, R.layout.layout_code) {
                when (it.id) {
                     R.id.tvTitle, R.id.otpView -> {
                        codeDialog?.cancel()
                        goToVerifyActivity()
                    }


                }
            }
        }

        codeDialog?.binding?.otpView?.setText(code)
        codeDialog?.setOnDismissListener {
            goToVerifyActivity()
        }
        codeDialog?.setCancelable(true)
        codeDialog?.dismiss()
        codeDialog?.show()
    }

    private fun goToVerifyActivity() {
        startActivity(
            VerifyActivity.intent(
                this, code = "+91", phoneNo = binding.tvPhone.text.toString()
            )
        )
    }

    private fun getCodeFromPhoneNo(number: String): String {
        if (number.length == 10) {
            return "${number[0]}${number[1]}${number[8]}${number[9]}"
        }
        return ""
    }

    private fun isEmptyField(): Boolean {
        if (binding.tvPhone.text.toString().isEmpty()) {
            binding.tvPhone.error = "Please Enter Mobile Number"
            return true
        }
        if (binding.tvPhone.text.toString().trim().length != 10) {
            binding.tvPhone.error = "Wrong Number Format"
            return true
        }
        return false
    }


}