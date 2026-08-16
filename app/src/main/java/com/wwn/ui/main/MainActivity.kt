package com.wwn.ui.main

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.wwn.R
import com.wwn.data.Session
import com.wwn.databinding.ActivityMainBinding
import com.wwn.ui.BaseActivity
import com.wwn.ui.control.ControlFragment
import com.wwn.ui.message.MessageFragment
import com.wwn.ui.profile.ProfileFragment
import com.wwn.ui.record.RecordFragment

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        switchTo(ControlFragment())
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.tab_control -> switchTo(ControlFragment())
                R.id.tab_records -> switchTo(RecordFragment())
                R.id.tab_messages -> switchTo(MessageFragment())
                R.id.tab_me -> switchTo(ProfileFragment())
            }
            true
        }
        if (Session.firstLaunch) {
            Session.firstLaunch = false
            AlertDialog.Builder(this)
                .setTitle(R.string.guide_title)
                .setMessage(R.string.guide_body)
                .setPositiveButton(R.string.ok, null)
                .show()
        }
    }

    private fun switchTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
