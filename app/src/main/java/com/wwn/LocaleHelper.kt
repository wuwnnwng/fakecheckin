package com.wwn

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import com.wwn.data.Session
import java.util.Locale

object LocaleHelper {
    private const val PREF = "ipl_prefs"
    private const val KEY = "locale"

    fun getLocaleTag(context: Context): String {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getString(KEY, "zh") ?: "zh"
    }

    fun persist(context: Context, tag: String) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY, tag)
            .apply()
        Session.settings.locale = tag
    }

    fun apply(activity: Activity, tag: String) {
        if (getLocaleTag(activity) == tag) return
        persist(activity, tag)
        activity.recreate()
    }

    fun currentLanguage(context: Context): String {
        return context.resources.configuration.locales[0].language
    }

    fun wrap(context: Context): Context {
        val tag = getLocaleTag(context)
        val locale = if (tag == "en") Locale.ENGLISH else Locale.SIMPLIFIED_CHINESE
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
