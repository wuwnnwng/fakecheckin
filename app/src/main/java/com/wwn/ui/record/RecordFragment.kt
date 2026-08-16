package com.wwn.ui.record

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wwn.R
import com.wwn.data.AppRepository
import com.wwn.data.Session
import com.wwn.data.model.UsageRecord
import com.wwn.databinding.FragmentSimpleListBinding
import com.wwn.ui.SimpleTextAdapter

class RecordFragment : Fragment() {
    private var _binding: FragmentSimpleListBinding? = null
    private val binding get() = _binding!!
    private var data = listOf<UsageRecord>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSimpleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val modes = listOf("all", "manual", "auto", "skin_tone")
        val parts = listOf("all", "arm", "underarm", "leg", "face", "back", "bikini")
        binding.spFilter1.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, modes)
        binding.spFilter2.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, parts)
        val adapter = SimpleTextAdapter { index ->
            val record = data[index]
            startActivity(Intent(requireContext(), RecordDetailActivity::class.java).putExtra("id", record.id))
        }
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        val reload = {
            if (Session.isGuest) {
                binding.tvEmpty.text = getString(R.string.guest_no_record)
                adapter.submit(emptyList())
            } else {
                data = AppRepository.queryRecords(
                    binding.spFilter1.selectedItem?.toString(),
                    binding.spFilter2.selectedItem?.toString()
                )
                binding.tvEmpty.text = if (data.isEmpty()) "No records" else ""
                adapter.submit(data.map { "${it.useTime}  ${it.workMode}" to "${it.bodyPart} · gear ${it.gear} · ${it.pulseCount} pulses" })
            }
        }
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = reload()
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.spFilter1.onItemSelectedListener = listener
        binding.spFilter2.onItemSelectedListener = listener
        reload()
    }

    override fun onResume() {
        super.onResume()
        binding.spFilter1.adapter
        if (!Session.isGuest) {
            data = AppRepository.queryRecords(
                binding.spFilter1.selectedItem?.toString(),
                binding.spFilter2.selectedItem?.toString()
            )
            (binding.recycler.adapter as? SimpleTextAdapter)?.submit(
                data.map { "${it.useTime}  ${it.workMode}" to "${it.bodyPart} · gear ${it.gear} · ${it.pulseCount} pulses" }
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
