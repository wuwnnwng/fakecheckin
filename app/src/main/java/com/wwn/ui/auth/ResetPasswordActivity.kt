package com.wwn.ui.auth

import android.os.Bundle
import android.widget.Toast
import com.wwn.R
import com.wwn.data.AppRepository
import com.wwn.databinding.ActivityResetBinding
import com.wwn.ui.BaseActivity

class ResetPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSms.setOnClickListener {
            AppRepository.sendSms(binding.etPhone.text.toString(), "reset_password")
            Toast.makeText(this, R.string.demo_sms_hint, Toast.LENGTH_SHORT).show()
        }
        binding.btnSubmit.setOnClickListener {
            val result = AppRepository.resetPassword(
                binding.etPhone.text.toString(),
                binding.etSms.text.toString(),
                binding.etPassword.text.toString()
            )
            result.fold({
                Toast.makeText(this, R.string.ok, Toast.LENGTH_SHORT).show()
                finish()
            }, { Toast.makeText(this, R.string.error_sms, Toast.LENGTH_SHORT).show() })
        }
    }
}
