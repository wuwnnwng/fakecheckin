package com.wwn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.wwn.R
import com.wwn.data.AppRepository
import com.wwn.databinding.ActivityRegisterBinding
import com.wwn.ui.BaseActivity
import com.wwn.ui.main.MainActivity

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSms.setOnClickListener {
            AppRepository.sendSms(binding.etPhone.text.toString(), "register")
            Toast.makeText(this, R.string.demo_sms_hint, Toast.LENGTH_SHORT).show()
        }
        binding.btnSubmit.setOnClickListener {
            if (!binding.cbAgree.isChecked) {
                Toast.makeText(this, R.string.agree_terms, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val result = AppRepository.register(
                binding.etPhone.text.toString(),
                binding.etSms.text.toString(),
                binding.etPassword.text.toString()
            )
            result.fold({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, {
                val msg = if (it.message == "invalid_password") getString(R.string.error_password) else getString(R.string.error_sms)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            })
        }
    }
}
