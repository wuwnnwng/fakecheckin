package com.wwn.ui.record

import android.os.Bundle
import android.widget.Toast
import com.wwn.R
import com.wwn.data.AppRepository
import com.wwn.databinding.ActivityFormBinding
import com.wwn.ui.BaseActivity

class RecordDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getLongExtra("id", -1)
        val record = AppRepository.queryRecords(null, null).find { it.id == id }
        binding.tvTitle.text = record?.recordNo ?: ""
        binding.tvBody.text = record?.let {
            "${it.useTime}\n${it.workMode} / ${it.bodyPart}\ngear ${it.gear}\n${it.durationSec}s\n${it.pulseCount} pulses\n${it.deviceModel}"
        }
        binding.et1.hint = getString(R.string.remark)
        binding.et1.setText(record?.remark.orEmpty())
        binding.et2.visibility = android.view.View.GONE
        binding.et3.visibility = android.view.View.GONE
        binding.spinner.visibility = android.view.View.GONE
        hideSwitches(binding)
        binding.btnPrimary.setOnClickListener {
            AppRepository.updateRemark(id, binding.et1.text.toString())
            Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.btnSecondary.visibility = android.view.View.GONE
    }

    private fun hideSwitches(binding: ActivityFormBinding) {
        listOf(binding.sw1, binding.sw2, binding.sw3, binding.sw4, binding.sw5).forEach {
            it.visibility = android.view.View.GONE
        }
    }
}
