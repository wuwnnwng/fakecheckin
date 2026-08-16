package com.wwn

import android.app.Application
import android.content.Context
import com.wwn.data.Session

class IplApp : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.wrap(base))
    }

    override fun onCreate() {
        super.onCreate()
        Session.settings.locale = LocaleHelper.getLocaleTag(this)
    }
}
