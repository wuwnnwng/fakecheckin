package com.wwn.ui.profile

import android.os.Bundle
import com.wwn.BuildConfig
import com.wwn.R
import com.wwn.databinding.ActivityFormBinding
import com.wwn.ui.BaseActivity

class AboutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvTitle.text = getString(R.string.about)
        binding.tvBody.text = "IPL Care ${BuildConfig.VERSION_NAME}\n\n${getString(R.string.user_agreement)}\n${getString(R.string.privacy_policy)}"
        binding.et1.visibility = android.view.View.GONE
        binding.et2.visibility = android.view.View.GONE
        binding.et3.visibility = android.view.View.GONE
        binding.spinner.visibility = android.view.View.GONE
        listOf(binding.sw1, binding.sw2, binding.sw3, binding.sw4, binding.sw5).forEach { it.visibility = android.view.View.GONE }
        binding.btnPrimary.visibility = android.view.View.GONE
        binding.btnSecondary.visibility = android.view.View.GONE
    }
}
