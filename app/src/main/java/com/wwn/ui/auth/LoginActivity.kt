package com.wwn.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.wwn.LocaleHelper
import com.wwn.R
import com.wwn.data.AppRepository
import com.wwn.databinding.ActivityLoginBinding
import com.wwn.ui.BaseActivity
import com.wwn.ui.main.MainActivity

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        highlightLanguage()
        binding.tvLangZh.setOnClickListener { LocaleHelper.apply(this, "zh") }
        binding.tvLangEn.setOnClickListener { LocaleHelper.apply(this, "en") }

        binding.btnLogin.setOnClickListener {
            val result = AppRepository.login(
                binding.etPhone.text.toString(),
                binding.etPassword.text.toString(),
                binding.cbRemember.isChecked
            )
            result.fold({ goMain() }, { Toast.makeText(this, it.message ?: getString(R.string.error_generic), Toast.LENGTH_SHORT).show() })
        }
        binding.btnRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        binding.tvForgot.setOnClickListener { startActivity(Intent(this, ResetPasswordActivity::class.java)) }
        binding.btnGuest.setOnClickListener {
            AppRepository.enterGuest()
            goMain()
        }
        binding.btnWechat.setOnClickListener { oauth("wechat") }
        binding.btnAlipay.setOnClickListener { oauth("alipay") }
        binding.btnGoogle.setOnClickListener { oauth("google") }
    }

    private fun oauth(provider: String) {
        AppRepository.oauthLogin(provider)
        goMain()
    }

    private fun goMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun highlightLanguage() {
        val en = LocaleHelper.getLocaleTag(this) == "en"
        binding.tvLangZh.setTextColor(getColor(if (en) R.color.text_secondary else R.color.primary))
        binding.tvLangEn.setTextColor(getColor(if (en) R.color.primary else R.color.text_secondary))
    }
}
