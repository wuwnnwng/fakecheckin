package com.wwn.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wwn.R
import com.wwn.data.AppRepository
import com.wwn.databinding.FragmentSimpleListBinding
import com.wwn.ui.BaseActivity
import com.wwn.ui.SimpleTextAdapter

class HelpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = FragmentSimpleListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.filterRow.visibility = android.view.View.GONE
        binding.tvEmpty.text = getString(R.string.faq)
        val faqs = AppRepository.faqs()
        val adapter = SimpleTextAdapter {}
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter
        adapter.submit(faqs.map { it.question to it.answer } + listOf(getString(R.string.feedback) to "Tap to write feedback"))
        binding.recycler.post {
            // last item opens feedback via a dedicated button area
        }
        binding.tvEmpty.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }
    }
}
