package com.wwn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.wwn.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        renderIdleState()

        binding.btnCheckIn.setOnClickListener {
            val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            binding.tvTime.text = now
            binding.tvStatus.text = getString(R.string.status_done)
            binding.tvResult.text = getString(R.string.check_in_success, now)
            binding.btnCheckIn.isEnabled = false
            binding.btnCheckIn.text = getString(R.string.checked_in)
            Snackbar.make(binding.root, R.string.check_in_snackbar, Snackbar.LENGTH_SHORT).show()
        }

        binding.btnReset.setOnClickListener {
            renderIdleState()
        }
    }

    private fun renderIdleState() {
        binding.tvTime.text = getString(R.string.time_placeholder)
        binding.tvStatus.text = getString(R.string.status_idle)
        binding.tvResult.text = getString(R.string.result_placeholder)
        binding.btnCheckIn.isEnabled = true
        binding.btnCheckIn.text = getString(R.string.check_in)
    }
}
