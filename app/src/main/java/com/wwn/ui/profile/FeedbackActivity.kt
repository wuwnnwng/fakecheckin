package com.wwn.ui.profile

import android.os.Bundle
import android.widget.Toast
import com.wwn.R
import com.wwn.data.AppRepository
import com.wwn.databinding.ActivityFormBinding
import com.wwn.ui.BaseActivity

class FeedbackActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvTitle.text = getString(R.string.feedback)
        binding.et1.hint = getString(R.string.feedback_hint)
        binding.et2.hint = getString(R.string.phone)
        binding.et3.visibility = android.view.View.GONE
        binding.spinner.visibility = android.view.View.GONE
        listOf(binding.sw1, binding.sw2, binding.sw3, binding.sw4, binding.sw5).forEach { it.visibility = android.view.View.GONE }
        binding.btnSecondary.text = getString(R.string.pick_image)
        binding.btnSecondary.setOnClickListener {
            Toast.makeText(this, "Image upload → POST /feedbacks imageUrls", Toast.LENGTH_SHORT).show()
        }
        binding.btnPrimary.setOnClickListener {
            if (AppRepository.submitFeedback(binding.et1.text.toString())) {
                Toast.makeText(this, R.string.submitted, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
