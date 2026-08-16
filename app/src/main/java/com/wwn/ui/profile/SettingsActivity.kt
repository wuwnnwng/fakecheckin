package com.wwn.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.wwn.LocaleHelper
import com.wwn.R
import com.wwn.data.Session
import com.wwn.databinding.ActivityFormBinding
import com.wwn.ui.BaseActivity

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val s = Session.settings
        binding.tvTitle.text = getString(R.string.settings)
        binding.et1.visibility = View.GONE
        binding.et2.visibility = View.GONE
        binding.et3.visibility = View.GONE
        binding.tvSpinnerHint.visibility = View.VISIBLE
        binding.tvSpinnerHint.text = getString(R.string.language)
        binding.sw1.text = getString(R.string.notify_device)
        binding.sw2.text = getString(R.string.notify_usage)
        binding.sw3.text = getString(R.string.notify_system)
        binding.sw4.text = getString(R.string.notify_sound)
        binding.sw5.text = getString(R.string.notify_vibrate)
        binding.sw1.isChecked = s.deviceAlert
        binding.sw2.isChecked = s.usageAlert
        binding.sw3.isChecked = s.systemAlert
        binding.sw4.isChecked = s.soundEnabled
        binding.sw5.isChecked = s.vibrateEnabled
        val langTags = listOf("zh", "en")
        val langLabels = listOf(getString(R.string.lang_zh), getString(R.string.lang_en))
        binding.spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, langLabels)
        binding.spinner.setSelection(langTags.indexOf(LocaleHelper.getLocaleTag(this)).coerceAtLeast(0))
        binding.btnPrimary.text = getString(R.string.save)
        binding.btnSecondary.text = getString(R.string.clear_cache)
        binding.btnPrimary.setOnClickListener {
            s.deviceAlert = binding.sw1.isChecked
            s.usageAlert = binding.sw2.isChecked
            s.systemAlert = binding.sw3.isChecked
            s.soundEnabled = binding.sw4.isChecked
            s.vibrateEnabled = binding.sw5.isChecked
            val tag = langTags[binding.spinner.selectedItemPosition]
            val languageChanged = LocaleHelper.getLocaleTag(this) != tag
            LocaleHelper.persist(this, tag)
            Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show()
            if (languageChanged) recreate() else finish()
        }
        binding.btnSecondary.setOnClickListener {
            Toast.makeText(this, R.string.cache_cleared, Toast.LENGTH_SHORT).show()
        }
    }
}
