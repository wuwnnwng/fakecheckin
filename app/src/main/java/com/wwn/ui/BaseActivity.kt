package com.wwn.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.wwn.LocaleHelper

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrap(newBase))
    }

    override fun onResume() {
        super.onResume()
        val expected = if (LocaleHelper.getLocaleTag(this) == "en") "en" else "zh"
        if (LocaleHelper.currentLanguage(this) != expected) {
            recreate()
        }
    }
}
