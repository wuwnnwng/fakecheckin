package com.wwn.ui.control

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.wwn.R
import com.wwn.data.AppRepository
import com.wwn.data.Session
import com.wwn.databinding.FragmentControlBinding
import com.wwn.ui.device.DeviceActivity

class ControlFragment : Fragment() {
    private var _binding: FragmentControlBinding? = null
    private val binding get() = _binding!!
    private var startedAt = 0L
    private var pulses = 0

    private val parts = listOf("arm", "underarm", "leg", "face", "back", "bikini")
    private val partLabels by lazy {
        listOf(
            getString(R.string.part_arm),
            getString(R.string.part_underarm),
            getString(R.string.part_leg),
            getString(R.string.part_face),
            getString(R.string.part_back),
            getString(R.string.part_bikini)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spPart.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, partLabels)
        refreshBar()
        binding.btnOpenDevice.setOnClickListener { startActivity(Intent(requireContext(), DeviceActivity::class.java)) }
        binding.rgMode.setOnCheckedChangeListener { _, id ->
            val mode = when (id) {
                R.id.rbAuto -> "auto"
                R.id.rbSkin -> "skin_tone"
                else -> "manual"
            }
            if (ensureDevice()) AppRepository.sendCommand("set_mode", mode = mode)
        }
        binding.spPart.setOnItemSelectedListener(simpleListener {
            if (ensureDevice(false)) AppRepository.sendCommand("set_part", bodyPart = parts[it])
        })
        binding.sbGear.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                val gear = progress + 1
                binding.tvGear.text = getString(R.string.gear) + " $gear"
                if (fromUser && ensureDevice(false)) AppRepository.sendCommand("set_gear", gear = gear)
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })
        binding.swIce.setOnCheckedChangeListener { _, checked ->
            binding.swIce.text = if (checked) getString(R.string.ice_on) else getString(R.string.ice_off)
            if (ensureDevice(false)) AppRepository.sendCommand("set_ice", iceOn = checked)
        }
        binding.btnTimer5.setOnClickListener { if (ensureDevice()) AppRepository.sendCommand("set_timer", timerMinutes = 5) }
        binding.btnTimer10.setOnClickListener { if (ensureDevice()) AppRepository.sendCommand("set_timer", timerMinutes = 10) }
        binding.btnStart.setOnClickListener {
            if (!ensureDevice()) return@setOnClickListener
            startedAt = System.currentTimeMillis()
            pulses = 20
            val status = AppRepository.sendCommand("start", timerMinutes = 10)
            renderStatus(status.workStatus)
        }
        binding.btnPause.setOnClickListener {
            if (ensureDevice()) renderStatus(AppRepository.sendCommand("pause").workStatus)
        }
        binding.btnStop.setOnClickListener { stopSession() }
    }

    override fun onResume() {
        super.onResume()
        refreshBar()
    }

    private fun stopSession() {
        if (!ensureDevice()) return
        AppRepository.sendCommand("stop")
        val duration = if (startedAt == 0L) 0 else ((System.currentTimeMillis() - startedAt) / 1000).toInt()
        val record = AppRepository.finishSessionAndRecord(duration.coerceAtLeast(1), pulses)
        renderStatus("idle")
        if (Session.isGuest) {
            Toast.makeText(requireContext(), R.string.guest_no_record, Toast.LENGTH_LONG).show()
        } else if (record != null) {
            Toast.makeText(requireContext(), R.string.session_end, Toast.LENGTH_SHORT).show()
        }
        startedAt = 0L
    }

    private fun refreshBar() {
        val device = Session.currentDevice
        binding.tvDeviceBar.text = if (device == null) {
            getString(R.string.device_none)
        } else {
            "${device.model} · ${getString(R.string.device_connected)} · ${device.workStatus}"
        }
        val status = Session.deviceStatus
        if (status != null) {
            binding.tvPulse.text = "${getString(R.string.pulse_done)} ${status.pulseDone} / ${status.pulseRemain}"
            renderStatus(status.workStatus)
            binding.sbGear.progress = (status.gear - 1).coerceIn(0, 9)
            binding.tvGear.text = getString(R.string.gear) + " ${status.gear}"
        }
    }

    private fun renderStatus(status: String) {
        binding.tvStatus.text = "${getString(R.string.work_status)}: $status"
    }

    private fun ensureDevice(toast: Boolean = true): Boolean {
        if (Session.currentDevice != null) return true
        if (toast) Toast.makeText(requireContext(), R.string.need_device, Toast.LENGTH_SHORT).show()
        return false
    }

    private fun simpleListener(block: (Int) -> Unit) = object : android.widget.AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) = block(position)
        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
