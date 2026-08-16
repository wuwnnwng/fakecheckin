package com.wwn.ui.profile

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.wwn.R
import com.wwn.data.AppRepository
import com.wwn.data.Session
import com.wwn.databinding.ActivityFormBinding
import com.wwn.ui.BaseActivity

class EditProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = Session.user
        binding.tvTitle.text = getString(R.string.profile)
        binding.et1.hint = getString(R.string.nickname)
        binding.et1.setText(user?.nickname.orEmpty())
        binding.et2.hint = getString(R.string.age)
        binding.et2.setText(user?.age?.toString().orEmpty())
        binding.et3.visibility = android.view.View.GONE
        val genders = listOf(getString(R.string.gender_unknown), getString(R.string.gender_male), getString(R.string.gender_female))
        binding.spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        binding.spinner.setSelection(user?.gender ?: 0)
        listOf(binding.sw1, binding.sw2, binding.sw3, binding.sw4, binding.sw5).forEach { it.visibility = android.view.View.GONE }
        binding.btnSecondary.visibility = android.view.View.GONE
        binding.btnPrimary.setOnClickListener {
            AppRepository.updateProfile(
                binding.et1.text.toString(),
                binding.spinner.selectedItemPosition,
                binding.et2.text.toString().toIntOrNull()
            )
            Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
