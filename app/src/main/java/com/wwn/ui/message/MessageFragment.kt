package com.wwn.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wwn.data.AppRepository
import com.wwn.databinding.FragmentSimpleListBinding
import com.wwn.ui.SimpleTextAdapter

class MessageFragment : Fragment() {
    private var _binding: FragmentSimpleListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSimpleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cats = listOf("all", "device", "usage", "system")
        binding.spFilter1.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, cats)
        binding.spFilter2.visibility = View.GONE
        val adapter = SimpleTextAdapter {}
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        val reload = {
            val list = AppRepository.messages(binding.spFilter1.selectedItem?.toString())
            adapter.submit(list.map { "${it.title}  ${if (it.read) "" else "•"}" to "${it.category} · ${it.createdAt}\n${it.content}" })
        }
        binding.spFilter1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = reload()
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        reload()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
