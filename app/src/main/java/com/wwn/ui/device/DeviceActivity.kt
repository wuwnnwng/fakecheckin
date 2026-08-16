package com.wwn.ui.device

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.wwn.R
import com.wwn.data.AppRepository
import com.wwn.data.Session
import com.wwn.data.model.Device
import com.wwn.databinding.ActivityDeviceBinding
import com.wwn.ui.BaseActivity
import com.wwn.ui.SimpleTextAdapter

class DeviceActivity : BaseActivity() {
    private lateinit var binding: ActivityDeviceBinding
    private var scanned = listOf<Device>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = SimpleTextAdapter { index ->
            val device = scanned[index]
            AppRepository.bindDevice(device)
            Toast.makeText(this, R.string.device_connected, Toast.LENGTH_SHORT).show()
            renderInfo()
        }
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
        binding.btnScan.setOnClickListener {
            scanned = AppRepository.scanDevices()
            adapter.submit(scanned.map { "${it.model}  RSSI ${it.rssi}" to "${it.deviceSn}  ${it.bleMac}" })
        }
        binding.btnQr.setOnClickListener {
            Toast.makeText(this, "QR bind placeholder → POST ${com.wwn.data.api.ApiConfig.DEVICE_BIND}", Toast.LENGTH_LONG).show()
        }
        binding.btnUnbind.setOnClickListener {
            Session.currentDevice?.let { AppRepository.unbind(it.id) }
            renderInfo()
        }
        renderInfo()
        binding.btnScan.performClick()
    }

    private fun renderInfo() {
        val d = Session.currentDevice
        binding.tvInfo.text = if (d == null) {
            getString(R.string.device_none)
        } else {
            "${getString(R.string.device_info)}\n${d.model}\n${getString(R.string.firmware)} ${d.firmwareVersion}\n${getString(R.string.total_uses)} ${d.totalPulses}\n${getString(R.string.remain_uses)} ${d.remainPulses}\n${getString(R.string.work_status)} ${d.workStatus}"
        }
    }
}
